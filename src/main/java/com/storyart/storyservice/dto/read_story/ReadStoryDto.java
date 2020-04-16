package com.storyart.storyservice.dto.read_story;

import com.storyart.storyservice.model.Information;
import com.storyart.storyservice.model.InformationAction;
import com.storyart.storyservice.model.Screen;
import com.storyart.storyservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStoryDto {
    private int id;
    private String title;
    private int userId;
    private double avgRate;
    private String intro;
    private String animation;
    private String image;
    private boolean published;
    private boolean active;
    private boolean deactiveByAdmin;
    private String firstScreenId;

    private User user;
    private List<ReadStoryScreenDto> screens;
    private List<ReadStoryTagDto> tags;
    private List<ReadStoryInformationDto> informations;
    private List<InformationAction> informationActions;
}
