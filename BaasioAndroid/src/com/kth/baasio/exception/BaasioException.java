/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.kth.baasio.exception;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.MapUtils;
import com.kth.baasio.utils.ObjectUtils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Simple wrapper for client exceptions
 * 
 * @author tnine
 */
public class BaasioException extends Exception {
    private static final long serialVersionUID = 1L;

    private String statusCode = null;

    private BaasioOtherCauses otherCauses = new BaasioOtherCauses();

    public BaasioException(Throwable cause) {
        super(cause);
    }

    public BaasioException(String error) {
        super(error);
    }

    public BaasioException(HttpStatus status, String errorBody) {
        super();

        statusCode = status.toString();
        try {
            otherCauses = JsonUtils.parse(errorBody, BaasioOtherCauses.class);
        } catch (BaasioRuntimeException e) {
            otherCauses.setProperty("error", errorBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        otherCauses.setProperty("statusCode", status.toString());
    }

    public String getStatusCode() {
        return statusCode;
    }

    public long getTimestamp() {
        if (!ObjectUtils.isEmpty(otherCauses) && !ObjectUtils.isEmpty(otherCauses.getTimestamp())) {
            return otherCauses.getTimestamp();
        }

        return -1;
    }

    public long getDuration() {
        if (!ObjectUtils.isEmpty(otherCauses) && !ObjectUtils.isEmpty(otherCauses.getDuration())) {
            return otherCauses.getDuration();
        }

        return -1;
    }

    public String getErrorDescription() {
        if (!ObjectUtils.isEmpty(otherCauses)) {
            return otherCauses.getErrorDescription();
        }

        return null;
    }

    public String getErrorUuid() {
        if (!ObjectUtils.isEmpty(otherCauses) && !ObjectUtils.isEmpty(otherCauses.getUuid())) {
            return otherCauses.getUuid().toString();
        }

        return null;
    }

    public long getErrorCode() {
        if (!ObjectUtils.isEmpty(otherCauses) && !ObjectUtils.isEmpty(otherCauses.getErrorCode())) {
            return otherCauses.getErrorCode();
        }

        return 0;
    }

    public Map<String, JsonNode> getOtherErrorMessages() {
        if (!ObjectUtils.isEmpty(otherCauses)) {
            return otherCauses.getProperties();
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());

        if (otherCauses != null) {
            builder.append(":" + JsonUtils.toJsonString(otherCauses));
        }
        return builder.toString();
    }

    static class BaasioOtherCauses {
        public final static String PROPERTY_TIMESTAMP = "timestamp";

        public final static String PROPERTY_DURATION = "duration";

        public final static String PROPERTY_ERROR_DESCRIPTION = "error_description";

        public final static String PROPERTY_ERROR_UUID = "error_uuid";

        public final static String PROPERTY_ERROR_CODE = "error_code";

        protected Map<String, JsonNode> properties = new HashMap<String, JsonNode>();

        @JsonCreator
        public static BaasioOtherCauses createObject(String jsonString) {
            BaasioOtherCauses otherCauses = JsonUtils.fromJsonString(jsonString,
                    BaasioOtherCauses.class);
            return otherCauses;
        }

        @JsonIgnore
        public List<String> getPropertyNames() {
            List<String> properties = new ArrayList<String>();
            properties.add(PROPERTY_TIMESTAMP);
            properties.add(PROPERTY_DURATION);
            properties.add(PROPERTY_ERROR_DESCRIPTION);
            properties.add(PROPERTY_ERROR_UUID);
            properties.add(PROPERTY_ERROR_CODE);
            return properties;
        }

        @JsonAnyGetter
        public Map<String, JsonNode> getProperties() {
            return MapUtils.newMapWithoutKeys(properties, getPropertyNames());
        }

        @JsonAnySetter
        public void setProperty(String name, JsonNode value) {
            if (value == null) {
                properties.remove(name);
            } else {
                properties.put(name, value);
            }
        }

        /**
         * Get error timestamp.
         * 
         * @return Error timestamp
         */
        @JsonSerialize(include = NON_NULL)
        @JsonProperty(PROPERTY_TIMESTAMP)
        public Long getTimestamp() {
            return JsonUtils.getLongProperty(properties, PROPERTY_TIMESTAMP);
        }

        /**
         * Get error description.
         * 
         * @return Error description
         */
        @JsonSerialize(include = NON_NULL)
        @JsonProperty(PROPERTY_ERROR_DESCRIPTION)
        public String getErrorDescription() {
            return JsonUtils.getStringProperty(properties, PROPERTY_ERROR_DESCRIPTION);
        }

        /**
         * Get error duration.
         * 
         * @return Error duration
         */
        @JsonSerialize(include = NON_NULL)
        @JsonProperty(PROPERTY_DURATION)
        public Long getDuration() {
            return JsonUtils.getLongProperty(properties, PROPERTY_DURATION);
        }

        /**
         * Get error UUID.
         * 
         * @return error UUID
         */
        @JsonSerialize(include = NON_NULL)
        @JsonProperty(PROPERTY_ERROR_UUID)
        public UUID getUuid() {
            return JsonUtils.getUUIDProperty(properties, PROPERTY_ERROR_UUID);
        }

        /**
         * Get error UUID.
         * 
         * @return error UUID
         */
        @JsonSerialize(include = NON_NULL)
        @JsonProperty(PROPERTY_ERROR_CODE)
        public Long getErrorCode() {
            return JsonUtils.getLongProperty(properties, PROPERTY_ERROR_CODE);
        }

        /**
         * Set the property
         * 
         * @param name
         * @param value
         */
        public void setProperty(String name, String value) {
            JsonUtils.setStringProperty(properties, name, value);
        }

        /**
         * Set the property
         * 
         * @param name
         * @param value
         */
        public void setProperty(String name, boolean value) {
            JsonUtils.setBooleanProperty(properties, name, value);
        }

        /**
         * Set the property
         * 
         * @param name
         * @param value
         */
        public void setProperty(String name, long value) {
            JsonUtils.setLongProperty(properties, name, value);
        }

        /**
         * Set the property
         * 
         * @param name
         * @param value
         */
        public void setProperty(String name, int value) {
            setProperty(name, (long)value);
        }

        /**
         * Set the property
         * 
         * @param name
         * @param value
         */
        public void setProperty(String name, double value) {
            JsonUtils.setDoubleProperty(properties, name, value);
        }

        @Override
        public String toString() {
            return JsonUtils.toJsonString(this);
        }
    }
}
