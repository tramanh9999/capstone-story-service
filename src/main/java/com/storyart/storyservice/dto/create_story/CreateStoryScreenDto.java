package com.storyart.storyservice.dto.create_story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoryScreenDto{
    @NotBlank(message = "Màn hình phải có id")
    private String id;

    private int storyId;

    @NotBlank(message = "Chưa có tiêu đề cho màn hình")
    @Size(max = 100, message = "Tiêu đề màn hình chỉ có tối đa 100 kí tự")
    private String title;

    @NotBlank(message = "Chưa có nội dung màn hình")
    @Size(min = 10, message = "Nội dung màn hình phải có tối thiểu 10 kí tự")
    private String content;

    private int myIndex;

    @Valid
    List<CreateStoryActionDto> actions;
}
