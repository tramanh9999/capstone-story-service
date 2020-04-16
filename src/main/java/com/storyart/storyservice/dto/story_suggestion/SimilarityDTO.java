package com.storyart.storyservice.dto.story_suggestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimilarityDTO {
    private Integer userid;
    private Double similarity;
}
