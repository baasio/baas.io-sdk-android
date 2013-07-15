
package com.kth.baasio.callback;

import com.kth.baasio.response.BaasioResponse;

public interface BaasioResponseCallback extends BaasioCallback<BaasioResponse> {

    public void onResponse(BaasioResponse response);

}
