
package com.kth.baasio.callback;

import com.kth.baasio.exception.BaasioException;

public interface BaasioCallback<T> {

    public void onResponse(T response);

    public void onException(BaasioException e);
}
