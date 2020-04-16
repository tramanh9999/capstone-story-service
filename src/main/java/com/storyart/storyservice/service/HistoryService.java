package com.storyart.storyservice.service;

import com.github.javafaker.Faker;
import com.storyart.storyservice.dto.create_reading_history.ClickLinkDto;
import com.storyart.storyservice.dto.create_reading_history.ReadingHistoryDto;
import com.storyart.storyservice.dto.ResultDto;
import com.storyart.storyservice.dto.create_reading_history.ScreenReadTimeDto;
import com.storyart.storyservice.dto.history.ReadingHistoryResponse;
import com.storyart.storyservice.dto.story_suggestion.HistoryDTO;
import com.storyart.storyservice.model.ClickLink;
import com.storyart.storyservice.model.ReadingHistory;
import com.storyart.storyservice.model.ScreenReadingTime;
import com.storyart.storyservice.model.Story;
import com.storyart.storyservice.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.storyart.storyservice.repository.HistoryRepository;
import com.storyart.storyservice.repository.StoryRepository;
import com.storyart.storyservice.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

public interface HistoryService {
 //   List<Integer> jaccardCalculate(Integer id);
    void createTempHistory();
    ResultDto saveReadHistory(ReadingHistoryDto readingHistoryDto, int userId);
    ResultDto saveClickLink(ClickLinkDto clickLink);
    ResultDto saveScreenReadTime(ScreenReadTimeDto screenReadTimeDto);

    List<Integer> findHitpointListByRange(Integer sid, String start, String end);
    Page<ReadingHistoryResponse> getReadingHistory(int userId, int pageNo, int pageSize);

    List<Integer> findViewListByRange(int sid, String start, String end);
}

@Service
class HistoryServiceIml implements HistoryService {


    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    TagService tagService;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ScreenReadingTimeRepository screenReadingTimeRepository;

    @Autowired
    ClickLinkRepository clickLinkRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResultDto saveClickLink(ClickLinkDto clickLink) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        Story story = storyRepository.findById(clickLink.getStoryId()).orElse(null);
        if(story == null){
            result.getErrors().put("NOT_FOUND", "Truyện này không tồn tại");
        } else if(!story.isActive() ||  story.isDeactiveByAdmin()){
            result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa");
        } else {
            ClickLink saved = modelMapper.map(clickLink, ClickLink.class);
            saved = clickLinkRepository.save(saved);
            result.setSuccess(true);
            result.setData(saved);
        }
        return result;
    }

    @Override
    public ResultDto saveScreenReadTime(ScreenReadTimeDto screenReadTimeDto) {
        ResultDto result = new ResultDto();
        System.out.println(screenReadTimeDto.getDuration());
        ScreenReadingTime screenReadingTime = modelMapper.map(screenReadTimeDto, ScreenReadingTime.class);

        screenReadingTime = screenReadingTimeRepository.save(screenReadingTime);
        result.setSuccess(true);
        result.setData(screenReadingTime);
        return result;
    }
/*
    @Override
    public   List<Integer> jaccardCalculate(Integer id) {
        List<ReadingHistory> CurrentUserHistory = historyRepository.findHistoryByIdOnly(id);
        HistoryDTO currentUserH = new HistoryDTO();
        currentUserH.setUserid(id);
        List<Integer> listCurr = new ArrayList<>();
        for (int i = 0; i < CurrentUserHistory.size(); i++) {
            listCurr.add(CurrentUserHistory.get(i).getStoryId());
        }
        currentUserH.setListStory(listCurr);

        List<HistoryDTO> listHistory = new ArrayList<>();

        List<Integer> check = historyRepository.findUserIdHistoryExceptId(id);
        // get list history except current
        List<ReadingHistory> byId = historyRepository.findHistoryById(check);

        for (Integer integer : check){
            HistoryDTO dto = new HistoryDTO();
            List<Integer> list = new ArrayList<>();
          for(int i = 0; i < byId.size(); i++){
              if(integer.equals(byId.get(i).getUserId())){
                  dto.setUserid(byId.get(integer).getUserId());
                  list.add(byId.get(i).getStoryId());
              }
          }
            dto.setListStory(list);
            listHistory.add(dto);
        }


        // count similarity
        Double MostFit = 0.0;
        Integer MostFitId = 0;
        for (int j = 0; j < listHistory.size(); j++) {
            Double jaccard = calculate(currentUserH.getListStory(), listHistory.get(j).getListStory());
            if (jaccard >= MostFit) {
                MostFit = jaccard;
                MostFitId = listHistory.get(j).getUserid();
            }

        }

        // find story current does not read
        List<Integer> MostFitHistory = historyRepository.findListHistory(MostFitId);
        MostFitHistory.removeAll(currentUserH.getListStory());

        return MostFitHistory;
    }*/

    @Override
    public void createTempHistory() {

        Faker faker = new Faker();
        for(int i = 0; i < 200; i++){
            ReadingHistory rh = new ReadingHistory();
            rh.setStoryId(faker.number().numberBetween(1, 300));
            historyRepository.save(rh);
        }

    }

    @Override
    public ResultDto saveReadHistory(ReadingHistoryDto readingHistoryDto, int userId) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        Story story = storyRepository.findById(readingHistoryDto.getStoryId()).orElse(null);
        if(story == null){
            result.getErrors().put("NOT_FOUND", "Truyện này không tồn tại");
        } else if(!story.isActive() ||  story.isDeactiveByAdmin()){
            result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa");
        } else {
            ReadingHistory rh = modelMapper.map(readingHistoryDto, ReadingHistory.class);
            rh.setUserId(userId);
            rh = historyRepository.save(rh);
            int id = rh.getId();
            storyRepository.save(story);

            result.setSuccess(true);
            result.setData(rh);
        }
        return result;
    }

