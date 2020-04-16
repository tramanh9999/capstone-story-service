package com.storyart.storyservice.repository;

import com.storyart.storyservice.common.MyQueries;
import com.storyart.storyservice.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface StoryRepository extends JpaRepository<Story, Integer> {
    @Query(value = "select * from story s " +
            "left join user u ON s.user_id = u.id " +
            "WHERE (s.active = true and s.published = true and s.deactive_by_admin = false) " +
            "and (s.title like %?1% or s.intro like %?1% or " +
            "u.name like %?1%) and u.is_active = true and u.is_deactive_by_admin = false " +
            "and s.id in (select distinct story_id from story_tag st where st.tag_id in ?2) order by s.created_at DESC",

            countQuery = "select count(*) from story s left join user u ON s.user_id = u.id WHERE " +
                    "(s.active = true and s.published = true and s.deactive_by_admin = false) " +
                    "and (s.title like %?1% or s.intro like %?1% or " +
                    "u.name like %?1%) and u.is_active = true and u.is_deactive_by_admin = false " +
                    "and s.id in (select distinct story_id from story_tag st where st.tag_id in ?2) order by s.created_at DESC",

            nativeQuery = true)
    Page<Story> findAllBySearchCondition(String title, Set<Integer> tagIds,
                                boolean active, boolean published, Pageable pageable);

    @Query(value = "select * from story s WHERE s.user_id = ?1 and (s.title like %?2% or s.intro like %?2%) " +
            "and s.active = true and s.published = true and s.deactive_by_admin = false and s.id in " +
            "(select distinct story_id from story_tag st where st.tag_id in ?3) order by s.created_at DESC",

            countQuery = "select count(*) from story s WHERE s.user_id = ?1 and (s.title like %?2% or s.intro like %?2%) " +
                    "and s.active = true and s.published = true and s.deactive_by_admin = false and s.id in " +
                    "(select distinct story_id from story_tag st where st.tag_id in ?3) order by s.created_at DESC",

            nativeQuery = true)
    Page<Story> findAllByUserProfile(int userId, String title, Set<Integer> tagIds, Pageable pageable);


    @Query(value = "select * from story s " +
            "where s.active = true and s.deactive_by_admin = false and s.published = true order by " +
            "(select count(id) from reading_history rd where rd.story_id = s.id " +
            "and (DATE(rd.created_at) >= (DATE(NOW()) - INTERVAL 7 DAY) " +
            "and (DATE(rd.created_at) <= DATE(NOW())) )) DESC LIMIT 8", nativeQuery = true)
    List<Story> findTheMostReadingStories();

    @Query(value = MyQueries. getStoriesForAdminOrderByDate+ " ASC",
            countQuery = MyQueries.countStoriesByKeyword,
            nativeQuery = true)
    Page<Story> findForAdminOrderDateASC(String keyword, Pageable pageable);

    @Query(value = MyQueries. getStoriesForAdminOrderByDate+ " DESC",
            countQuery = MyQueries.countStoriesByKeyword,
            nativeQuery = true)
    Page<Story> findForAdminOrderDateDESC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByComment + " ASC",
            countQuery = MyQueries.countStoriesByKeyword,
            nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfCommentASC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByComment + " DESC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfCommentDESC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByRating + " DESC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfRatingDESC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByRating + " ASC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfRatingASC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByScreen + " DESC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfScreenDESC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByScreen + " ASC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfScreenASC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByAvgRate + " DESC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByAvgRateDESC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByAvgRate + " ASC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByAvgRateASC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByRead + " ASC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfReadASC(String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForAdminOrderByRead + " DESC",
            countQuery = MyQueries.countStoriesByKeyword, nativeQuery = true)
    Page<Story> findForAdminOrderByNumOfReadDESC(String keyword, Pageable pageable);

    //for users
    @Query(value = MyQueries.getStoriesForUserOrderByDate + " ASC", nativeQuery = true)
    Page<Story> findForUserOrderByDateASC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByDate + " DESC", nativeQuery = true)
    Page<Story> findForUserOrderByDateDESC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByComment + " ASC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfCommentASC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByComment + " DESC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfCommentDESC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByRating + " DESC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfRatingDESC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByRating + " ASC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfRatingASC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByScreen + " DESC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfScreenDESC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByScreen + " ASC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfScreenASC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByAvgRate + " DESC", nativeQuery = true)
    Page<Story> findForUserOrderByAvgRateDESC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByAvgRate + " ASC", nativeQuery = true)
    Page<Story> findForUserOrderByAvgRateASC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByRead + " ASC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfReadASC(int userId, String keyword, Pageable pageable);

    @Query(value = MyQueries.getStoriesForUserOrderByRead + " DESC", nativeQuery = true)
    Page<Story> findForUserOrderByNumOfReadDESC(int userId, String keyword, Pageable pageable);
    //end for user

    @Query(value = "SELECT story.id FROM storyart_db.story", nativeQuery = true)
    List<Integer> findAllStory ();

    @Query(value = "SELECT * FROM story where id = :storyid", nativeQuery = true)
    Story findStoryById (@Param("storyid") Integer storyid);

    @Query(value = "select id from storyart_db.story  where story.published = '1' and story.active ='1' and story.deactive_by_admin ='0' and YEARWEEK(story.created_at) = YEARWEEK(NOW())", nativeQuery = true)
    List<Integer> findStoryThisWeek();

    @Query(value = "select id from storyart_db.story  where story.published = '1' and story.active ='1' and story.deactive_by_admin ='0' and not YEARWEEK(story.created_at) = YEARWEEK(NOW())", nativeQuery = true)
    List<Integer> findStoryExceptThisWeek();

    @Query(value = "SELECT * FROM storyart_db.story order by  story.created_at DESC", nativeQuery = true)
    Page<Story> findStoryOrderByCreateAt(Pageable page);

    @Query(value = "select * from storyart_db.story where story.id in (:storyIds) and story.published = '1' and story.active ='1' and story.deactive_by_admin ='0'", nativeQuery = true)
    Page<Story> findAllByStoryIds(@Param("storyIds") List<Integer> storyIds, Pageable pageable);

    @Query(value = "SELECT * FROM story s where s.user_id = ?1 order by s.created_at desc", nativeQuery = true)
    List<Story> findAllByUserId(int userId);

   //ta: use for get comment
    @Query("select s.id from Story s where s.userId = ?1 and s.active = true and s.deactiveByAdmin=false")
    List<Integer> getAllStoryIdByUserId(int userId);


}
