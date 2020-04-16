package com.storyart.storyservice.repository;

import com.storyart.storyservice.dto.statistic.ILinkClickCountResponse;
import com.storyart.storyservice.dto.statistics.ReadStatisticDto;
import com.storyart.storyservice.model.ReadingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<ReadingHistory, Integer>
{
    @Query(value = "select * FROM storyart_db.reading_history where reading_history.user_id in (:userid) order by reading_history.user_id", nativeQuery = true)
    List<ReadingHistory> findHistoryById(@Param("userid") List<Integer> userid);

    @Query(value = "select * FROM storyart_db.reading_history where reading_history.user_id = :userid", nativeQuery = true)
    List<ReadingHistory> findHistoryByIdOnly(@Param("userid") Integer userid);

    @Query(value = "select distinct user_id from reading_history where Not reading_history.user_id = :userid order by reading_history.user_id", nativeQuery = true)
    List<Integer>  findUserIdHistoryExceptId(@Param("userid") Integer userid);

    @Query(value = "select story_id from reading_history where user_id = :param", nativeQuery = true)
    List<Integer>  findListHistory(@Param("param") Integer userid);

    @Query(value = "select count(id) as readCount, DATE(created_at) as dateCreated from reading_history " +
            "where (story_id IN (select id from story where user_id = ?3) " +
            "and DATE(created_at) <= DATE(?2) and DATE(created_at) >= DATE(?1) ) GROUP BY DATE(created_at) order by dateCreated ASC",
            nativeQuery = true)
    List<ReadStatisticDto> findReadingStatisticsByDateRangeOfUser(Date from, Date to, int userId);

    List<ReadingHistory> findAllByStoryIdAndIsReachingEndAndCreatedAtBetweenOrderByCreatedAtDesc(int storyId,boolean reachingEnd, Date startDate, Date endDate );
    List<ReadingHistory> findAllByStoryIdAndCreatedAtBetweenOrderByCreatedAtDesc(int storyId, Date startDate, Date endDate );
    @Query(value = "SELECT * FROM storyart_db.reading_history as rh1" +
            " JOIN (select max(id) id, max(created_at) " +
            "from storyart_db.reading_history " +
            "where user_id = ?1 group by user_id, story_id) as rh2" +
            " on rh1.id = rh2.id order by created_at desc", nativeQuery = true)
    Page<ReadingHistory> findAllWithUserId(int userId, Pageable pageable);

    @Query(value = "SELECT r.story_id FROM storyart_db.reading_history as r, storyart_db.story as s " +
            "where r.story_id = s.id  and s.active = '1' and s.published = '1' and s.deactive_by_admin = '0' " +
            "group by r.story_id order by count(r.story_id) DESC limit 10", nativeQuery = true)
    List<Integer>  countTopView();

    int countAllByStoryId(int storyId);
}
