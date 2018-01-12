package swagger;

import io.swagger.annotations.*;
import model.ApiError;
import model.User;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Api
@Path("/register")
@Produces("application/json")
public class CreateUserRoute implements Route {

    @POST
    @ApiOperation(value = "Creates a new user", nickname="CreateUser")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "model.User", paramType = "body") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response= User.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response= ApiError.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response=ApiError.class), //
            @ApiResponse(code = 404, message = "User not found", response=ApiError.class) //
    })
    public User handle(@ApiParam(hidden=true) Request request, @ApiParam(hidden=true) Response response) throws Exception {
        return new User();
    }

}