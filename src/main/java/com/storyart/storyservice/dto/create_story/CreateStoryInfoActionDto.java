package com.storyart.storyservice.dto.create_story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoryInfoActionDto {
    private String actionId;
    private String informationId;
    private String value; //gia tri tac dong
}
