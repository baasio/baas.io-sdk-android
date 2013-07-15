
package com.kth.baasio.help.data;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

public class HelpResult {
    private String action;

    private String application;

    private String applicationName;

    private String baasioId;

    private List<Faq> entities;

    /**
     * Get request action type.
     * 
     * @return Action type
     */
    @JsonSerialize(include = NON_NULL)
    public String getAction() {
        return action;
    }

    /**
     * Set request action type.
     * 
     * @param action Action type
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Get application uuid.
     * 
     * @return Application uuid
     */
    @JsonSerialize(include = NON_NULL)
    public String getApplication() {
        return application;
    }

    /**
     * Set application uuid.
     * 
     * @param application Application uuid
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * Get application id.
     * 
     * @return Application id
     */
    @JsonSerialize(include = NON_NULL)
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Set application id.
     * 
     * @param applicationName Application id
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Get baas.io member id.
     * 
     * @return baas.io member id
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("baasio_id")
    public String getBaasioId() {
        return baasioId;
    }

    /**
     * Set baas.io member id.
     * 
     * @param baasioId baas.io member id
     */
    public void setBaasioId(String baasioId) {
        this.baasioId = baasioId;
    }

    /**
     * Get FAQ list
     * 
     * @return baas.io member id
     */
    @JsonSerialize(include = NON_NULL)
    public List<Faq> getEntities() {
        return entities;
    }

    public void setEntities(List<Faq> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
