package fyber.jehandadk.com.modules;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.devland.esperandro.Esperandro;
import fyber.jehandadk.com.App;
import fyber.jehandadk.com.prefs.IFyberPrefs;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class AppModule {

    App mApplication;

    public AppModule(App application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    App providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    IFyberPrefs providesMainPrefs(Context ctx) {
        return Esperandro.getPreferences(IFyberPrefs.class, ctx);
    }

    @Singleton
    @Provides
    @Named("google_ad_id")
    Observable<String> provideGoogleAdvertisingId(Context ctx) {
        return Observable
                .fromCallable(() -> AdvertisingIdClient.getAdvertisingIdInfo(ctx).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache(1)
                .timeout(5, TimeUnit.SECONDS);
    }
}