
package com.kth.baasio.response;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.kth.baasio.utils.JsonUtils;

public class AggregateCounter {

    private long timestamp;
    private long value;
    
    public AggregateCounter() {
    	
    }
    public AggregateCounter(long timestamp, long value) {
        this.timestamp = timestamp;
        this.value = value;
    }
    @JsonSerialize(include = Inclusion.NON_NULL)
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    @JsonSerialize(include = Inclusion.NON_NULL)
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

}
