
package com.kth.baasio.response;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;

public class ClientCredentialsInfo {

    private String id;

    private String secret;

    public ClientCredentialsInfo(String id, String secret) {
        this.id = id;
        this.secret = secret;
    }

    @JsonProperty("client_id")
    public String getId() {
        return id;
    }

    @JsonProperty("client_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("client_secret")
    public String getSecret() {
        return secret;
    }

    @JsonProperty("client_secret")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

}
