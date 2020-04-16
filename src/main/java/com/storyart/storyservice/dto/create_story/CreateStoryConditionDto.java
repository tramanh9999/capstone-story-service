package com.storyart.storyservice.dto.create_story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoryConditionDto{
    private String id;

    @NotBlank(message = "Điều kiện thông tin không được để trống")
    private String type; //<, >

    @NotBlank(message = "Điều kiện thông tin không được để trống")
    private String value;

    @NotBlank(message = "Màn hình mặc định đi tới không được để trống")
    private String nextScreenId;
}
