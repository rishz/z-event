package config;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import model.Event;
import model.LoginResult;
import model.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import service.AuthService;
import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;
import spark.utils.StringUtils;
import utils.SwaggerParser;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static spark.Spark.*;


@SwaggerDefinition(host = "localhost:4567", //
        info = @Info(description = "Event-Manager API", //
                version = "V1.0", //
                title = "Event-Manager", //
                contact = @Contact(name = "abc") ) , //
        schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS }, //
        consumes = { "application/json" }, //
        produces = { "application/json" }, //
        tags = { @Tag(name = "swagger") })

public class WebConfig {

    private static final String USER_SESSION_ID = "user";
    private static final String EVENT_ID = "event";
    private static final Logger logger = Logger.getLogger(WebConfig.class.getCanonicalName());
    public static final String APP_PACKAGE = "";

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
            map.put("pageTitle", "Events");
            map.put("user", user);
            List<Event> events = service.getPublicEvents();
            if(req.queryParams("r") != null) {
                map.put("message", "You have successfully created an event");
            }
            map.put("events", events);
            return new ModelAndView(map, "events.ftl");
        }, new FreeMarkerEngine());

        before("/", (req, res) -> {
            User user = getAuthenticatedUser(req);
            if(user == null) {
                res.redirect("/login");
                halt();
            }
        });

        get("/events", (req, res) -> {
            User user = getAuthenticatedUser(req);
            Map<String, Object> map = new HashMap<>();
            map.put("pageTitle", "Events");
            map.put("user", user);
            List<Event> events = service.getPublicEvents();
            map.put("events", events);
            if(req.queryParams("r") != null) {
                map.put("message", "You have successfully created an event");
            }
            return new ModelAndView(map, "events.ftl");
        }, new FreeMarkerEngine());

        put("/:event_uuid", (req,res) -> {
            User user = getAuthenticatedUser(req);
            MultiMap<String> params = new MultiMap<>();

            Event e = new Event();
            e.setOrganizer(user.getUsername());
            e.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(req.params("date")));
            e.setDescription(req.params("description"));
            e.setCategories(Arrays.asList(req.params("categories").split(",")));
            BeanUtils.populate(e, params);
            service.addEvent(e);
            Map<String, Object> map = new HashMap<>();
            map.put("pageTitle", "Event Updated");
            map.put("event", e);
            return new ModelAndView(map, "events.ftl");
        });

        delete("/:event_uuid", (req,res) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("pageTitle", "Events");
            map.put("message", "Event deleted successfully");
            return new ModelAndView(map, "events.ftl");
        });

        get("/t/:username", (req, res) -> {
            String username = req.params(":username");
            User profileUser = service.getUserbyUsername(username);

            User authUser = getAuthenticatedUser(req);

            List<Event> events = service.getUserEvents(profileUser);

            Map<String, Object> map = new HashMap<>();
            map.put("pageTitle", username + "'s events");
            map.put("user", authUser);
            map.put("profileUser", profileUser);
            map.put("events", events);
            return new ModelAndView(map, "events.ftl");
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

        post("/event", (req, res) -> {
            User user = getAuthenticatedUser(req);

            Event e = new Event();
            try {
                MultiMap<String> params = new MultiMap<>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");

                DateTimeConverter dtConverter = new DateConverter();
                dtConverter.setPattern("yyyy-MM-dd");
                ConvertUtils.register(dtConverter, Date.class);

                BeanUtils.populate(e, params);
            } catch (Exception ex) {
                ex.printStackTrace();
                halt(501);
                return null;
            }
            e.setOrganizer(user.getUsername());
//            BeanUtils.populate(e, params);
            service.addEvent(e);
            res.redirect("/events?r=1");
            return null;
        });

		/*
		 * Checks if the user is authenticated
		 */
        before("/event", (req, res) -> {
            User authUser = getAuthenticatedUser(req);
            if(authUser == null) {
                res.redirect("/login");
                halt();
            }
        });

		/*
		 * Presents the login form or redirect the user to
		 * her events if it's already logged in
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
                MultiMap<String> params = new MultiMap<>();
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
		 * her events if it's already logged in
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
                MultiMap<String> params = new MultiMap<>();
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
		 * Logs the user out and redirects to the public events
		 */
        get("/logout", (req, res) -> {
            removeAuthenticatedUser(req);
            res.redirect("/login");
            return null;
        });

        try {
            // Build swagger json description
            final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
            get("/swagger", (req, res) -> swaggerJson);

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
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