
package com.kth.baasio.callback;

import com.kth.baasio.entity.user.BaasioUser;

public interface BaasioSignInCallback extends BaasioCallback<BaasioUser> {

    public void onResponse(BaasioUser response);

}
