package com.storyart.storyservice.dto.create_story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoryInformationActionDto {
    private String actionId;
    private String informationId;

    @NotBlank(message = "Ảnh hưởng thông tin không được để trống")
    private String operation;
    private String value;
}
