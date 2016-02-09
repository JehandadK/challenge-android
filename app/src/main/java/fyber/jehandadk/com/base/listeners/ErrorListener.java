package fyber.jehandadk.com.base.listeners;


import fyber.jehandadk.com.data.errors.Err;

/**
 * Created by jehandad.kamal on 1/25/2016.
 */
public interface ErrorListener {

    void handleError(Throwable t);

    void handleError(Err err);

    void handleError(Error error);
}
