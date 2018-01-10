package config;

import model.Event;
import model.LoginResult;
import model.User;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import service.AuthService;
import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;
import spark.utils.StringUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class WebConfig {

    private static final String USER_SESSION_ID = "user";
    private AuthService service;


    public WebConfig(AuthService service) {
        this.service = service;
        staticFileLocation("/public");
        setupRoutes();
    }

    private void setupRoutes() {

        get("/", (req, res) -> {
            User user = getAuthenticatedUser(req);
            Map<String, Object> map = new HashMap<>();
            map.put("pageTitle", "Timeline");
            map.put("user", user);
//            List<Message> messages = service.getUserFullTimelineMessages(user);
//            map.put("messages", messages);
            return new ModelAndView(map, "timeline.ftl");
        }, new FreeMarkerEngine());
        before("/", (req, res) -> {
            User user = getAuthenticatedUser(req);
            if(user == null) {
                res.redirect("/events");
                halt();
            }
        });

        get("/events", (req, res) -> {
            User user = getAuthenticatedUser(req);
            Map<String, Object> map = new HashMap<>();
            map.put("pageTitle", "Events");
            map.put("user", user);
            List<Event> events = service.getPublicTimelineMessages();
            map.put("events", events);
            return new ModelAndView(map, "timeline.ftl");
        }, new FreeMarkerEngine());

        get("/t/:username", (req, res) -> {
            String username = req.params(":username");
            User profileUser = service.getUserbyUsername(username);

            User authUser = getAuthenticatedUser(req);
            boolean followed = false;

            List<Event> events = service.getUserTimelineMessages(profileUser);

            Map<String, Object> map = new HashMap<>();
            map.put("pageTitle", username + "'s Timeline");
            map.put("user", authUser);
            map.put("profileUser", profileUser);
            map.put("events", events);
            return new ModelAndView(map, "timeline.ftl");
        }, new FreeMarkerEngine());
		/*
		 * Checks if the user exists
		 */
        before("/t/:username", (req, res) -> {
            String username = req.params(":username");
            User profileUser = service.getUserbyUsername(username);
            if(profileUser == null) {
                halt(404, "User not Found");
            }
        });


		/*
		 * Presents the login form or redirect the user to
		 * her timeline if it's already logged in
		 */
        get("/login", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            if(req.queryParams("r") != null) {
                map.put("message", "You were successfully registered and can login now");
            }
            return new ModelAndView(map, "login.ftl");
        }, new FreeMarkerEngine());
		/*
		 * Logs the user in.
		 */
        post("/login", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            User user = new User();
            try {
                MultiMap<String> params = new MultiMap<String>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                BeanUtils.populate(user, params);
            } catch (Exception e) {
                halt(501);
                return null;
            }
            LoginResult result = service.checkUser(user);
            if(result.getUser() != null) {
                addAuthenticatedUser(req, result.getUser());
                res.redirect("/");
                halt();
            } else {
                map.put("error", result.getError());
            }
            map.put("username", user.getUsername());
            return new ModelAndView(map, "login.ftl");
        }, new FreeMarkerEngine());
		/*
		 * Checks if the user is already authenticated
		 */
        before("/login", (req, res) -> {
            User authUser = getAuthenticatedUser(req);
            if(authUser != null) {
                res.redirect("/");
                halt();
            }
        });


		/*
		 * Presents the register form or redirect the user to
		 * her timeline if it's already logged in
		 */
        get("/register", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "register.ftl");
        }, new FreeMarkerEngine());
		/*
		 * Registers the user.
		 */
        post("/register", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            User user = new User();
            try {
                MultiMap<String> params = new MultiMap<String>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                BeanUtils.populate(user, params);
            } catch (Exception e) {
                halt(501);
                return null;
            }
            String error = user.validate();
            if(StringUtils.isEmpty(error)) {
                User existingUser = service.getUserbyUsername(user.getUsername());
                if(existingUser == null) {
                    service.registerUser(user);
                    res.redirect("/login?r=1");
                    halt();
                } else {
                    error = "The username is already taken";
                }
            }
            map.put("error", error);
            map.put("username", user.getUsername());
            map.put("email", user.getEmail());
            return new ModelAndView(map, "register.ftl");
        }, new FreeMarkerEngine());
		/*
		 * Checks if the user is already authenticated
		 */
        before("/register", (req, res) -> {
            User authUser = getAuthenticatedUser(req);
            if(authUser != null) {
                res.redirect("/");
                halt();
            }
        });


		/*
		 * Registers a new message for the user.
		 */
        post("/message", (req, res) -> {
            User user = getAuthenticatedUser(req);
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(req.body(), params, "UTF-8");
            Message m = new Message();
            m.setUserId(user.getId());
            m.setPubDate(new Date());
            BeanUtils.populate(m, params);
            service.addMessage(m);
            res.redirect("/");
            return null;
        });
		/*
		 * Checks if the user is authenticated
		 */
        before("/message", (req, res) -> {
            User authUser = getAuthenticatedUser(req);
            if(authUser == null) {
                res.redirect("/login");
                halt();
            }
        });


		/*
		 * Logs the user out and redirects to the public timeline
		 */
        get("/logout", (req, res) -> {
            removeAuthenticatedUser(req);
            res.redirect("/public");
            return null;
        });
    }

    private void addAuthenticatedUser(Request request, User u) {
        request.session().attribute(USER_SESSION_ID, u);
    }

    private void removeAuthenticatedUser(Request request) {
        request.session().removeAttribute(USER_SESSION_ID);

    }

    private User getAuthenticatedUser(Request request) {
        return request.session().attribute(USER_SESSION_ID);
    }
}