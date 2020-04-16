package com.storyart.storyservice.service;

import com.storyart.storyservice.model.Screen;
import com.storyart.storyservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ScreenService {
   List<Screen> getScreensByStoryId(int storyId);
}


@Service
class ScreenServiceImpl implements ScreenService {

    @Autowired
    ScreenRepository screenRepository;

    @Override
    public List<Screen> getScreensByStoryId(int storyId) {
        return screenRepository.findByStoryId(storyId);
    }
}
