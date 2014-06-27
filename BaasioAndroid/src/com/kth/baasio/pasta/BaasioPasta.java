
package com.kth.baasio.pasta;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.response.BaasioResponse;

import org.springframework.http.HttpMethod;

import java.util.Map;

public class BaasioPasta {
    public static <T extends BaasioResponse> void requestInBackground(final HttpMethod method,
            final Class<T> cls, final Map<String, Object> params, final Object data,
            final BaasioCallback<T> callback, final String... segments) {
        (new BaasioAsyncTask<T>(callback) {
            @Override
            public T doTask() throws BaasioException {
                return request(method, cls, params, data, segments);
            }
        }).execute();
    }

    public static <T extends BaasioResponse> T request(HttpMethod method, Class<T> cls,
            Map<String, Object> params, Object data, String... segments) throws BaasioException {

        T response = Baas.io().pastaApiRequest(method, cls, params, data, segments);

        return response;
    }
}
