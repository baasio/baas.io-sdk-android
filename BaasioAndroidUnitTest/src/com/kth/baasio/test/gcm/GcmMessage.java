
package com.kth.baasio.test.gcm;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.entity.push.BaasioMessage.Payload;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class GcmMessage {
    public Payload aps;

    /**
     * Get payload to send.
     * 
     * @return payload
     */
    @JsonSerialize(include = NON_NULL)
    public Payload getAps() {
        return aps;
    }

    /**
     * Set payload to send.
     * 
     * @param payload Payload to send
     */
    public void setAps(Payload aps) {
        this.aps = aps;
    }
}
