package com.storyart.storyservice.dto.statistic;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryReactByRange {
List<String> times= new ArrayList<>();
//    private int numOfShare;
//    private int numOfHitPoint;
//    private int numOfRead;
private List<Integer> numOfComment= new ArrayList<>();
    private List<Integer> numOfView = new ArrayList<>();
    private List<Integer> numOfHitPoint= new ArrayList<>();
    private List<Integer> numOfClickLink= new ArrayList<>();
//    private int numOfClickLink;

//    {"26/04", "27/04"}
//    {21, 23}
}

