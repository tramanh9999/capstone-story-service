package com.storyart.storyservice.model;

import com.storyart.storyservice.model.ids.StoryTagId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "story_tag")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(StoryTagId.class)
public class StoryTag {
    @Id
    private int tagId;

    @Id
    private int storyId;
}
