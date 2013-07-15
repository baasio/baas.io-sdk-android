
package com.kth.baasio.entity.user;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.MapUtils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaasioFacebook {
    public final static String PROPERTY_ID = "id";

    public final static String PROPERTY_NAME = "name";

    public final static String PROPERTY_FIRSTNAME = "first_name";

    public final static String PROPERTY_MIDDLENAME = "middle_name";

    public final static String PROPERTY_LASTNAME = "last_name";

    public final static String PROPERTY_LINK = "link";

    public final static String PROPERTY_USERNAME = "username";

    public final static String PROPERTY_GENDER = "gender";

    public final static String PROPERTY_EMAIL = "email";

    public final static String PROPERTY_TIMEZONE = "timezone";

    public final static String PROPERTY_LOCALE = "locale";

    public final static String PROPERTY_VERIFIED = "verified";

    public final static String PROPERTY_UPDATEDTIME = "updated_time";

    protected Map<String, JsonNode> properties = new HashMap<String, JsonNode>();

    public BaasioFacebook() {

    }

    @JsonCreator
    public static BaasioFacebook createObject(String jsonString) {
        BaasioFacebook facebook = JsonUtils.fromJsonString(jsonString, BaasioFacebook.class);
        return facebook;
    }

    /**
     * Get predefined property names.
     * 
     * @return List of predefined property names
     */
    @JsonIgnore
    public List<String> getPropertyNames() {
        List<String> properties = new ArrayList<String>();
        properties.add(PROPERTY_ID);
        properties.add(PROPERTY_NAME);
        properties.add(PROPERTY_FIRSTNAME);
        properties.add(PROPERTY_MIDDLENAME);
        properties.add(PROPERTY_LASTNAME);
        properties.add(PROPERTY_LINK);
        properties.add(PROPERTY_USERNAME);
        properties.add(PROPERTY_GENDER);
        properties.add(PROPERTY_EMAIL);
        properties.add(PROPERTY_TIMEZONE);
        properties.add(PROPERTY_LOCALE);
        properties.add(PROPERTY_VERIFIED);
        properties.add(PROPERTY_UPDATEDTIME);
        return properties;
    }

    /**
     * Get id.
     * 
     * @return Id
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_ID)
    public String getId() {
        return JsonUtils.getStringProperty(properties, PROPERTY_ID);
    }

    /**
     * Set id.
     * 
     * @param id Id
     */
    public void setId(String id) {
        JsonUtils.setStringProperty(properties, PROPERTY_ID, id);
    }

    /**
     * Get name.
     * 
     * @return Name
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_NAME)
    public String getName() {
        return JsonUtils.getStringProperty(properties, PROPERTY_NAME);
    }

    /**
     * Set name.
     * 
     * @param name Name
     */
    public void setName(String name) {
        JsonUtils.setStringProperty(properties, PROPERTY_NAME, name);
    }

    /**
     * Get first name.
     * 
     * @return First name
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_FIRSTNAME)
    public String getFirstname() {
        return JsonUtils.getStringProperty(properties, PROPERTY_FIRSTNAME);
    }

    /**
     * Set first name.
     * 
     * @param firstname First name
     */
    public void setFirstname(String firstname) {
        JsonUtils.setStringProperty(properties, PROPERTY_FIRSTNAME, firstname);
    }

    /**
     * Get middle name.
     * 
     * @return Middle name
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_MIDDLENAME)
    public String getMiddlename() {
        return JsonUtils.getStringProperty(properties, PROPERTY_MIDDLENAME);
    }

    /**
     * Set middle name.
     * 
     * @param middlename Middle name
     */
    public void setMiddlename(String middlename) {
        JsonUtils.setStringProperty(properties, PROPERTY_MIDDLENAME, middlename);
    }

    /**
     * Get last name.
     * 
     * @return Last name
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_LASTNAME)
    public String getLastname() {
        return JsonUtils.getStringProperty(properties, PROPERTY_LASTNAME);
    }

    /**
     * Set last name.
     * 
     * @param lastname Last name
     */
    public void setLastname(String lastname) {
        JsonUtils.setStringProperty(properties, PROPERTY_LASTNAME, lastname);
    }

    /**
     * Get link.
     * 
     * @return Link
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_LINK)
    public String getLink() {
        return JsonUtils.getStringProperty(properties, PROPERTY_LINK);
    }

    /**
     * Set link.
     * 
     * @param link Link
     */
    public void setLink(String link) {
        JsonUtils.setStringProperty(properties, PROPERTY_LINK, link);
    }

    /**
     * Get user name.
     * 
     * @return User name
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_USERNAME)
    public String getUsername() {
        return JsonUtils.getStringProperty(properties, PROPERTY_USERNAME);
    }

    /**
     * Set user name.
     * 
     * @param username User name
     */
    public void setUsername(String username) {
        JsonUtils.setStringProperty(properties, PROPERTY_USERNAME, username);
    }

    /**
     * Get gender.
     * 
     * @return Gender
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_GENDER)
    public String getGender() {
        return JsonUtils.getStringProperty(properties, PROPERTY_GENDER);
    }

    /**
     * Set gender.
     * 
     * @param gender Gender
     */
    public void setGender(String gender) {
        JsonUtils.setStringProperty(properties, PROPERTY_GENDER, gender);
    }

    /**
     * Get email.
     * 
     * @return Email
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_EMAIL)
    public String getEmail() {
        return JsonUtils.getStringProperty(properties, PROPERTY_EMAIL);
    }

    /**
     * Set email.
     * 
     * @param email email
     */
    public void setEmail(String email) {
        JsonUtils.setStringProperty(properties, PROPERTY_EMAIL, email);
    }

    /**
     * Get time zone.
     * 
     * @return Time zone
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_TIMEZONE)
    public Long getTimezone() {
        return JsonUtils.getLongProperty(properties, PROPERTY_TIMEZONE);
    }

    /**
     * Set time zone.
     * 
     * @param timezone Time zone
     */
    public void setTimeZone(Long timezone) {
        JsonUtils.setLongProperty(properties, PROPERTY_TIMEZONE, timezone);
    }

    /**
     * Get locale.
     * 
     * @return Locale
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_LOCALE)
    public String getLocale() {
        return JsonUtils.getStringProperty(properties, PROPERTY_LOCALE);
    }

    /**
     * Set locale.
     * 
     * @param locale Locale
     */
    public void setLocale(String locale) {
        JsonUtils.setStringProperty(properties, PROPERTY_LOCALE, locale);
    }

    /**
     * Get verified.
     * 
     * @return Verified
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_VERIFIED)
    public Boolean getVerified() {
        return JsonUtils.getBooleanProperty(properties, PROPERTY_VERIFIED);
    }

    /**
     * Set verified.
     * 
     * @param verified Verified
     */
    public void setVerified(boolean verified) {
        JsonUtils.setBooleanProperty(properties, PROPERTY_VERIFIED, true);
    }

    /**
     * Get updated time.
     * 
     * @return Updated time
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty(PROPERTY_UPDATEDTIME)
    public String getUpdatedTime() {
        return JsonUtils.getStringProperty(properties, PROPERTY_UPDATEDTIME);
    }

    /**
     * Set updated time.
     * 
     * @param updatedTime Updated time
     */
    public void setUpdatedTime(String updatedTime) {
        JsonUtils.setStringProperty(properties, PROPERTY_UPDATEDTIME, updatedTime);
    }

    /**
     * Get custom properties list.
     * 
     * @return Custom properties list
     */
    @JsonAnyGetter
    public Map<String, JsonNode> getProperties() {
        return MapUtils.newMapWithoutKeys(properties, getPropertyNames());
    }

    /**
     * Set a custom property
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

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
