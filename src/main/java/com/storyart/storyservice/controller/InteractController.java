package com.storyart.storyservice.controller;

import com.storyart.storyservice.dto.LinkAndSidDTO;
import com.storyart.storyservice.dto.statistic.ScreenTimeResponse;
import com.storyart.storyservice.dto.statistic.TimeRangeRequest;
import com.storyart.storyservice.service.LinkClickService;
import com.storyart.storyservice.service.ScreenReadingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interact")
@CrossOrigin
public class InteractController {



    @Autowired
    LinkClickService linkClickService;



    @PostMapping("/clicklink")
    public ResponseEntity setClick(@RequestBody LinkAndSidDTO c){
        linkClickService.save(c);
        return new ResponseEntity("Đã lưu kết quả", HttpStatus.OK);
    }

@Autowired
    ScreenReadingTimeService screenReadingTimeService;

    @PostMapping("/getScreenTime/{sid}")
    public ResponseEntity setClick(@PathVariable("sid") Integer sid,@RequestBody TimeRangeRequest c){
        List<ScreenTimeResponse> screenTimeResponses= screenReadingTimeService.getListDurationOfEachSreenInTimeRangeByStoryId(sid, c.getStart(), c.getEnd());
        return new ResponseEntity(screenTimeResponses, HttpStatus.OK);
    }









}
