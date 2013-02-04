
package com.kth.baasio.entity.group;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.ObjectUtils;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.HttpMethod;

import java.util.List;

public class BaasioGroup extends BaasioBaseEntity {
    public final static String ENTITY_TYPE = "group";

    public final static String PROPERTY_PATH = "path";

    public final static String PROPERTY_TITLE = "title";

    public BaasioGroup() {
        super();
        setType(ENTITY_TYPE);
    }

    public BaasioGroup(BaasioBaseEntity entity) {
        super(entity);
    }

    /**
     * Get predefined property names.
     * 
     * @return List of predefined property names
     */
    @Override
    @JsonIgnore
    public List<String> getPropertyNames() {
        List<String> properties = super.getPropertyNames();
        properties.add(PROPERTY_PATH);
        properties.add(PROPERTY_TITLE);
        return properties;
    }

    /**
     * Get group path.
     * 
     * @return Group path
     */
    @JsonSerialize(include = NON_NULL)
    public String getPath() {
        return JsonUtils.getStringProperty(properties, PROPERTY_PATH);
    }

    /**
     * Set group path.
     * 
     * @param path Group path
     */
    public void setPath(String path) {
        JsonUtils.setStringProperty(properties, PROPERTY_PATH, path);
    }

    /**
     * Get group title.
     * 
     * @return Group title
     */
    @JsonSerialize(include = NON_NULL)
    public String getTitle() {
        return JsonUtils.getStringProperty(properties, PROPERTY_TITLE);
    }

    /**
     * Set group title.
     * 
     * @param title Group title
     */
    public void setTitle(String title) {
        JsonUtils.setStringProperty(properties, PROPERTY_TITLE, title);
    }

    /**
     * Get group entity.
     * 
     * @return Group entity
     */
    public BaasioGroup get() throws BaasioException {
        BaasioBaseEntity entity = BaasioBaseEntity.get(getType(), getUniqueKey());
        return entity.toType(BaasioGroup.class);
    }

    /**
     * Get group entity. Executes asynchronously in background and the callbacks
     * are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void getInBackground(final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return BaasioGroup.this.get();
            }
        }).execute();
    }

    /**
     * Save group entity to baas.io.
     * 
     * @return Saved group entity
     */
    public BaasioGroup save() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.POST, null, this, getType());

        if (response != null) {
            BaasioGroup entity = response.getFirstEntity().toType(BaasioGroup.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Save group entity to baas.io. Executes asynchronously in background and
     * the callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void saveInBackground(final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return save();
            }
        }).execute();
    }

    /**
     * Update group entity from baas.io.
     * 
     * @return Updated group entity
     */
    public BaasioGroup update() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.PUT, null, this, getType(),
                getUniqueKey());

        if (response != null) {
            BaasioGroup entity = response.getFirstEntity().toType(BaasioGroup.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Update group entity from baas.io. Executes asynchronously in background
     * and the callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void updateInBackground(final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return update();
            }
        }).execute();
    }

    /**
     * Delete group entity from baas.io.
     * 
     * @return Deleted group entity
     */
    public BaasioGroup delete() throws BaasioException {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, this, getType(),
                getUniqueKey());

        if (response != null) {
            BaasioGroup entity = response.getFirstEntity().toType(BaasioGroup.class);
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Delete group entity from baas.io. Executes asynchronously in background
     * and the callbacks are called in the UI thread.
     * 
     * @param callback Result callback
     */
    public void deleteInBackground(final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return delete();
            }
        }).execute();
    }

    /**
     * Add a user to group.
     * 
     * @param user User to add
     * @return Added user
     */
    public BaasioUser add(BaasioUser user) throws BaasioException {
        BaasioResponse response = Baas.io().apiRequest(HttpMethod.POST, null, null, "groups",
                getUniqueKey(), "users", user.getUniqueKey());

        if (response != null) {
            BaasioUser addedEntity = response.getFirstEntity().toType(BaasioUser.class);
            if (!ObjectUtils.isEmpty(addedEntity)) {
                return addedEntity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Add a user to group. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param user User to add
     * @param callback Result callback
     */
    public void addInBackground(final BaasioUser user, final BaasioCallback<BaasioUser> callback) {
        (new BaasioAsyncTask<BaasioUser>(callback) {
            @Override
            public BaasioUser doTask() throws BaasioException {
                return add(user);
            }
        }).execute();
    }

    /**
     * Remove a user from group.
     * 
     * @param user User to remove
     * @return Removed user
     */
    public BaasioUser remove(BaasioUser user) throws BaasioException {

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.DELETE, null, null, "groups",
                getUniqueKey(), "users", user.getUniqueKey());

        if (response != null) {
            BaasioUser removedEntity = response.getFirstEntity().toType(BaasioUser.class);
            if (!ObjectUtils.isEmpty(removedEntity)) {
                return removedEntity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Remove a user from group. Executes asynchronously in background and the
     * callbacks are called in the UI thread.
     * 
     * @param user User to remove
     * @param callback Result callback
     */
    public void removeInBackground(final BaasioUser user, final BaasioCallback<BaasioUser> callback) {
        (new BaasioAsyncTask<BaasioUser>(callback) {
            @Override
            public BaasioUser doTask() throws BaasioException {
                return remove(user);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Connected group entity with class type
     */
    public BaasioGroup connect(String relationship, String targetType, String targetUuid)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.connect(getType(), getUniqueKey(), relationship,
                targetType, targetUuid);
        return entity.toType(BaasioGroup.class);
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
            final String targetUuid, final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return connect(relationship, targetType, targetUuid);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Connected group entity with class type
     */
    public <T extends BaasioBaseEntity> BaasioGroup connect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.connect(getType(), getUniqueKey(), relationship,
                target.getType(), target.getUniqueKey());
        return entity.toType(BaasioGroup.class);
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
            final T target, final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return connect(relationship, target);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Connected group entity with class type
     */
    public BaasioGroup disconnect(String relationship, String targetType, String targetUuid)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.disconnect(getType(), getUniqueKey(),
                relationship, targetType, targetUuid);
        return entity.toType(BaasioGroup.class);
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
    public void disconnectInBackground(final String relationship, final String targetType,
            final String targetUuid, final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return disconnect(relationship, targetType, targetUuid);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @return Connected group entity with class type
     */
    public <T extends BaasioBaseEntity> BaasioGroup disconnect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.disconnect(getType(), getUniqueKey(),
                relationship, target.getType(), target.getUniqueKey());
        return entity.toType(BaasioGroup.class);
    }

    /**
     * Connect to a entity with relationship. Executes asynchronously in
     * background and the callbacks are called in the UI thread.
     * 
     * @param relationship Relationship name
     * @param target Target entity
     * @param callback Result callback
     */
    public <T extends BaasioBaseEntity> void disconnectInBackground(final String relationship,
            final T target, final BaasioCallback<BaasioGroup> callback) {
        (new BaasioAsyncTask<BaasioGroup>(callback) {
            @Override
            public BaasioGroup doTask() throws BaasioException {
                return disconnect(relationship, target);
            }
        }).execute();
    }
}
