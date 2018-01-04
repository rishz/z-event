import static spark.Spark.get;

/**
 * Created by rishabhshukla on 05/01/18.
 */

public class Server {

    private static final int HTTP_BAD_REQUEST = 400;

    interface Validable {
        boolean isValid();
    }

    public static void main(String[] args) {
        get("/hello", (req,res) -> "Hey all");
    }
}
