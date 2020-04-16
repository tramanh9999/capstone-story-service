package com.storyart.storyservice.dto.read_story;

import com.storyart.storyservice.model.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStoryScreenDto {
    private String id;
    private int storyId;
    private String title;
    private String content;
    List<Action> actions;
}
