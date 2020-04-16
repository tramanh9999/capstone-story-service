package com.storyart.storyservice.dto.story_suggestion;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoryCommentDTO {
    private int StoryId;
    private double point;
}
