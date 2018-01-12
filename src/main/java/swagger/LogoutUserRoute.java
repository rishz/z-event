package swagger;


import io.swagger.annotations.*;
import model.ApiError;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Api
@Path("/logout")
@Produces("application/json")
public class LogoutUserRoute implements Route {

    @POST
    @ApiOperation(value = "Logs out a user", nickname="LogoutUser")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "model.User", paramType = "body") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response= String.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response= ApiError.class), //
            @ApiResponse(code = 404, message = "User not found", response=ApiError.class) //
    })
    public String handle(@ApiParam(hidden=true) Request request, @ApiParam(hidden=true) Response response) throws Exception {
        return "Logout Successful";
    }

}