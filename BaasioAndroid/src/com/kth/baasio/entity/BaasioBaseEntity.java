
package com.kth.baasio.entity;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioAsyncTask;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioError;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.response.BaasioResponse;
import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.MapUtils;
import com.kth.baasio.utils.ObjectUtils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BaasioBaseEntity {
    public final static String PROPERTY_UUID = "uuid";

    public final static String PROPERTY_TYPE = "type";

    public final static String PROPERTY_NAME = "name";

    public final static String PROPERTY_CREATED = "created";

    public final static String PROPERTY_MODIFIED = "modified";

    public final static String PROPERTY_LOCATION = "location";

    protected HashMap<String, JsonNode> properties = new HashMap<String, JsonNode>();

    public BaasioBaseEntity() {
    }

    public BaasioBaseEntity(String type) {
        setType(type);
    }

    public <T extends BaasioBaseEntity> BaasioBaseEntity(T entity) {
        setType(entity.getType());
        properties = new HashMap<String, JsonNode>(entity.properties);
    }

    /**
     * Get predefined property names.
     * 
     * @return List of predefined property names
     */
    @JsonIgnore
    public List<String> getPropertyNames() {
        List<String> properties = new ArrayList<String>();
        properties.add(PROPERTY_TYPE);
        properties.add(PROPERTY_UUID);
        properties.add(PROPERTY_NAME);
        properties.add(PROPERTY_CREATED);
        properties.add(PROPERTY_MODIFIED);
        properties.add(PROPERTY_LOCATION);
        return properties;
    }

    /**
     * Get entity type.
     * 
     * @return Entity type
     */
    public String getType() {
        return JsonUtils.getStringProperty(properties, PROPERTY_TYPE);
    }

    /**
     * Set entity type.
     * 
     * @param type Entity type
     */
    public void setType(String type) {
        JsonUtils.setStringProperty(properties, PROPERTY_TYPE, type);
    }

    /**
     * Get entity UUID.
     * 
     * @return Entity UUID
     */
    @JsonSerialize(include = NON_NULL)
    public UUID getUuid() {
        return JsonUtils.getUUIDProperty(properties, PROPERTY_UUID);
    }

    /**
     * Set entity UUID.
     * 
     * @param uuid Entity UUID
     */
    public void setUuid(UUID uuid) {
        JsonUtils.setUUIDProperty(properties, PROPERTY_UUID, uuid);
    }

    /**
     * Get entity name.
     * 
     * @return Entity name
     */
    @JsonSerialize(include = NON_NULL)
    public String getName() {
        return JsonUtils.getStringProperty(properties, PROPERTY_NAME);
    }

    /**
     * Set entity name.
     * 
     * @param name Entity name
     */
    public void setName(String name) {
        JsonUtils.setStringProperty(properties, PROPERTY_NAME, name);
    }

    /**
     * Get created time of entity.
     * 
     * @return Created time in milliseconds
     */
    @JsonSerialize(include = NON_NULL)
    public Long getCreated() {
        return JsonUtils.getLongProperty(properties, PROPERTY_CREATED);
    }

    /**
     * Set created time of entity.
     * 
     * @param created Created time in milliseconds
     */
    public void setCreated(Long created) {
        JsonUtils.setLongProperty(properties, PROPERTY_CREATED, created);
    }

    /**
     * Get modified time of entity.
     * 
     * @return Modified time in milliseconds
     */
    @JsonSerialize(include = NON_NULL)
    public Long getModified() {
        return JsonUtils.getLongProperty(properties, PROPERTY_MODIFIED);
    }

    /**
     * Set modified time of entity.
     * 
     * @param modified modified time in milliseconds
     */
    public void setModified(Long modified) {
        JsonUtils.setLongProperty(properties, PROPERTY_MODIFIED, modified);
    }

    /**
     * Get location(latitude, longitude) of entity.
     * 
     * @return location(latitude, longitude)
     */
    @JsonSerialize(include = NON_NULL)
    public BaasioLocation getLocation() {
        return JsonUtils.getObjectProperty(properties, PROPERTY_LOCATION, BaasioLocation.class);
    }

    /**
     * Set location(latitude, longitude) of entity.
     * 
     * @param location location(latitude, longitude)
     */
    public void setLocation(BaasioLocation location) {
        JsonUtils.setObjectProperty(properties, PROPERTY_LOCATION, location);
    }

    /**
     * Get custom properties list of entity.
     * 
     * @return Custom properties list
     */
    @JsonAnyGetter
    public Map<String, JsonNode> getProperties() {
        return MapUtils.newMapWithoutKeys(properties, getPropertyNames());
    }

    @JsonIgnore
    public JsonNode getProperty(String name) {
        return getProperties().get(name);
    }

    /**
     * Set a custom property.
     * 
     * @param name Property name
     * @param value Property value
     */
    @JsonAnySetter
    public void setProperty(String name, JsonNode value) {
        if (value == null) {
            properties.remove(name);
        } else {
            properties.put(name, value);
        }
    }

    /**
     * Set a custom property
     * 
     * @param name Property name
     * @param value Property value
     */
    public void setProperty(String name, String value) {
        JsonUtils.setStringProperty(properties, name, value);
    }

    /**
     * Set a custom property
     * 
     * @param name Property name
     * @param value Property value
     */
    public void setProperty(String name, boolean value) {
        JsonUtils.setBooleanProperty(properties, name, value);
    }

    /**
     * Set a custom property
     * 
     * @param name Property name
     * @param value Property value
     */
    public void setProperty(String name, long value) {
        JsonUtils.setLongProperty(properties, name, value);
    }

    /**
     * Set a custom property
     * 
     * @param name Property name
     * @param value Property value
     */
    public void setProperty(String name, int value) {
        setProperty(name, (long)value);
    }

    /**
     * Set a custom property
     * 
     * @param name Property name
     * @param value Property value
     */
    public void setProperty(String name, float value) {
        JsonUtils.setFloatProperty(properties, name, value);
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

    /**
     * Cast class type to the class type extended from BaasioBaseEntity. The
     * type must be same type.
     * 
     * @param t The class type extended from BaasioBaseEntity
     * @return Casted object
     */
    public <T extends BaasioBaseEntity> T toType(Class<T> t) {
        return toType(this, t);
    }

    /**
     * Cast class type for a object extended to the class type from
     * BaasioBaseEntity. The type must be same type.
     * 
     * @param entity Entity object extended from BaasioBaseEntity
     * @param t The class type extended from BaasioBaseEntity
     * @return Casted object
     */
    public static <T extends BaasioBaseEntity> T toType(BaasioBaseEntity entity, Class<T> t) {
        if (entity == null) {
            return null;
        }
        T newEntity = null;
        try {
            newEntity = (t.newInstance());

            if (newEntity.getType() != null) {
                if (newEntity.getType().equals(entity.getType())) {
                    newEntity.properties = entity.properties;
                } else {
                    throw new Exception(BaasioError.ERROR_ENTITY_TYPE_MISMATCHED);
                }
            } else {
                newEntity.setType(entity.getType());
                newEntity.properties = entity.properties;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newEntity;
    }

    /**
     * Cast class type for object list to the class type extended from
     * BaasioBaseEntity. The type must be same type.
     * 
     * @param entities list of entity class objects extended from
     *            BaasioBaseEntity
     * @param t The class type extended from BaasioBaseEntity
     * @return List of casted objects
     */
    public static <T extends BaasioBaseEntity> List<T> toType(List<BaasioBaseEntity> entities,
            Class<T> t) {
        List<T> l = new ArrayList<T>(entities != null ? entities.size() : 0);
        if (entities != null) {
            for (BaasioBaseEntity entity : entities) {
                T newEntity = entity.toType(t);
                if (newEntity != null) {
                    l.add(newEntity);
                }
            }
        }
        return l;
    }

    /**
     * Get entity.
     * 
     * @param type Entity type
     * @param uuid Entity uuid or name
     * @return Entity
     */
    public static BaasioBaseEntity get(String type, String uuid) throws BaasioException {

        if (ObjectUtils.isEmpty(type)) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        BaasioResponse response = Baas.io().apiRequest(HttpMethod.GET, null, null, type, uuid);

        if (response != null) {
            BaasioBaseEntity entity = response.getFirstEntity();
            if (!ObjectUtils.isEmpty(entity)) {
                return entity;
            }

            throw new BaasioException(BaasioError.ERROR_UNKNOWN_NORESULT_ENTITY);
        }

        throw new BaasioException(BaasioError.ERROR_UNKNOWN_NO_RESPONSE_DATA);
    }

    /**
     * Get entity. Executes asynchronously in background and the callbacks are
     * called in the UI thread.
     * 
     * @param type Entity type
     * @param uuid Entity uuid or name
     * @param callback Result callback
     */
    public static void getInBackground(final String type, final String uuid,
            final BaasioCallback<BaasioBaseEntity> callback) {
        (new BaasioAsyncTask<BaasioBaseEntity>(callback) {
            @Override
            public BaasioBaseEntity doTask() throws BaasioException {
                return BaasioBaseEntity.get(type, uuid);
            }
        }).execute();
    }

    @JsonIgnore
    public String getUniqueKey() {
        if (ObjectUtils.isEmpty(getType())) {
            throw new IllegalArgumentException(BaasioError.ERROR_MISSING_TYPE);
        }

        String uniqueKey;
        if (getType().equals(BaasioUser.ENTITY_TYPE)) {
            BaasioUser userEntity = toType(BaasioUser.class);
            if (ObjectUtils.isEmpty(getUuid()) && ObjectUtils.isEmpty(userEntity.getUsername())) {
                throw new IllegalArgumentException(BaasioError.ERROR_MISSING_USER_UUID_OR_USERNAME);
            }

            if (!ObjectUtils.isEmpty(getUuid())) {
                uniqueKey = userEntity.getUuid().toString();
            } else {
                uniqueKey = userEntity.getUsername();
            }
        } else if (getType().equals(BaasioGroup.ENTITY_TYPE)) {
            BaasioGroup groupEntity = toType(BaasioGroup.class);
            if (ObjectUtils.isEmpty(getUuid()) && ObjectUtils.isEmpty(groupEntity.getPath())) {
                throw new IllegalArgumentException(BaasioError.ERROR_MISSING_GROUP_UUID_OR_PATH);
            }

            if (!ObjectUtils.isEmpty(getUuid())) {
                uniqueKey = groupEntity.getUuid().toString();
            } else {
                uniqueKey = groupEntity.getPath();
            }
        } else {
            if (ObjectUtils.isEmpty(getUuid()) && ObjectUtils.isEmpty(getName())) {
                throw new IllegalArgumentException(BaasioError.ERROR_MISSING_UUID_OR_NAME);
            }

            if (!ObjectUtils.isEmpty(getUuid())) {
                uniqueKey = getUuid().toString();
            } else {
                uniqueKey = getName();
            }
        }

        return uniqueKey;
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
                return BaasioBaseEntity.connect(sourceType, sourceUuid, relationship, targetType,
                        targetUuid);
            }
        }).execute();
    }

    /**
     * Connect to a entity with relationship
     * 
     * @param relationship Relationship name
     * @param targetType Target entity type
     * @param targetUuid Target entity uuid or name
     * @return Connected entity with class type
     */
    public BaasioBaseEntity connect(String relationship, String targetType, String targetUuid)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.connect(getType(), getUniqueKey(), relationship,
                targetType, targetUuid);
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
     * @param target Target entity
     * @return Connected entity with class type
     */
    public <T extends BaasioBaseEntity> BaasioBaseEntity connect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.connect(getType(), getUniqueKey(), relationship,
                target.getType(), target.getUniqueKey());
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

        BaasioBaseEntity entity = BaasioBaseEntity.disconnect(getType(), getUniqueKey(),
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
     * @param target Target entity
     * @return Disconnected entity with class type
     */
    public <T extends BaasioBaseEntity> BaasioBaseEntity disconnect(String relationship, T target)
            throws BaasioException {

        BaasioBaseEntity entity = BaasioBaseEntity.disconnect(getType(), getUniqueKey(),
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
}
