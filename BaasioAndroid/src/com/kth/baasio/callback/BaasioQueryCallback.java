
package com.kth.baasio.callback;

import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;

import java.util.List;

public interface BaasioQueryCallback {
    public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query,
            long timestamp);

    public void onException(BaasioException e);

}
