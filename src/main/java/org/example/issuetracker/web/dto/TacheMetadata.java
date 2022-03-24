package org.example.issuetracker.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ftoum on 19/09/2017.
 */
public class TacheMetadata {

    @JsonProperty("total_count")
    private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
