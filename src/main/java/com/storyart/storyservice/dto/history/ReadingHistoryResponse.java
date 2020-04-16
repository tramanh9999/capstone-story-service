package com.storyart.storyservice.dto.history;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingHistoryResponse {
    private int id;
    private Integer userId;

    private Integer storyId;
    private String storyName;
    private String storyContent;
    private String storyImageUrl;

    private String listScreenId;
    private boolean isReachingEnd;
    private Date createdAt;
    private Date updatedAt;
}
