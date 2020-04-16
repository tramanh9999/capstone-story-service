package com.storyart.storyservice.model.ids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RatingId implements Serializable {
    private int userId;
    private int storyId;
}
