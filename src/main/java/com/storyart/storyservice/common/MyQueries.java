package com.storyart.storyservice.common;

public class MyQueries {
    public static final String countStoriesByKeyword = "select count(s.id) from story s WHERE (s.title like %?1% or s.intro like %?1%)";
    public static final String getStoriesByKeyword = "select * from story s " +
            "WHERE (s.title like %?1% or s.intro like %?1% " +
            "or (select name from user u where u.id = s.user_id) like %?1%)";
    public static final String getStoriesByUser = "select * from story s " +
            "WHERE s.user_id = ?1 and s.active = true and s.deactive_by_admin = false " +
            "and s.title like %?2%";

    public static final String orderByDate = "order by s.created_at";
    public static final String orderByComment = "order by (select count(c.id) from comment c where c.story_id = s.id)";
    public static final String orderByRating = "order by (select count(*) from rating r where r.story_id = s.id)";
    public static final String orderByRead = "order by (select count(*) from reading_history rd where rd.story_id = s.id)";
    public static final String orderByAvgRate = "order by s.avg_rate";
    public static final String orderByScreen = "order by (select count(sc.id) from screen sc where sc.story_id = s.id)";

    public static final String getStoriesForAdminOrderByDate = getStoriesByKeyword + " " + orderByDate;
    public static final String getStoriesForAdminOrderByComment = getStoriesByKeyword + " " + orderByComment;
    public static final String getStoriesForAdminOrderByRating = getStoriesByKeyword + " " + orderByRating;
    public static final String getStoriesForAdminOrderByRead = getStoriesByKeyword + " " + orderByRead;
    public static final String getStoriesForAdminOrderByAvgRate = getStoriesByKeyword + " " + orderByAvgRate;
    public static final String getStoriesForAdminOrderByScreen = getStoriesByKeyword + " " + orderByScreen;

    public static final String getStoriesForUserOrderByDate = getStoriesByUser + " " + orderByDate;
    public static final String getStoriesForUserOrderByComment = getStoriesByUser + " " + orderByComment;
    public static final String getStoriesForUserOrderByRating = getStoriesByUser + " " + orderByRating;
    public static final String getStoriesForUserOrderByRead = getStoriesByUser + " " + orderByRead;
    public static final String getStoriesForUserOrderByAvgRate = getStoriesByUser + " " + orderByAvgRate;
    public static final String getStoriesForUserOrderByScreen = getStoriesByUser + " " + orderByScreen;
}
