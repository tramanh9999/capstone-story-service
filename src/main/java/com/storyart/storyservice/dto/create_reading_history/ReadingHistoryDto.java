package com.storyart.storyservice.dto.create_reading_history;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingHistoryDto {
    private int id = 0;
    private int storyId;
    private String listScreenId;
    private boolean isReachingEnd;
}
