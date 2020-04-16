package com.storyart.storyservice.repository;

import com.storyart.storyservice.dto.statistic.ILinkClickCountResponse;
import com.storyart.storyservice.dto.statistic.LinkClickCountResponse;
import com.storyart.storyservice.model.ClickLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface ClickLinkRepository extends JpaRepository<ClickLink, Integer> {
    List<ClickLink> findByStoryIdAndCreatedAtBetweenOrderByCreatedAtDesc(int storyId, Date startDate, Date endDate);


@Query(value = "select link , count(*) as 'count' from click_link\n" +
        " where \n" +
        "story_id=:storyId and (created_at between :start and  :end) group By link", nativeQuery = true)
    List<ILinkClickCountResponse> countLinkClickNumberByLinkByStoryIdInTimeRange(@Param("storyId") Integer storyId,
                                                                                 @Param("start") String start,@Param("end") String end);


}
