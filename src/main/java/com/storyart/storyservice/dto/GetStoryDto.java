package com.storyart.storyservice.dto;

import com.storyart.storyservice.model.Rating;
import com.storyart.storyservice.model.Tag;
import com.storyart.storyservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoryDto {
    private Integer id;
    private String title;
    private String intro;
    private String image;
    private float avgRate;
    private Date createdAt;
    private Date updatedAt;
    private boolean deactiveByAdmin;
    private boolean active;
    private boolean published;
    private int authorId;
    private int numOfComment;
    private int numOfScreen;
    private int numOfRate;
    private int numOfRead;

    private User user;
    private Rating rating;
    List<TagDto> tags;
}
