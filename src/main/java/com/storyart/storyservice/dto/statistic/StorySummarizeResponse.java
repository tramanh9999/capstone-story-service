package com.storyart.storyservice.dto.statistic;

import com.storyart.storyservice.dto.TagDto;
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

/*Dùng cho thống kê thông tin react (like comment, read, share(chưa có) và 1 số thông tin cơ bản)*/
public class StorySummarizeResponse {

    //thông tin react
    private int numOfComment;
    private int numOfRead;
    private int numOfShare;
    private int numOfHitPoint;
    private int numOfClickLink;

    //thông tin cơ bản
    private int numOfScreen;
    private int numOfRate;
    private Integer id;
    private String title;
    private String image;
    private float avgRate;
    private Date createdAt;
    private Date updatedAt;
    private boolean deactiveByAdmin;
    private boolean active;
    private boolean published;
    private int authorId;
    private String intro;
    List<TagDto> tags;



    User user;
}
