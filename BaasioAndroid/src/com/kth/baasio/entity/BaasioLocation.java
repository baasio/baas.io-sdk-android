
package com.kth.baasio.entity;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

public class BaasioLocation {
    public final static String PROPERTY_LATITUDE = "latitude";

    public final static String PROPERTY_LONGITUDE = "longitude";

    private Double latitude;

    private Double longitude;

    public BaasioLocation() {

    }

    @JsonCreator
    public static BaasioLocation createObject(String jsonString) {
        BaasioLocation facebook = JsonUtils.fromJsonString(jsonString, BaasioLocation.class);
        return facebook;
    }

    @JsonIgnore
    public List<String> getPropertyNames() {
        List<String> properties = new ArrayList<String>();
        properties.add(PROPERTY_LATITUDE);
        properties.add(PROPERTY_LONGITUDE);
        return properties;
    }

    @JsonSerialize(include = NON_NULL)
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonSerialize(include = NON_NULL)
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
