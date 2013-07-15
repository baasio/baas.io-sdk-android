
package com.kth.baasio.help.data;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Tag {
    private Integer id;

    private String name;

    @JsonSerialize(include = NON_NULL)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonSerialize(include = NON_NULL)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
