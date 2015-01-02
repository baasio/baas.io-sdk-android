
package com.kth.baasio.entity.push;

import com.kth.baasio.entity.BaasioConnectableEntity;
import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.ObjectUtils;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

public class BaasioDevice extends BaasioConnectableEntity {

    public final static String ENTITY_TYPE = "device";

    public final static String PROPERTY_TOKEN = "token";

    public final static String PROPERTY_TAGS = "tags";

    public final static String PROPERTY_PLATFORM = "platform";

    public final static String PLATFORM_TYPE_GCM = "G";

    public final static String PLATFORM_TYPE_IOS = "I";

    public BaasioDevice() {
        super();
        setType(ENTITY_TYPE);
        setPlatform(PLATFORM_TYPE_GCM);
    }

    public BaasioDevice(BaasioConnectableEntity entity) {
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
        properties.add(PROPERTY_TOKEN);
        properties.add(PROPERTY_PLATFORM);
        properties.add(PROPERTY_TAGS);
        return properties;
    }

    /**
     * Get device token(regId).
     * 
     * @return Device token(regId)
     */
    @JsonSerialize(include = NON_NULL)
    public String getToken() {
        return JsonUtils.getStringProperty(properties, PROPERTY_TOKEN);
    }

    /**
     * Set device token(regId).
     * 
     * @param token Device token(regId)
     */
    public void setToken(String token) {
        JsonUtils.setStringProperty(properties, PROPERTY_TOKEN, token);
    }

    /**
     * Get platform type.
     * 
     * @return Platform type. 'G' is android GCM. 'I' is iOS APNS.
     */
    @JsonSerialize(include = NON_NULL)
    public String getPlatform() {
        return JsonUtils.getStringProperty(properties, PROPERTY_PLATFORM);
    }

    /**
     * Set platform type. 'G' is android GCM. 'I' is iOS APNS.
     */
    public void setPlatform(String platform) {
        JsonUtils.setStringProperty(properties, PROPERTY_PLATFORM, platform);
    }

    /**
     * Get tags.
     * 
     * @return List of tags.
     */
    @JsonSerialize(include = NON_NULL)
    public List<String> getTags() {
        List<String> tags = JsonUtils.getStringArrayProperty(properties, PROPERTY_TAGS, List.class);
        if (ObjectUtils.isEmpty(tags)) {
            return null;
        }
        return tags;
    }

    /**
     * Set tags.
     * 
     * @param tags List of tags.
     */
    public void setTags(List<String> tags) {
        JsonUtils.setStringArrayProperty(properties, PROPERTY_TAGS, tags);
    }
}
