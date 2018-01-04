import static spark.Spark.get;

/**
 * Created by rishabhshukla on 05/01/18.
 */

public class Server {
    public static void main(String[] args) {
        get("/hello", (req,res) -> "Hey all");
    }
}