/*
    public Double calculate(List<Integer> curUser, List<Integer> nextUser) {
        List<Integer> intersect = new ArrayList<>();
        for (Integer i : curUser) {
            if (nextUser.contains(i)) {
                intersect.add(i);
            }
        }

        List<Integer> union = new ArrayList<>();

        union.addAll(curUser);
        union.addAll(nextUser);

        return Double.valueOf(intersect.size()) / Double.valueOf(union.size() - intersect.size());

    }*/  @Override
public List<Integer> findViewListByRange(int sid, String start, String end) {

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
    List<ReadingHistory> hitPoints = historyRepository
            .findAllByStoryIdAndCreatedAtBetweenOrderByCreatedAtDesc(sid, startDate, endDate);
    List<Integer> integerList = new ArrayList<>();
    int totalDay = (int) ChronoUnit.DAYS.between(calendarStart.toInstant(), calendarEnd.toInstant());
    for (int i = 0; i <= totalDay; i++) {
        Date dateTemp = calendarStart.getTime();
        int countClickByDay = 0;
        for (ReadingHistory hitPoint : hitPoints) {
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
        integerList.add(countClickByDay);
        calendarStart.add(Calendar.DAY_OF_MONTH, 1);
    }
    return integerList;
}

    @Override
    public List<Integer> findHitpointListByRange(Integer sid,String start, String  end) {

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
        List<ReadingHistory> hitPoints = historyRepository
                .findAllByStoryIdAndIsReachingEndAndCreatedAtBetweenOrderByCreatedAtDesc(sid,true,  startDate, endDate);
        List<Integer> integerList = new ArrayList<>();
        int totalDay = (int) ChronoUnit.DAYS.between(calendarStart.toInstant(), calendarEnd.toInstant());
        for (int i = 0; i <= totalDay; i++) {
            Date dateTemp = calendarStart.getTime();
            int countClickByDay = 0;
            for (ReadingHistory hitPoint : hitPoints) {
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
            integerList.add(countClickByDay);
            calendarStart.add(Calendar.DAY_OF_MONTH, 1);
        }
        return integerList;
    }

    @Override
    public Page<ReadingHistoryResponse> getReadingHistory(int userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<ReadingHistory> historyPage = historyRepository.findAllWithUserId(userId, pageable);

        Page<ReadingHistoryResponse> responsePage = historyPage.map(new Function<ReadingHistory, ReadingHistoryResponse>() {
            @Override
            public ReadingHistoryResponse apply(ReadingHistory readingHistory) {
                ModelMapper mm = new ModelMapper();
                ReadingHistoryResponse readingHistoryResponse =mm.map(readingHistory, ReadingHistoryResponse.class);
                return  readingHistoryResponse;
            }
        });

        List<ReadingHistoryResponse> responseList = responsePage.getContent();

        List<Integer> storyIds = new ArrayList<>();

        for (ReadingHistoryResponse response: responseList) {
            storyIds.add(response.getStoryId());
        }

        List<Story> stories = storyRepository.findAllById(storyIds);

        for (ReadingHistoryResponse response: responseList) {
            for (Story story: stories) {
                System.out.println(response.getStoryId());
                System.out.println(story.getId());
                int sid = story.getId();
                int rsid = response.getStoryId();
                if(sid == rsid){
                    response.setStoryName(story.getTitle());
                    response.setStoryContent(story.getIntro());
                    response.setStoryImageUrl(story.getImage());
                }
            }
        }

        return responsePage;
    }

}