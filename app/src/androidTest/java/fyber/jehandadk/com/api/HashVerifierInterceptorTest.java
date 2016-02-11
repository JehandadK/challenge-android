package fyber.jehandadk.com.api;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import fyber.jehandadk.com.BuildConfig;
import fyber.jehandadk.com.data.errors.InvalidFyberHash;
import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jehandad.kamal on 2/12/2016.
 */
//@RunWith(MockitoJUnitRunner.class)
public class HashVerifierInterceptorTest {

    public static final String RESPONSE = "{\"code\":\" OK\",\"message\":\"OK\",\"count\":1,\"pages\":1,\"information\":{\"app_name\":\"SP Test App\",\"appid\":157,\"virtual_currency\":\"Coins\",\"country\":\" US\",\"language\":\" EN\",\"support_url\":\"http://iframe.fyber.com/mobile/DE/157/my_offers\"},\"offers\":[{\"title\":\"Tap  Fish\",\"offer_id\":13554,\"teaser\":\"Download and START\",\"required_actions\":\"Download and START\",\"link\":\"http://iframe.fyber.com/mbrowser?appid=157&lpid=11387&uid=player1\",\"offer_types\":[{\"offer_type_id\":101,\"readable\":\"Download\"},{\"offer_type_id\":112,\"readable\":\"Free\"}],\"thumbnail\":{\"lowres\":\"http://cdn.fyber.com/assets/1808/icon175x175-2_square_60.png\",\"hires\":\"http://cdn.fyber.com/assets/1808/icon175x175-2_square_175.png\"},\"payout\":90,\"time_to_payout\":{\"amount\":1800,\"readable\":\"30 minutes\"}}]}";
    @Mock
    Interceptor.Chain chain;

    //    @Mock
//    Request request;

    //    @Mock
//    Response response;


    @Captor
    private ArgumentCaptor<Request> captor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test()
    public void testIntercept() throws Exception {
        HashVerifierInterceptor interceptor = new HashVerifierInterceptor();


        HttpUrl url = getTestHttpUrl()
                .build();

        Request request = new Request.Builder()
                .header(IFyberClient.HEADER_API_KEY, "e95a21621a1865bcbae3bee89c4d4f84")
                .url(url)
                .build();
        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), RESPONSE);
        Response response = getResponseBuidler(request, body)
                .addHeader(IFyberClient.HASH_NAME, "46EF0F4AC1946DCCD2033E309C890C589E089328".toLowerCase())
                .build();
        when(chain.request()).then(invocation -> request);
        when(chain.proceed(Matchers.any())).then(invocation -> response);

        interceptor.intercept(chain);

        verify(chain).proceed(captor.capture());

        assertThat(captor.getValue().url().queryParameter(IFyberClient.QUERY_HASH_KEY),
                equalTo("7a2b1604c03d46eec1ecd4a686787b75dd693c4d"));
    }


    @Test(expected = InvalidFyberHash.class)
    public void testInvalidHashErrorThrown() throws Exception {
        HashVerifierInterceptor interceptor = new HashVerifierInterceptor();
        HttpUrl url = getTestHttpUrl()
                .build();
        Request request = new Request.Builder()
                .header(IFyberClient.HEADER_API_KEY, "e95a21621a1865bcbae3bee89c4d4f84")
                .url(url)
                .build();
        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), RESPONSE);
        Response response = getResponseBuidler(request, body)
                .addHeader(IFyberClient.HASH_NAME, "Invalid Hash Key")
                .build();
        when(chain.request()).then(invocation -> request);
        when(chain.proceed(Matchers.any())).then(invocation -> response);

        interceptor.intercept(chain);
        verify(chain).proceed(captor.capture());
        assertThat(captor.getValue().url().queryParameter(IFyberClient.QUERY_HASH_KEY),
                equalTo("7a2b1604c03d46eec1ecd4a686787b75dd693c4d"));
    }

    private Response.Builder getResponseBuidler(Request request, ResponseBody body) {
        return new Response.Builder()
                .request(request)
                .body(body)
                .protocol(Protocol.HTTP_1_1)
                .code(200);
    }

    @NonNull
    private HttpUrl.Builder getTestHttpUrl() {
        return new HttpUrl.Builder()
                .addQueryParameter("appid", "157")
                .addQueryParameter("uid", "player1")
                .addQueryParameter("ip", "212.45.111.17")
                .addQueryParameter("locale", "de")
                .addQueryParameter("device_id", "2b6f0cc904d137be2e1730235f5664094b831186")
                .addQueryParameter("ps_time", "1312211903")
                .addQueryParameter("pub0", "campaign2")
                .addQueryParameter("page", "2")
                .addQueryParameter("timestamp", "1312553361")
                .host("api.fyber.com")
                .scheme("http");
    }

}