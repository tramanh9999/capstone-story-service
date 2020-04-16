package com.storyart.storyservice.dto.read_story;

import com.storyart.storyservice.model.InfoCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStoryInformationDto {
    private String id;
    private int storyId;
    private String name;
    private String value;
    private String unit;
    private String type;

    private List<InfoCondition> conditions;
}
