package com.storyart.storyservice.service;

import com.storyart.storyservice.dto.LinkAndSidDTO;
import com.storyart.storyservice.dto.statistic.ILinkClickCountResponse;
import com.storyart.storyservice.dto.statistic.LinkClickCountResponse;
import com.storyart.storyservice.model.ClickLink;
import com.storyart.storyservice.repository.ClickLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

public interface LinkClickService {
    List<Integer> findClickLinkRange(Integer storyId, String start, String end);

    void save(LinkAndSidDTO linkAndSidDTO);

    List<ILinkClickCountResponse> countLinkClickByStoryIdInTimeRange(int storyId, String start, String end);

}


@Service
class LinkClickServiceImpl implements LinkClickService {

    @Autowired
    ClickLinkRepository clickLinkRepository;

    public List<Integer> findClickLinkRange(Integer storyId, String start, String end) {

        Date startDate, endDate;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarStart = Calendar.getInstance();

        try {
            startDate = formatter.parse(start);
            calendarStart.setTime(startDate);
            endDate = formatter.parse(end);
            calendarEnd.setTime(endDate);
            calendarEnd.set(Calendar.HOUR, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            endDate = calendarEnd.getTime();
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sai định dạng ngày.");
        }


        List<ClickLink> hitPoints = clickLinkRepository.findByStoryIdAndCreatedAtBetweenOrderByCreatedAtDesc(storyId, startDate, endDate);


        List<Integer> integerList = new ArrayList<>();

        int totalDay = (int) ChronoUnit.DAYS.between(calendarStart.toInstant(), calendarEnd.toInstant());

        for (int i = 0; i <= totalDay; i++) {


            Date dateTemp = calendarStart.getTime();


            int countClickByDay = 0;
            for (ClickLink hitPoint : hitPoints) {
                //getDate of hit point
                Date date = new Date(hitPoint.getCreatedAt().getTime());
                String dateString = formatter.format(date);
                //23/02/2020
                try {
                    date = formatter.parse(dateString);
                } catch (ParseException e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Có lỗi xảy ra khi tính toán.");
                }

//                so sanh ngay cua 2 list rong va co
                if (date.compareTo(dateTemp) == 0) {
                    countClickByDay++;
                }
            }
            integerList.add( countClickByDay);
            calendarStart.add(Calendar.DAY_OF_MONTH, 1);



        }


        return integerList;
    }

    @Override
    public void save(LinkAndSidDTO c) {
        ClickLink p= new ClickLink ();
        p.setStoryId(c.getSid());
        p.setLink(c.getLink());
        clickLinkRepository.save(p);
    }

    @Override
    public List<ILinkClickCountResponse> countLinkClickByStoryIdInTimeRange(int storyId, String start, String end) {

        List<ILinkClickCountResponse> iLinkClickCountResponses = clickLinkRepository.countLinkClickNumberByLinkByStoryIdInTimeRange(storyId, start, end);
    return iLinkClickCountResponses;

    }


}



