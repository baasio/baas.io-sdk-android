
package com.kth.baasio.entity;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.utils.ObjectUtils;

import org.springframework.http.HttpMethod;

public class BaasioConnectableEntity extends BaasioBaseEntity {

    public BaasioConnectableEntity() {
    }

    public BaasioConnectableEntity(String type) {
        super(type);
    }

    public <T extends BaasioConnectableEntity> BaasioConnectableEntity(T entity) {
        super(entity);
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Connected entity
     */
    public BaasioBaseEntity connect(String relationship, String targetType, String targetUuid)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.connect(getType(), getUniqueKey(),
                relationship, targetType, targetUuid);
        return entity;
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public void connectInBackground(final String relationship, final String targetType,
            final String targetUuid, final BaasioCallback<BaasioBaseEntity> callback) {
        (new BaasioAsyncTask<BaasioBaseEntity>(callback) {
            @Override
            public BaasioBaseEntity doTask() throws BaasioException {
                return connect(relationship, targetType, targetUuid);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param t Result class type
     * @return Connected entity
     */
    public <T extends BaasioBaseEntity> T connect(String relationship, String targetType,
            String targetUuid, Class<T> t) throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.connect(getType(), getUniqueKey(),
                relationship, targetType, targetUuid);

        return entity.toType(t);
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void connectInBackground(final String relationship,
            final String targetType, final String targetUuid, final Class<T> t,
            final BaasioCallback<T> callback) {
        (new BaasioAsyncTask<T>(callback) {
            @Override
            public T doTask() throws BaasioException {
                return connect(relationship, targetType, targetUuid, t);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Connected entity
     */
    public <T extends BaasioBaseEntity> BaasioBaseEntity connect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.connect(getType(), getUniqueKey(),
                relationship, target.getType(), target.getUniqueKey());

        return entity;
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void connectInBackground(final String relationship,
            final T target, final BaasioCallback<BaasioBaseEntity> callback) {
        (new BaasioAsyncTask<BaasioBaseEntity>(callback) {
            @Override
            public BaasioBaseEntity doTask() throws BaasioException {
                return connect(relationship, target);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Connected entity
     */
    public <T extends BaasioBaseEntity> T connect(String relationship, T target, Class<T> t)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.connect(getType(), getUniqueKey(),
                relationship, target.getType(), target.getUniqueKey());

        return entity.toType(t);
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void connectInBackground(final String relationship,
            final T target, final Class<T> t, final BaasioCallback<T> callback) {
        (new BaasioAsyncTask<T>(callback) {
            @Override
            public T doTask() throws BaasioException {
                return connect(relationship, target, t);
            }
        }).execute();
    }

    /**
     * Disconnect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Disconnected entity with class type
     */
    public BaasioBaseEntity disconnect(String relationship, String targetType, String targetUuid)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.disconnect(getType(), getUniqueKey(),
                relationship, targetType, targetUuid);
        return entity;
    }

    /**
     * Disconnect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public void disconnectInBackground(final String relationship, final String targetType,
            final String targetUuid, final BaasioCallback<BaasioBaseEntity> callback) {
        (new BaasioAsyncTask<BaasioBaseEntity>(callback) {
            @Override
            public BaasioBaseEntity doTask() throws BaasioException {
                return disconnect(relationship, targetType, targetUuid);
            }
        }).execute();
    }

    /**
     * Disconnect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param t Result class type
     * @return Disconnected entity with class type
     */
    public <T extends BaasioBaseEntity> T disconnect(String relationship, String targetType,
            String targetUuid, Class<T> t) throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.disconnect(getType(), getUniqueKey(),
                relationship, targetType, targetUuid);
        return entity.toType(t);
    }

    /**
     * Disconnect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void disconnectInBackground(final String relationship,
            final String targetType, final String targetUuid, final Class<T> t,
            final BaasioCallback<T> callback) {
        (new BaasioAsyncTask<T>(callback) {
            @Override
            public T doTask() throws BaasioException {
                return disconnect(relationship, targetType, targetUuid, t);
            }
        }).execute();
    }

    /**
     * Disconnect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Disconnected entity with class type
     */
    public <T extends BaasioBaseEntity> BaasioBaseEntity disconnect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.disconnect(getType(), getUniqueKey(),
                relationship, target.getType(), target.getUniqueKey());
        return entity;
    }

    /**
     * Disconnect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void disconnectInBackground(final String relationship,
            final T target, final BaasioCallback<BaasioBaseEntity> callback) {
        (new BaasioAsyncTask<BaasioBaseEntity>(callback) {
            @Override
            public BaasioBaseEntity doTask() throws BaasioException {
                return disconnect(relationship, target);
            }
        }).execute();
    }

    /**
     * Disconnect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Disconnected entity with class type
     */
    public <T extends BaasioBaseEntity> T disconnect(String relationship, T target, Class<T> t)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioConnectableEntity.disconnect(getType(), getUniqueKey(),
                relationship, target.getType(), target.getUniqueKey());
        return entity.toType(t);
    }

    /**
     * Disconnect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void disconnectInBackground(final String relationship,
            final T target, final Class<T> t, final BaasioCallback<T> callback) {
        (new BaasioAsyncTask<T>(callback) {
            @Override
            public T doTask() throws BaasioException {
                return disconnect(relationship, target, t);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param sourceType Source entity type
     * @param sourceUuid Source entity uuid or name
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Connected entity
     */
    public static <T extends BaasioBaseEntity> BaasioBaseEntity connect(String sourceType,
            String sourceUuid, String relationship, String targetType, String targetUuid)
            throws BaasioException {
        if (ObjectUtils.isEmpty(sourceType)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(sourceUuid)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_UUID);
        }

        if (ObjectUtils.isEmpty(targetType)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(targetUuid)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_UUID);
        }

        if (ObjectUtils.isEmpty(relationship)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_RELATIONSHIP);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.POST, null, null, sourceType,
                sourceUuid, relationship, targetType, targetUuid);

        if (response != null) {
            BaasioBaseEntity connectedTagetEntity = response.getFirstEntity();
            if (!ObjectUtils.isEmpty(connectedTagetEntity)) {
                return connectedTagetEntity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param sourceType Source entity type
     * @param sourceUuid Source entity uuid or name
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public static void connectInBackground(final String sourceType, final String sourceUuid,
            final String relationship, final String targetType, final String targetUuid,
            final BaasioCallback<BaasioBaseEntity> callback) {
        (new BaasioAsyncTask<BaasioBaseEntity>(callback) {
            @Override
            public BaasioBaseEntity doTask() throws BaasioException {
                return BaasioConnectableEntity.connect(sourceType, sourceUuid, relationship,
                        targetType, targetUuid);
            }
        }).execute();
    }

    /**
     * Disconnect to a entity with relationship
     * 
     * @param sourceType Source entity type
     * @param sourceUuid Source entity uuid or name
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Disconnected entity
     */
    public static <T extends BaasioBaseEntity> BaasioBaseEntity disconnect(String sourceType,
            String sourceUuid, String relationship, String targetType, String targetUuid)
            throws BaasioException {
        if (ObjectUtils.isEmpty(sourceType)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(sourceUuid)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_UUID);
        }

        if (ObjectUtils.isEmpty(targetType)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(targetUuid)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        if (ObjectUtils.isEmpty(relationship)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_RELATIONSHIP);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, null, sourceType,
                sourceUuid, relationship, targetType, targetUuid);

        if (response != null) {
            BaasioBaseEntity result = response.getFirstEntity();
            if (!ObjectUtils.isEmpty(result)) {
                return result;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Disconnect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param sourceType Source entity type
     * @param sourceUuid Source entity uuid or name
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @param callback Result callback
     */
    public static <T extends BaasioBaseEntity> void disconnectInBackground(final String sourceType,
            final String sourceUuid, final String relationship, final String targetType,
            final String targetUuid, final BaasioCallback<BaasioBaseEntity> callback) {
        (new BaasioAsyncTask<BaasioBaseEntity>(callback) {
            @Override
            public BaasioBaseEntity doTask() throws BaasioException {
                return disconnect(sourceType, sourceUuid, relationship, targetType, targetUuid);
            }
        }).execute();
    }
}
