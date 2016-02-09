package fyber.jehandadk.com.api;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fyber.jehandadk.com.data.errors.InvalidFyberHash;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by jehandad.kamal on 2/7/2016.
 */
public class HashVerifierInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        List<String> queries = new ArrayList<>(request.url().querySize());
        for (String queryKey : request.url().queryParameterNames()) {
            // Would love to use streams api from java 8
            queries.add(Joiner.on("=").join(queryKey, Joiner.on(",").join(request.url().queryParameterValues(queryKey))));
        }
        Collections.sort(queries);
        String apiKey = request.header(IFyberClient.HEADER_API_KEY);
        String queryParamsString = Joiner.on("&").join(queries) + "&" + apiKey;
        String hashKey = String.valueOf(Hex.encodeHex(DigestUtils.sha1(queryParamsString)));
        HttpUrl.Builder urlBuilder = request.url().newBuilder().addQueryParameter(IFyberClient.QUERY_HASH_KEY, hashKey);
        request = request.newBuilder().url(urlBuilder.build()).removeHeader(IFyberClient.HEADER_API_KEY).build();
        Response response = chain.proceed(request);
        String expectedHash = response.header(IFyberClient.HASH_NAME);
        if (Strings.isNullOrEmpty(expectedHash)) {
            // No hash provided, no need to verify
            // Maybe we should just fail?
            // throw new InvalidFyberHash();
            return response;
        }

        byte[] bytes = response.body().bytes();
        String toHash = StringUtils.newStringUtf8(bytes) + apiKey;
        String hash = new String(Hex.encodeHex(DigestUtils.sha1(toHash)));

        if (!hash.equals(expectedHash)) throw new InvalidFyberHash();

        return response.newBuilder()
                .body(ResponseBody.create(
                        response.body().contentType(), bytes))
                .build();
    }

}
