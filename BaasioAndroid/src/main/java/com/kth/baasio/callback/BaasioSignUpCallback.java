
package com.kth.baasio.callback;

import com.kth.baasio.entity.user.BaasioUser;

public interface BaasioSignUpCallback extends BaasioCallback<BaasioUser> {

    public void onResponse(BaasioUser response);

}
