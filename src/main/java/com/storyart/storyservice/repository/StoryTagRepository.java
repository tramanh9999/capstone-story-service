package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.StoryTag;
import com.storyart.storyservice.model.ids.StoryTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryTagRepository extends JpaRepository<StoryTag, StoryTagId> {
    List<StoryTag> findAllByStoryId(int storyId);
}
