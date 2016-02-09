package fyber.jehandadk.com.modules;

import javax.inject.Singleton;

import dagger.Component;
import fyber.jehandadk.com.offerconfig.ConfigActivity;
import fyber.jehandadk.com.offerconfig.ConfigFragment;
import fyber.jehandadk.com.offers.OffersFragment;

@Singleton
@Component(
        modules = {AppModule.class, ApiModule.class}
)
public interface MainComponent {
    void inject(ConfigActivity activity);

    void inject(ConfigFragment fragment);

    void inject(OffersFragment offersFragment);
}