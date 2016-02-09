package fyber.jehandadk.com.data.errors;

/**
 * Created by jehandad.kamal on 2/7/2016.
 */
public class InvalidFyberHash extends Error {

    public InvalidFyberHash() {
        super("Invalid hash returned, response is ...");
    }
}
