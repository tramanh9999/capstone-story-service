package com.storyart.storyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSectionDto implements Serializable {
    private String id;
    private String title;
    private int storyId;
    private String content;
    private int parameterPoint;
    private boolean isEndSection;
    private String parentSectionId;
    private boolean isNew;
}
