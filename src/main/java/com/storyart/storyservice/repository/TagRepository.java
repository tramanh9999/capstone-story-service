package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "SELECT * FROM tag t where t.id in " +
            "(select st.tag_id from story_tag st where st.story_id = ?1)", nativeQuery = true)
    List<Tag> findAllByStoryId(int storyId);

    @Query(value = "SELECT * FROM tag t where t.is_active = ?1", nativeQuery = true)
    List<Tag> findAllByActive(boolean active);

    Tag findByTitle(String title);

    Page<Tag> findAll(Pageable sort);

}
