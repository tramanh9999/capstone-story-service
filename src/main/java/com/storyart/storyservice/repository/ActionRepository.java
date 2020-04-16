package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, String> {

    @Query(value = "select * from action a where a.screen_id = ?1 " +
            "order by a.my_index ASC",
            nativeQuery = true)
    List<Action> findAllByScreenId(String screenId);
    void deleteAllByScreenId(String screenId);
}
