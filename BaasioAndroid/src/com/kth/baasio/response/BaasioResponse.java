
package com.kth.baasio.response;

import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BaasioResponse {

    private String accessToken;

    private String path;

    private String uri;

    private String status;

    private long timestamp;

    private UUID application;

    private List<BaasioBaseEntity> entities;

    private UUID next;

    private String cursor;

    private String action;

    private List<Object> list;

    private Object data;

    private Map<String, UUID> applications;

    private Map<String, JsonNode> metadata;

    private Map<String, List<String>> params;

    private List<AggregateCounterSet> counters;

    private ClientCredentialsInfo credentials;

    private List<QueueInfo> queues;

    private UUID last;

    private UUID queue;

    private UUID consumer;

    private BaasioUser user;

    private final Map<String, JsonNode> properties = new HashMap<String, JsonNode>();

    public BaasioResponse() {
    }

    @JsonAnyGetter
    public Map<String, JsonNode> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void setProperty(String key, JsonNode value) {
        properties.put(key, value);
    }

    @JsonProperty("access_token")
    @JsonSerialize(include = Inclusion.NON_NULL)
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public UUID getApplication() {
        return application;
    }

    public void setApplication(UUID application) {
        this.application = application;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public List<BaasioBaseEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<BaasioBaseEntity> entities) {
        this.entities = entities;
    }

    public int getEntityCount() {
        if (entities == null) {
            return 0;
        }
        return entities.size();
    }

    public BaasioBaseEntity getFirstEntity() {
        if ((entities != null) && (entities.size() > 0)) {
            return entities.get(0);
        }
        return null;
    }

    public <T extends BaasioEntity> T getFirstEntity(Class<T> t) {
        return BaasioEntity.toType(getFirstEntity(), t);
    }

    public BaasioBaseEntity getLastEntity() {
        if ((entities != null) && (entities.size() > 0)) {
            return entities.get(entities.size() - 1);
        }
        return null;
    }

    public <T extends BaasioBaseEntity> T getLastEntity(Class<T> t) {
        return BaasioEntity.toType(getLastEntity(), t);
    }

    public <T extends BaasioBaseEntity> List<T> getEntities(Class<T> t) {
        return BaasioBaseEntity.toType(entities, t);
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public UUID getNext() {
        return next;
    }

    public void setNext(UUID next) {
        this.next = next;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public Map<String, UUID> getApplications() {
        return applications;
    }

    public void setApplications(Map<String, UUID> applications) {
        this.applications = applications;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public Map<String, JsonNode> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, JsonNode> metadata) {
        this.metadata = metadata;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public Map<String, List<String>> getParams() {
        return params;
    }

    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public List<AggregateCounterSet> getCounters() {
        return counters;
    }

    public void setCounters(List<AggregateCounterSet> counters) {
        this.counters = counters;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public ClientCredentialsInfo getCredentials() {
        return credentials;
    }

    public void setCredentials(ClientCredentialsInfo credentials) {
        this.credentials = credentials;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public BaasioUser getUser() {
        return user;
    }

    public void setUser(BaasioUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public UUID getLast() {
        return last;
    }

    public void setLast(UUID last) {
        this.last = last;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public List<QueueInfo> getQueues() {
        return queues;
    }

    public void setQueues(List<QueueInfo> queues) {
        this.queues = queues;
    }

    @JsonIgnore
    public QueueInfo getFirstQueue() {
        if ((queues != null) && (queues.size() > 0)) {
            return queues.get(0);
        }
        return null;
    }

    @JsonIgnore
    public QueueInfo getLastQueue() {
        if ((queues != null) && (queues.size() > 0)) {
            return queues.get(queues.size() - 1);
        }
        return null;
    }

    @JsonIgnore
    public UUID getLastQueueId() {
        QueueInfo q = getLastQueue();
        if (q != null) {
            return q.getQueue();
        }
        return null;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public UUID getQueue() {
        return queue;
    }

    public void setQueue(UUID queue) {
        this.queue = queue;
    }

    @JsonSerialize(include = Inclusion.NON_NULL)
    public UUID getConsumer() {
        return consumer;
    }

    public void setConsumer(UUID consumer) {
        this.consumer = consumer;
    }

}
