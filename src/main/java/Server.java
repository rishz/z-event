import static spark.Spark.get;


public class Server {

    private static final int HTTP_BAD_REQUEST = 400;

    interface Validable {
        boolean isValid();
    }

    public static void main(String[] args) {
        get("/hello", (req,res) -> "Hey all");
    }
}
