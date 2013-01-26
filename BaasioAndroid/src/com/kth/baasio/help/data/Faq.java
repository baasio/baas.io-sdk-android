
package com.kth.baasio.help.data;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Faq {
    private String applicationId;

    private String classificationId;

    private String content;

    private Long createdAt;

    private Boolean disabled;

    private Integer id;

    private String title;

    private Long updatedAt;

    private String uuid;

    private Integer viewCount;

    private Boolean isFeatured;

    /**
     * Get application id.
     * 
     * @return Application id
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("application_id")
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Set application id.
     * 
     * @param applicationId Application id
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Get classification id.
     * 
     * @return Classification id
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("classification_id")
    public String getClassificationId() {
        return classificationId;
    }

    /**
     * Set classification id.
     * 
     * @param classificationId Classification id
     */
    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    /**
     * Get body content.
     * 
     * @return body content
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    /**
     * Set body content.
     * 
     * @param content Body content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get created time.
     * 
     * @return Created time
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("created_at")
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     * Set created time.
     * 
     * @param createdAt Created time
     */
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get disable state.
     * 
     * @return Disable state
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("disabled")
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * Set disable state.
     * 
     * @param disabled Disable state
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Get id.
     * 
     * @return Id
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * Set id.
     * 
     * @param id Id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get title.
     * 
     * @return Title
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Set title.
     * 
     * @param title Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get updated time.
     * 
     * @return Updated time
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("updated_at")
    public Long getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set updated time.
     * 
     * @param updatedAt Updated time
     */
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Get uuid.
     * 
     * @return uuid
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * Set uuid.
     * 
     * @param uuid uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Get view count.
     * 
     * @return View count
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("view_count")
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * Set view count.
     * 
     * @param viewCount View count
     */
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * Get view count.
     * 
     * @return View count
     */
    @JsonSerialize(include = NON_NULL)
    @JsonProperty("is_featured")
    public Boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
