package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.Information;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, String> {
    List<Information> findAllByStoryId(int storyId);
    void deleteAllByStoryIdEquals(int storyId);
}
