package com.storyart.storyservice.controller;

import com.netflix.discovery.converters.Auto;
import com.storyart.storyservice.dto.ResultDto;
import com.storyart.storyservice.dto.statistic.*;
import com.storyart.storyservice.repository.UserRepository;
import com.storyart.storyservice.security.CurrentUser;
import com.storyart.storyservice.security.UserPrincipal;
import com.storyart.storyservice.service.LinkClickService;
import com.storyart.storyservice.service.ScreenReadingTimeService;
import com.storyart.storyservice.service.StoryService;
import com.storyart.storyservice.service.StoryStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {

    @Autowired
    StoryService storyService;

    @GetMapping("get_read_statistics_of_user")
    public ResponseEntity getReadStatisticsOfUser(@RequestParam Date from,
                                                  @RequestParam Date to,
                                                  @CurrentUser UserPrincipal user){
        ResultDto result = storyService.getReadStatisticsByDateRangeOfUser(from, to, user.getId());
        return new ResponseEntity(result, HttpStatus.OK);
    }


    //số liệu từ khi xuất bản câu truyện lần đầu (cũng bao gồm 1 số thông tin khác)
    @GetMapping("story/{sid}/summary")
    public ResponseEntity getStorySummary(@PathVariable int sid){
        StorySummarizeResponse storySummarizeResponse= storyStatisticService.getStorySummarizeResponse(sid);
        return new ResponseEntity(storySummarizeResponse, HttpStatus.OK);
    }

@Autowired
    StoryStatisticService storyStatisticService;
    @Autowired
    ScreenReadingTimeService screenReadingTimeService;
    // tam thoi de null
    @GetMapping("story/{sid}/screen")
    public ResponseEntity getStoryScreenStatistic(@PathVariable int sid,@RequestBody TimeRangeRequest timeRangeRequest){
        List<ScreenTimeResponse> storySummarizeResponse= screenReadingTimeService.getListDurationOfEachSreenInTimeRangeByStoryId(sid, timeRangeRequest.getStart(), timeRangeRequest.getEnd());

        return new ResponseEntity(storySummarizeResponse, HttpStatus.OK);
    }


    // bao gồm số liệu cho biểu đồ đường của rating theo khung thời gian
    @GetMapping("story/{sid}/rating")
    public ResponseEntity getRating_Statistic(@PathVariable int sid){
        List<IRatingClassify> ratingClassifyList= storyStatisticService.getRatingClassify(sid);
        return new ResponseEntity(ratingClassifyList, HttpStatus.OK);
    }

    // bao gồm số liệu cho biểu đồ đường của (share, comment(cái
    // này gọi qua gw sang comment sv của a đạt))
    @PostMapping("story/{sid}/react")
    public ResponseEntity getReacts_Statistic(@PathVariable int sid, @RequestBody TimeRangeRequest timeRangeRequest){
        StoryReactByRange storyReactByRanges= storyStatisticService.getReactStatisticInTimeRange(sid,timeRangeRequest );
        return new ResponseEntity(storyReactByRanges, HttpStatus.OK);
    }

    @Autowired
    LinkClickService linkClickService;
    @PostMapping("story/{sid}/link-click")
    public ResponseEntity countLinkClickByStoryInTimeRange(@PathVariable int sid, @RequestBody TimeRangeRequest timeRangeRequest){
        List<ILinkClickCountResponse> iLinkClickCountResponses = linkClickService.countLinkClickByStoryIdInTimeRange(sid, timeRangeRequest.getStart(), timeRangeRequest.getEnd());
        return new ResponseEntity(iLinkClickCountResponses, HttpStatus.OK);
    }




    @PostMapping("/story/{sid}/screen-time")
    public ResponseEntity getScreenTimeByStory(@PathVariable("sid") Integer sid, @RequestBody TimeRangeRequest c) {
        List<ScreenTimeResponse> screenTimeResponses= screenReadingTimeService.getListDurationOfEachSreenInTimeRangeByStoryId(sid, c.getStart(), c.getEnd());
        return new ResponseEntity(screenTimeResponses, HttpStatus.OK);
    }

    @GetMapping("/story/{sid}/check")
    public boolean checkBeforeComeToStatic(@PathVariable("sid") Integer sid, @CurrentUser UserPrincipal userPrincipal) {
            return  storyStatisticService.checkOwner(userPrincipal.getId() ,sid );

    }
}
