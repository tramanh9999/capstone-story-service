package com.storyart.storyservice.service;

import com.storyart.storyservice.dto.statistic.NumberOfCommentByDate;
import com.storyart.storyservice.dto.statistic.StatisticResponse;
import com.storyart.storyservice.model.Comment;
import com.storyart.storyservice.repository.CommentRepository;
import com.storyart.storyservice.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

//@FeignClient(name="api-gateway")
public interface CommentMicroService {
//    @GetMapping(value = "comment-service/api/v1/comment/public/getCommentStatistic")
//    StatisticResponse getCommentListResponce(@RequestParam(defaultValue = "0") @Valid Integer storyId,
//                                             @RequestParam(defaultValue = "0") @Valid Integer userId,
//                                             @RequestParam(defaultValue = "0") @Valid String start,
//                                             @RequestParam(defaultValue = "0") @Valid String end);

    public StatisticResponse  getCommentListResponce(int storyId,int userId, String start, String end);





}



@Service
class CommentServiceImpl implements  CommentMicroService{


    @Autowired
    StoryRepository storyRepository;
    @Autowired
    CommentRepository commentRepository;

    @Override
    public StatisticResponse getCommentListResponce(int storyId,int userId, String start, String end) {
        List<Integer> storyIds = new ArrayList<>();
        if(storyId == 0){
            storyIds = storyRepository.getAllStoryIdByUserId(userId);
        }
        else {
            storyIds.add(storyId);
        }

        Date startDate, endDate;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarStart= Calendar.getInstance();

        try {
            //
            startDate = formatter.parse(start);
            calendarStart.setTime(startDate);
            //
            endDate = formatter.parse(end);
            calendarEnd.setTime(endDate);
            calendarEnd.set(Calendar.HOUR, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            endDate = calendarEnd.getTime();
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sai định dạng ngày.");
        }

        StatisticResponse response = new StatisticResponse();



        List<Comment> comments = commentRepository.findAllByStoryIdInAndActiveAndDisableByAdminAndCreatedAtBetweenOrderByCreatedAtDesc(storyIds,true, false, startDate, endDate);
        response.setTotal(comments.size());
        List<NumberOfCommentByDate> numberOfCommentByDates = new ArrayList<>();

        int totalDay = (int) ChronoUnit.DAYS.between(calendarStart.toInstant(), calendarEnd.toInstant());

        for(int i = 0; i<= totalDay; i++){
            Date dateTemp = calendarStart.getTime();
            NumberOfCommentByDate numberOfCommentByDate = new NumberOfCommentByDate();
            numberOfCommentByDate.setDate(dateTemp);
            numberOfCommentByDate.setNumberOfComment(0);
            for (Comment comment: comments) {
                Date date = new Date(comment.getCreatedAt().getTime());
                String dateString = formatter.format(date);
                try {
                    date = formatter.parse(dateString);
                } catch (ParseException e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Có lỗi xảy ra khi tính toán.");
                }
                if(date.compareTo(dateTemp) ==0){
                    numberOfCommentByDate.setNumberOfComment(numberOfCommentByDate.getNumberOfComment()+1);
                }
            }
            int dayOfMonth = calendarStart.get(Calendar.DAY_OF_MONTH);
            calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth+1);
            numberOfCommentByDates.add(numberOfCommentByDate);
        }
        response.setNumberOfCommentByDates(numberOfCommentByDates);

        return response;
    }
}



