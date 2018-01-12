package swagger;


import io.swagger.annotations.*;
import model.ApiError;
import model.Event;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Api
@Path("/event")
@Produces("application/json")
public class DeletesEventRoute implements Route {

    @DELETE
    @ApiOperation(value = "Deletes an event", nickname="Delete Event")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "model.Event", paramType = "body") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response= Event.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response=ApiError.class), //
            @ApiResponse(code = 404, message = "Event not found", response=ApiError.class) //
    })
    public Event handle(@ApiParam(hidden=true) Request request, @ApiParam(hidden=true) Response response) throws Exception {
        return new Event();
    }

}