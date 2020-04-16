package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, String> {
    @Query(value = "select * from screen sc where sc.story_id = ?1 " +
            "order by sc.my_index ASC",
            nativeQuery = true)
    List<Screen> findByStoryId(int storyId);
    void deleteAllByStoryId(int storyId);

    int countAllByStoryId(int storyId);
}
