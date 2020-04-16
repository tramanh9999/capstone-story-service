package com.storyart.storyservice.controller;

import com.storyart.storyservice.dto.ResultDto;
import com.storyart.storyservice.dto.create_reading_history.ClickLinkDto;
import com.storyart.storyservice.dto.create_reading_history.ReadingHistoryDto;
import com.storyart.storyservice.dto.create_reading_history.ScreenReadTimeDto;
import com.storyart.storyservice.dto.history.ReadingHistoryResponse;
import com.storyart.storyservice.security.CurrentUser;
import com.storyart.storyservice.security.UserPrincipal;
import com.storyart.storyservice.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reading_history")
@CrossOrigin
public class ReadingHistoryController {
    @Autowired
    HistoryService historyService;

    @PostMapping("")
    public ResponseEntity saveReadingHistory(@RequestBody ReadingHistoryDto readingHistoryDto, @CurrentUser UserPrincipal user){
        System.out.println("end: " + readingHistoryDto.isReachingEnd());
        ResultDto result = historyService.saveReadHistory(readingHistoryDto, user.getId());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("public")
    public ResponseEntity saveReadingHistoryForGuest(@RequestBody ReadingHistoryDto readingHistoryDto){
        System.out.println("end: " + readingHistoryDto.isReachingEnd());
        ResultDto result = historyService.saveReadHistory(readingHistoryDto, 0);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("public/clicklink")
    public ResponseEntity saveClickLink(@RequestBody ClickLinkDto clickLink){
        ResultDto result = historyService.saveClickLink(clickLink);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("public/screen_read_time")
    public ResponseEntity saveScreenReadTime(@RequestBody ScreenReadTimeDto screenReadTimeDto){
        ResultDto result = historyService.saveScreenReadTime(screenReadTimeDto);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("getReadingHistory")
    public Page<ReadingHistoryResponse> getReadingHistory(
            @RequestParam(defaultValue = "0") Integer userId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize){
        pageNo = pageNo -1;
        if(pageNo<0){
            pageNo = 0;
        }
        return historyService.getReadingHistory(userId, pageNo, pageSize);
    }

}
