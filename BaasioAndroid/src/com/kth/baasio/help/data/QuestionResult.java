
package com.kth.baasio.help.data;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

public class QuestionResult {
    private String action;

    private String application;

    private String applicationName;

    private String baasioId;

    private List<Question> entities;

    @JsonSerialize(include = NON_NULL)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @JsonSerialize(include = NON_NULL)
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    @JsonSerialize(include = NON_NULL)
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("baasio_id")
    public String getBaasioId() {
        return baasioId;
    }

    public void setBaasioId(String baasioId) {
        this.baasioId = baasioId;
    }

    @JsonSerialize(include = NON_NULL)
    public List<Question> getEntities() {
        return entities;
    }

    public void setEntities(List<Question> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
