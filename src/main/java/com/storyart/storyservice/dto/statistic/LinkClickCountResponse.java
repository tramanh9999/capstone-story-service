package com.storyart.storyservice.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkClickCountResponse implements Comparable<LinkClickCountResponse>{

    String link;
    int count;

    @Override
    public int compareTo(LinkClickCountResponse linkClickCountResponse) {
        return Integer.compare(this.getCount(),(linkClickCountResponse.getCount()));
    }
}
