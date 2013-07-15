
package com.kth.baasio.help.data;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import com.kth.baasio.utils.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Pagination {
    private Integer allCount;

    private Integer currentPage;

    private Integer totalCount;

    private Integer totalPage;

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("all_count")
    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("current_page")
    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("total_count")
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @JsonSerialize(include = NON_NULL)
    @JsonProperty("total_page")
    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
