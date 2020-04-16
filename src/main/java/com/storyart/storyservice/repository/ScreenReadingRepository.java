package com.storyart.storyservice.repository;

import com.storyart.storyservice.model.ScreenReadingTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenReadingRepository extends JpaRepository<ScreenReadingTime, Integer> {



}
