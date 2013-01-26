
package com.kth.baasio.help.data;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

public class FaqCategory {
    private List<Faq> faqs;

    private Integer id;

    private String name;

    private Integer sort;

    private String type;

    /**
     * Get FAQ list.
     * 
     * @return FAQ list
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("helps")
    public List<Faq> getFaqs() {
        return faqs;
    }

    /**
     * Set FAQ list.
     * 
     * @param faqs FAQ list
     */
    public void setFaqs(List<Faq> faqs) {
        this.faqs = faqs;
    }

    /**
     * Get category id.
     * 
     * @return Category id
     */
    @JsonSerialize(include = NON_NULL)
    public Integer getId() {
        return id;
    }

    /**
     * Set category id.
     * 
     * @param id Category id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get category name.
     * 
     * @return Category name
     */
    @JsonSerialize(include = NON_NULL)
    public String getName() {
        return name;
    }

    /**
     * Set category name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get a number of sort order.
     * 
     * @return Sort order
     */
    @JsonSerialize(include = NON_NULL)
    public Integer getSort() {
        return sort;
    }

    /**
     * Set a number of sort order.
     * 
     * @param sort Sort order
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @JsonSerialize(include = NON_NULL)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
