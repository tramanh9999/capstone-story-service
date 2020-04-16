package com.storyart.storyservice.service;

import com.storyart.storyservice.dto.statistic.*;
import com.storyart.storyservice.model.Rating;
import com.storyart.storyservice.model.Story;
import com.storyart.storyservice.model.Tag;
import com.storyart.storyservice.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public interface StoryStatisticService {

    boolean checkOwner(Integer id, Integer sid);

    StorySummarizeResponse getStorySummarizeResponse(int sid);

    StoryReactByRange getReactStatisticInTimeRange(int sid, TimeRangeRequest timeRangeRequest);

    List<IRatingClassify> getRatingClassify(int sid);

    Rating getRatingByStoryAndUser(int storyId, int userId);

}

@Service
class StoryStatisticServiceImpl implements StoryStatisticService {


    @Autowired
    StoryRepository storyRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagService tagService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager entityManager;

    @Override
    public boolean checkOwner(Integer id, Integer sid) {
        Optional<Story> byId = storyRepository.findById(sid);
        if (byId.isPresent() && byId.get().getUserId() == id) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public StorySummarizeResponse getStorySummarizeResponse(int sid) {
        Story story = storyRepository.findById(sid).orElse(null);
        if (story != null) {
            StorySummarizeResponse storySummarizeResponse = modelMapper.map(story, StorySummarizeResponse.class);
            storySummarizeResponse.setNumOfRead(historyRepository.countAllByStoryId(sid));

            List<Tag> tagList = tagRepository.findAllByStoryId(story.getId());
            storySummarizeResponse.setTags(tagService.mapModelToDto(tagList));
            storySummarizeResponse.setNumOfComment(commentRepository.countCommentByStoryId(story.getId()));
            storySummarizeResponse.setNumOfRate(ratingRepository.countRatingByStoryId(story.getId()));

            Query query1 = entityManager.createNativeQuery("SELECT count(*) FROM storyart_db.click_link where story_id= :storyId");
            Query query2 = entityManager.createNativeQuery("SELECT count(*) FROM storyart_db.reading_history where story_id= :storyId and  is_reaching_end=1");
            Query query3 = entityManager.createNativeQuery("SELECT count(*) FROM storyart_db.screen   where story_id= :storyId");

            query1.setParameter("storyId", sid);
            query2.setParameter("storyId", sid);
            query3.setParameter("storyId", sid);

            int clicklinkCount = ((Number) query1.getSingleResult()).intValue();
            int hitpointCount = ((Number) query2.getSingleResult()).intValue();
            int screenNumber = ((Number) query3.getSingleResult()).intValue();


            //todo:xoa dong nay
            storySummarizeResponse.setNumOfHitPoint(hitpointCount);
            storySummarizeResponse.setNumOfClickLink(clicklinkCount);
            storySummarizeResponse.setNumOfScreen(screenNumber);
            storySummarizeResponse.setNumOfShare(new Random().nextInt(300));


            return storySummarizeResponse;

        }
        return null;
    }

    @Autowired
    CommentMicroService commentMicroService;
    @Autowired
    LinkClickService clickService;
    @Autowired
    HistoryService historyService;
    StoryReactByRange storyReactByRange = new StoryReactByRange();



    @Override
    public StoryReactByRange getReactStatisticInTimeRange(int sid, TimeRangeRequest timeRangeRequest) {
        List<NumberOfCommentByDate> commentByDay = commentMicroService.getCommentListResponce(sid, 0, timeRangeRequest.getStart(), timeRangeRequest.getEnd()).getNumberOfCommentByDates();

        List<Integer> viewByDay = historyService.findViewListByRange(sid, timeRangeRequest.getStart(), timeRangeRequest.getEnd());
        List<Integer> hitPointByDay = new ArrayList<>();
        List<Integer> clickLinkByDay = clickService.findClickLinkRange
                (sid, timeRangeRequest.getStart(), timeRangeRequest.getEnd());



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = simpleDateFormat.parse(timeRangeRequest.getStart());
            Date endDate = simpleDateFormat.parse(timeRangeRequest.getEnd());

            hitPointByDay = historyService.findHitpointListByRange(sid, timeRangeRequest.getStart(), timeRangeRequest.getEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        StoryReactByRange storyReactByRange = new StoryReactByRange();
        storyReactByRange.setNumOfClickLink(clickLinkByDay);
        storyReactByRange.setNumOfView(viewByDay);
        storyReactByRange.setNumOfHitPoint(hitPointByDay);
        for (NumberOfCommentByDate numberOfCommentByDate : commentByDay) {
            Integer comment = numberOfCommentByDate.getNumberOfComment();
            storyReactByRange.getNumOfComment().add(comment);
//            storyReactByRange.getNumOfComment().add(new Random().nextInt(50));
            Calendar cal = Calendar.getInstance();
            cal.setTime(numberOfCommentByDate.getDate());
            String time = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1);
            storyReactByRange.getTimes().add(time);
            //click
        }

//todo: xoa dong nay di
        this.storyReactByRange = storyReactByRange;

        return storyReactByRange;

    }

    @Override
    public List<IRatingClassify> getRatingClassify(int sid) {
        return ratingRepository.countStarByStoryId(sid);

    }

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public Rating getRatingByStoryAndUser(int storyId, int userId) {
        Rating rating = ratingRepository.findById(storyId, userId);
        return rating;
    }
}