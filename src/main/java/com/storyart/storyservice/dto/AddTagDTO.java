package com.storyart.storyservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTagDTO {
    private int id;
    private String title;
    private boolean isActive;
    private boolean isUpdate;
}
