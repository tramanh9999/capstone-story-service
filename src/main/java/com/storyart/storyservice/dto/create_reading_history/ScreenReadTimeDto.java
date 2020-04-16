package com.storyart.storyservice.dto.create_reading_history;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenReadTimeDto{
    private int id = 0;
    private String screenId;
    private int duration;//count by seconds;
}
