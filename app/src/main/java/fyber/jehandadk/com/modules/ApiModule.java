package fyber.jehandadk.com.modules;

import java.io.IOException;
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
import fyber.jehandadk.com.data.models.OfferListResponse;
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
public class ApiModule {

    public ApiModule() {
    }

    @Provides
    @Singleton
    IFyberClient provideFyberService(Retrofit retrofit) {
        return retrofit.create(IFyberClient.class);
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
        logger.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BASIC);
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
        return api.getIp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(responseBody -> {
                    try {
                        return responseBody.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .cache(1);
    }


}
