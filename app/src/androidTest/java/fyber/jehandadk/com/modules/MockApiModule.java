package fyber.jehandadk.com.modules;

import org.mockito.Mockito;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.NoSuchElementException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fyber.jehandadk.com.BuildConfig;
import fyber.jehandadk.com.api.HashVerifierInterceptor;
import fyber.jehandadk.com.api.IFyberClient;
import fyber.jehandadk.com.api.OffersRequestBuilder;
import fyber.jehandadk.com.data.models.ApiConfig;
import fyber.jehandadk.com.data.models.OfferListResponse;
import fyber.jehandadk.com.prefs.IFyberPrefs;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jehandad.kamal on 1/24/2016.
 */
@Module
public class MockApiModule {

    public MockApiModule() {
    }

    @Provides
    @Singleton
    IFyberClient provideFyberService(Retrofit retrofit) {
        return Mockito.mock(IFyberClient.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.ENDPOINT)
                .build();
    }

    @Provides
    OkHttpClient provideClient(Interceptor logger) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new HashVerifierInterceptor());
        builder.interceptors().add(logger);
        return builder.build();
    }

    @Provides
    Interceptor provideLogger() {
        // Just some stupid issue with my device
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(message -> System.out.println(message));
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logger;
    }

    @Provides
    @Named("ip")
    Observable<String> provideLocalIp() {
        try {
            return Observable.from(Collections.list(NetworkInterface.getNetworkInterfaces()))
                    .flatMapIterable(networkInterface -> Collections.list(networkInterface.getInetAddresses()))
                    .filter(inetAddress -> !inetAddress.isLoopbackAddress())
                    .map(InetAddress::getHostAddress)
                    .filter(s -> !s.contains(":"))
                    .first();
        } catch (SocketException | NoSuchElementException e) {
            e.printStackTrace();
            return Observable.just(null);
        }
    }

    @Singleton
    @Provides
    @Named("external_ip")
    Observable<String> provideExternalIp(IFyberClient api) {
        return Observable.just("192.168.1.1");
    }

    @Named("offer_list")
    @Singleton
    @Provides
    Observable<OfferListResponse> provideOffersList(IFyberClient api, @Named("external_ip") Observable<String> ip, @Named("google_ad_id") Observable<String> adId, IFyberPrefs prefs) {
        OffersRequestBuilder builder = OffersRequestBuilder.newBuilder();
        ApiConfig config = prefs.apiConfig();
        builder.apiKey(config.getApiKey())
                .appId(config.getAppId())
                .offerTypes("112").pub0(config.getPub0())
                .uid(config.getUid())
                .page(1)
                .limitedTrackingEnabled(true);
        return ip.zipWith(adId, (ipStr, adIdStr) -> builder.ip(ipStr).googleAdId(adIdStr).build(api))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .toBlocking().first()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache(1);


    }

}
