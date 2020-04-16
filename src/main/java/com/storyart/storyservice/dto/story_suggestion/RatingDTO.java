package com.storyart.storyservice.dto.story_suggestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    private int userid;
    private List<RatedStoryDTO> listPoint;
}
