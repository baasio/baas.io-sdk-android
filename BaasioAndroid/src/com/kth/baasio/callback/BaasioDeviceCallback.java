
package com.kth.baasio.callback;

import com.kth.baasio.entity.push.BaasioDevice;
import com.kth.baasio.exception.BaasioException;

public interface BaasioDeviceCallback {

    public void onResponse(BaasioDevice response);

    public void onException(BaasioException e);
}
