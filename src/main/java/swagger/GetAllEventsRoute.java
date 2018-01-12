package swagger;

import io.swagger.annotations.*;
import model.ApiError;
import model.Event;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Api
@Path("/events")
@Produces("application/json")
public class GetAllEventsRoute implements Route {

    @GET
    @ApiOperation(value = "Gets all events", nickname="GetEvents")
    @ApiImplicitParams({ //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response= Event[].class), //
            @ApiResponse(code = 400, message = "Invalid input data", response= ApiError.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response=ApiError.class), //
    })
    public List<Event> handle(@ApiParam(hidden=true) Request request, @ApiParam(hidden=true) Response response) throws Exception {
        return new ArrayList<>();
    }

}