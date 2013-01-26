
package com.kth.baasio.entity.push;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaasioMessage extends BaasioBaseEntity {
    public final static String ENTITY_TYPE = "push";

    public final static String TARGET_TYPE_ALL = "all";

    public final static String TARGET_TYPE_TAG = "tag";

    public final static String TARGET_TYPE_DEVICE = "device";

    public final static String TARGET_TYPE_USER = "user";

    public final static String PLATFORM_TYPE_GCM = "G";

    public final static String PLATFORM_TYPE_IOS = "I";

    public final static int PLATFORM_FLAG_TYPE_GCM = 0x01;

    public final static int PLATFORM_FLAG_TYPE_IOS = 0x02;

    public final static String PROPERTY_TARGET = "target";

    public final static String PROPERTY_TO = "to";

    public final static String PROPERTY_PAYLOAD = "payload";

    public final static String PROPERTY_RESERVE = "reserve";

    public final static String PROPERTY_PLATFORM = "platform";

    public final static String PROPERTY_MEMO = "memo";

    public BaasioMessage() {
        super();
        setType(ENTITY_TYPE);
        setTarget(TARGET_TYPE_ALL);
        setPlatform(PLATFORM_FLAG_TYPE_GCM | PLATFORM_FLAG_TYPE_IOS);
    }

    public BaasioMessage(BaasioBaseEntity entity) {
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
        properties.add(PROPERTY_TARGET);
        properties.add(PROPERTY_TO);
        properties.add(PROPERTY_PAYLOAD);
        properties.add(PROPERTY_RESERVE);
        properties.add(PROPERTY_PLATFORM);
        properties.add(PROPERTY_MEMO);
        return properties;
    }

    /**
     * Get target type.
     * 
     * @return Target type
     */
    @JsonSerialize(include = NON_NULL)
    public String getTarget() {
        return JsonUtils.getStringProperty(properties, PROPERTY_TARGET);
    }

    /**
     * Set target type.
     * 
     * @param target Target type
     */
    public void setTarget(String target) {
        JsonUtils.setStringProperty(properties, PROPERTY_TARGET, target);
    }

    /**
     * Get target string.
     * 
     * @return Target string
     */
    @JsonSerialize(include = NON_NULL)
    public String getTo() {
        return JsonUtils.getStringProperty(properties, PROPERTY_TO);
    }

    /**
     * Get list of target.
     * 
     * @return List of target
     */
    @JsonIgnore
    public String[] getToList() {
        String to = getTo();
        return to.split(",\\");
    }

    /**
     * Set target.
     * 
     * @param toList List of target
     */
    public void setTo(String... toList) {
        if (toList == null || toList.length <= 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (String to : toList) {
            if (builder.length() > 0) {
                builder.append(",");
            }

            builder.append(to);
        }

        JsonUtils.setStringProperty(properties, PROPERTY_TO, builder.toString());
    }

    /**
     * Get payload to send.
     * 
     * @return payload
     */
    @JsonSerialize(include = NON_NULL)
    public Payload getPayload() {
        return JsonUtils.getObjectProperty(properties, PROPERTY_PAYLOAD, Payload.class);
    }

    /**
     * Set payload to send.
     * 
     * @param payload Payload to send
     */
    public void setPayload(Payload payload) {
        JsonUtils.setObjectProperty(properties, PROPERTY_PAYLOAD, payload);
    }

    /**
     * Set payload to send.
     * 
     * @param alert Message
     * @param sound Sound
     * @param badge badge count
     */
    public void setMessage(String alert, String sound, Integer badge) {
        Payload payload = new Payload();
        payload.setAlert(alert);
        payload.setSound(sound);
        payload.setBadge(badge);

        JsonUtils.setObjectProperty(properties, PROPERTY_PAYLOAD, payload);
    }

    /**
     * Get time string to send.
     * 
     * @return Time string(yyyyMMddHHmm) in KST
     */
    @JsonSerialize(include = NON_NULL)
    public String getReserve() {
        return JsonUtils.getStringProperty(properties, PROPERTY_RESERVE);
    }

    /**
     * Set time string to send.
     * 
     * @param reserve Time string(yyyyMMddHHmm) in KST
     */
    public void setReserve(String reserve) {
        if (reserve.length() < 12) {
            return;
        }

        JsonUtils.setStringProperty(properties, PROPERTY_RESERVE, reserve);
    }

    private static String getReserveTimeString(long millis) {
        String time = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        time = formatter.format(new Date(millis));

        return time;
    }

    /**
     * Set time to send in UNIX time(milliseconds).
     * 
     * @param millis UNIX time(milliseconds)
     */
    public void setReserve(long millis) {
        String reserve = getReserveTimeString(millis);

        JsonUtils.setStringProperty(properties, PROPERTY_RESERVE, reserve);
    }

    /**
     * Get target platform type. 'G' is android GCM. 'I' is iOS APNS.
     * 
     * @return Target platform type
     */
    @JsonSerialize(include = NON_NULL)
    public String getPlatform() {
        return JsonUtils.getStringProperty(properties, PROPERTY_PLATFORM);
    }

    /**
     * To check, a platform type is included.
     * 
     * @return true is included
     */
    @JsonIgnore
    public boolean isPlatformIncluded(int flag) {
        String platform = getPlatform();
        if (flag == (PLATFORM_FLAG_TYPE_GCM | PLATFORM_FLAG_TYPE_IOS)) {
            if ((platform.contains("G") || platform.contains("g")) && platform.contains("I")
                    || platform.contains("i")) {
                return true;
            }
        } else if (flag == PLATFORM_FLAG_TYPE_GCM) {
            if (platform.contains("G") || platform.contains("g")) {
                return true;
            }
        } else if (flag == PLATFORM_FLAG_TYPE_IOS) {
            if (platform.contains("I") || platform.contains("i")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Set target platform with flag.
     * 
     * @param flag PLATFORM_FLAG_TYPE_GCM(0x1), PLATFORM_FLAG_TYPE_IOS(0x2)
     */
    public void setPlatform(int flag) {
        StringBuilder builder = new StringBuilder();
        if ((flag & PLATFORM_FLAG_TYPE_GCM) == PLATFORM_FLAG_TYPE_GCM) {
            builder.append("G");
        }

        if ((flag & PLATFORM_FLAG_TYPE_IOS) == PLATFORM_FLAG_TYPE_IOS) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append("I");
        }

        JsonUtils.setStringProperty(properties, PROPERTY_PLATFORM, builder.toString());
    }

    /**
     * Get additional memo.
     * 
     * @return memo
     */
    @JsonSerialize(include = NON_NULL)
    public String getMemo() {
        return JsonUtils.getStringProperty(properties, PROPERTY_MEMO);
    }

    /**
     * Set additional memo data.
     * 
     * @param memo memo
     */
    public void setMemo(String memo) {
        JsonUtils.setStringProperty(properties, PROPERTY_MEMO, memo);
    }

    public static class Payload {
        private Integer badge;

        private String sound;

        private String alert;

        @JsonCreator
        public static Payload createObject(String jsonString) {
            Payload payload = JsonUtils.fromJsonString(jsonString, Payload.class);
            return payload;
        }

        /**
         * Get badge count.
         * 
         * @return badge count
         */
        @JsonSerialize(include = NON_NULL)
        public Integer getBadge() {
            return badge;
        }

        /**
         * Set badge count.
         */
        public void setBadge(Integer badge) {
            this.badge = badge;
        }

        /**
         * Get sound for iOS APNS.
         * 
         * @return sound
         */
        @JsonSerialize(include = NON_NULL)
        public String getSound() {
            return sound;
        }

        /**
         * Set sound for iOS APNS.
         * 
         * @param sound sound
         */
        public void setSound(String sound) {
            this.sound = sound;
        }

        /**
         * Get push message.
         * 
         * @return push message
         */
        @JsonSerialize(include = NON_NULL)
        public String getAlert() {
            return alert;
        }

        /**
         * Set push message.
         * 
         * @param alert push message
         */
        public void setAlert(String alert) {
            this.alert = alert;
        }

        @Override
        public String toString() {
            return JsonUtils.toJsonString(this);
        }
    }
}
