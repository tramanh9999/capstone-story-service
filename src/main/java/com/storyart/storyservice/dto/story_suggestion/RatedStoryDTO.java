package com.storyart.storyservice.dto.story_suggestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RatedStoryDTO {

    private int storyId;
    private double ratedPoint;
}
