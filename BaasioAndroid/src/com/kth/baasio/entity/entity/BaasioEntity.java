
package com.kth.baasio.entity.entity;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.BaasioConnectableEntity;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.utils.ObjectUtils;

import org.springframework.http.HttpMethod;

public class BaasioEntity extends BaasioConnectableEntity {
    public BaasioEntity() {
        super();
    }

    public BaasioEntity(String type) {
        super(type);
    }

    public BaasioEntity(BaasioConnectableEntity entity) {
        super(entity);
    }

    /**
     * Get entity.
     * 
     * @return Entity
     */
    public BaasioEntity get() throws BaasioException {
        BaasioBaseEntity entity = BaasioBaseEntity.get(getType(), getUniqueKey());
        return entity.toType(BaasioEntity.class);
    }

    /**
     * Get entity. Executes asynchronously in background and the callbacks are
     * called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void getInBackground(final BaasioCallback<BaasioEntity> callback) {
        (new BaasioAsyncTask<BaasioEntity>(callback) {
            @Override
            public BaasioEntity doTask() throws BaasioException {
                return BaasioEntity.this.get();
            }
        }).execute();
    }

    /**
     * Save entity to baas.io.
     * 
     * @return Saved entity
     */
    public BaasioEntity save() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.POST, null, this, getType());

        if (response != null) {
            BaasioEntity entity = response.getFirstEntity().toType(BaasioEntity.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Save entity to baas.io. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void saveInBackground(final BaasioCallback<BaasioEntity> callback) {
        (new BaasioAsyncTask<BaasioEntity>(callback) {
            @Override
            public BaasioEntity doTask() throws BaasioException {
                return save();
            }
        }).execute();
    }

    /**
     * Update entity from baas.io.
     * 
     * @return Updated entity
     */
    public BaasioEntity update() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.PUT, null, this, getType(),
                getUniqueKey());

        if (response != null) {
            BaasioEntity entity = response.getFirstEntity().toType(BaasioEntity.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Update entity from baas.io. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void updateInBackground(final BaasioCallback<BaasioEntity> callback) {
        (new BaasioAsyncTask<BaasioEntity>(callback) {
            @Override
            public BaasioEntity doTask() throws BaasioException {
                return update();
            }
        }).execute();
    }

    /**
     * Delete entity from baas.io.
     * 
     * @return Deleted entity
     */
    public BaasioEntity delete() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, this, getType(),
                getUniqueKey());

        if (response != null) {
            BaasioEntity entity = response.getFirstEntity().toType(BaasioEntity.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Delete entity from baas.io. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void deleteInBackground(final BaasioCallback<BaasioEntity> callback) {
        (new BaasioAsyncTask<BaasioEntity>(callback) {
            @Override
            public BaasioEntity doTask() throws BaasioException {
                return delete();
            }
        }).execute();
    }
}
