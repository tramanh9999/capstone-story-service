package com.storyart.storyservice.dto.create_reading_history;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClickLinkDto{
    private int storyId;
    private String link;//count by seconds;
}
