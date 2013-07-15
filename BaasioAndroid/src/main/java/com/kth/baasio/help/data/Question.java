
package com.kth.baasio.help.data;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

public class Question {
    private String acceptedAt;

    private String acceptedNumber;

    private String appInfo;

    private Integer applicationId;

    private Integer classificationId;

    private Long completedAt;

    private String content;

    private Long createdAt;

    private Boolean deleted;

    private String deviceInfo;

    private Boolean disabled;

    private Boolean editable;

    private String email;

    private Integer id;

    private Boolean official;

    private String osInfo;

    private String platform;

    private Boolean publicAccessible;

    private Integer satifactionLevelId;

    private Integer statusDefaultId;

    private Integer statusId;

    private List<Tag> tags;

    private String temporaryAnswer;

    private String title;

    private Long updatedAt;

    private Integer userId;

    private String userName;

    private String uuid;

    private Integer viewCount;

    private Integer vote;

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("accepted_at")
    public String getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(String acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("accepted_number")
    public String getAcceptedNumber() {
        return acceptedNumber;
    }

    public void setAcceptedNumber(String acceptedNumber) {
        this.acceptedNumber = acceptedNumber;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("app_info")
    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("application_id")
    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("classification_id")
    public Integer getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Integer classificationId) {
        this.classificationId = classificationId;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("completed_at")
    public Long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Long completedAt) {
        this.completedAt = completedAt;
    }

    @JsonSerialize(include = NON_NULL)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("created_at")
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @JsonSerialize(include = NON_NULL)
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("device_info")
    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @JsonSerialize(include = NON_NULL)
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @JsonSerialize(include = NON_NULL)
    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @JsonSerialize(include = NON_NULL)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonSerialize(include = NON_NULL)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("official")
    public Boolean isOfficial() {
        return official;
    }

    public void setOfficial(Boolean official) {
        this.official = official;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("os_info")
    public String getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    @JsonSerialize(include = NON_NULL)
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("public_accessible")
    public Boolean getPublicAccessible() {
        return publicAccessible;
    }

    public void setPublicAccessible(Boolean publicAccessible) {
        this.publicAccessible = publicAccessible;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("satisfaction_level_id")
    public Integer getSatifactionLevelId() {
        return satifactionLevelId;
    }

    public void setSatifactionLevelId(Integer satifactionLevelId) {
        this.satifactionLevelId = satifactionLevelId;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("status_default_id")
    public Integer getStatusDefaultId() {
        return statusDefaultId;
    }

    public void setStatusDefaultId(Integer statusDefaultId) {
        this.statusDefaultId = statusDefaultId;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("status_id")
    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    @JsonSerialize(include = NON_NULL)
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("temporary_answer")
    public String getTemporaryAnswer() {
        return temporaryAnswer;
    }

    public void setTemporaryAnswer(String temporaryAnswer) {
        this.temporaryAnswer = temporaryAnswer;
    }

    @JsonSerialize(include = NON_NULL)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonSerialize(include = NON_NULL)
    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonSerialize(include = NON_NULL)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("view_count")
    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @JsonSerialize(include = NON_NULL)
    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
