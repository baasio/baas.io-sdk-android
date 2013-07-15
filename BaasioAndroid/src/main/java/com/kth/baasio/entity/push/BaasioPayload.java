
package com.kth.baasio.entity.push;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.MapUtils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaasioPayload {

    private Aps aps = new Aps();

    protected HashMap<String, JsonNode> properties = new HashMap<String, JsonNode>();

    @JsonCreator
    public static BaasioPayload createObject(String jsonString) {
        BaasioPayload payload = JsonUtils.fromJsonString(jsonString, BaasioPayload.class);
        return payload;
    }

    @JsonSerialize(include = NON_NULL)
    public Aps getAps() {
        return aps;
    }

    /**
     * Get badge count.
     * 
     * @return badge count
     */
    @JsonIgnore
    public Integer getBadge() {
        return aps.badge;
    }

    /**
     * Set badge count.
     */
    public void setBadge(Integer badge) {
        aps.badge = badge;
    }

    /**
     * Get sound for iOS APNS.
     * 
     * @return sound
     */
    @JsonIgnore
    public String getSound() {
        return aps.sound;
    }

    /**
     * Set sound for iOS APNS.
     * 
     * @param sound sound
     */
    public void setSound(String sound) {
        aps.sound = sound;
    }

    /**
     * Get push message.
     * 
     * @return push message
     */
    @JsonIgnore
    public String getAlert() {
        return aps.alert;
    }

    /**
     * Set push message.
     * 
     * @param alert push message
     */
    public void setAlert(String alert) {
        aps.alert = alert;
    }

    /**
     * Get predefined property names.
     * 
     * @return List of predefined property names
     */
    @JsonIgnore
    public List<String> getPropertyNames() {
        List<String> properties = new ArrayList<String>();
        return properties;
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
    public void setProperty(String name, double value) {
        JsonUtils.setDoubleProperty(properties, name, value);
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

    public class Aps {
        Integer badge;

        String sound;

        String alert;

        @JsonSerialize(include = NON_NULL)
        public Integer getBadge() {
            return badge;
        }

        public void setBadge(Integer badge) {
            this.badge = badge;
        }

        @JsonSerialize(include = NON_NULL)
        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        @JsonSerialize(include = NON_NULL)
        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        @Override
        public String toString() {
            return JsonUtils.toJsonString(this);
        }
    }
}
