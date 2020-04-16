package com.storyart.storyservice.service;

import com.storyart.storyservice.dto.statistic.ScreenTimeResponse;
import com.storyart.storyservice.model.Screen;
import com.storyart.storyservice.repository.ScreenReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ScreenReadingTimeService {

//
//    Integer countDurationInTimeRange(Integer screenId, String start, String end);

    List<ScreenTimeResponse> getListDurationOfEachSreenInTimeRangeByStoryId(Integer storyId, String start, String end);
}

@Service
class ScreenReadingTimeImpl implements ScreenReadingTimeService {


    @Autowired
    ScreenReadingRepository screenReadingRepository;

//    @Override
//    public Integer countDurationInTimeRange(Integer screenId, String start, String end) {
//        // tim thoe man hinh id va khoang thoi gian
//        return screenReadingRepository.countDurationByScreenIdAndCreatedAtBetween(screenId, start, end);
//    }

    @Autowired
    EntityManager entityManager;

    @Autowired
    ScreenService screenService;
    @Override
    public List<ScreenTimeResponse> getListDurationOfEachSreenInTimeRangeByStoryId(Integer sid, String startDate, String endDate) {
        List<Screen> screensByStoryId = screenService.getScreensByStoryId(sid);

        List<ScreenTimeResponse> list = new ArrayList<>();
        Query query = entityManager.createNativeQuery("select sum(duration) from storyart_db.screen_reading_time where screen_id= :screenId and (created_at between :startDate and :endDate)");
        for (Screen screen : screensByStoryId) {
            String screenId = screen.getId();
            query.setParameter("screenId", screenId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
// ben t cung mat r , de tim lai troing history
            Object singleResult = query.getSingleResult();
            long sumtime=0;
            if(singleResult!= null){
              sumtime=  ((Number) singleResult).longValue();
            }
//            Integer sumtime = countDurationInTimeRange(screenId, startDate, endDate);
            ScreenTimeResponse screenTimeResponse = new ScreenTimeResponse();
            screenTimeResponse.setId(screenId);
            screenTimeResponse.setContent(screen.getContent());
            screenTimeResponse.setTitle(screen.getTitle());
            screenTimeResponse.setStoryId(sid);
            screenTimeResponse.setSumtime(sumtime);
            //add
            list.add(screenTimeResponse);
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }
}
