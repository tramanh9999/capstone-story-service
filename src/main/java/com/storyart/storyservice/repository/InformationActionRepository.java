package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.InformationAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationActionRepository extends JpaRepository<InformationAction, Integer> {
    List<InformationAction> findAllByInformationIdIn(List<String> informationIds);
    void deleteAllByInformationIdIn(List<String> informationId);
}
