package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.InfoCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoConditionRepository extends JpaRepository<InfoCondition, Integer> {
    List<InfoCondition> findAllByInformationId(String id);
}
