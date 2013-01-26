
package com.kth.baasio.response;

import com.kth.baasio.utils.JsonUtils;

public class AggregateCounter {

    private long timestamp;

    private long value;

    public AggregateCounter(long timestamp, long value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

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
