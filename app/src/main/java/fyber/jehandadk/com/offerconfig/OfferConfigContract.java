package fyber.jehandadk.com.offerconfig;

/**
 * Created by jehandad.kamal on 2/7/2016.
 */
public interface OfferConfigContract {

    interface Views {

        void loadDefaultValues();

    }

    interface UserActions {
        void saveApiParams();
    }
}
