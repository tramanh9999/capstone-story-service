package com.storyart.storyservice.dto.create_story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoryInformationDto{
    @NotBlank(message = "Chưa có id cho thông tin")
//    @Size(max = 10, message = "Nội dung màn hình có tối đa 100 kí tự")
    private String id;
    private int storyId;

    @Pattern(regexp = "NUMBER|STRING", message = "Kiểu thông tin chỉ được NUMBER|STRING")
    private String type; //NUMBER

    @NotBlank(message = "Tên thông tin không được để trống")
    @Size(max = 100, message = "Tên thông tin có tối đa 100 kí tự")
    private String name;

    @NotBlank(message = "Giá trị thông tin không được để trống")
    private String value;
    List<CreateStoryConditionDto> conditions;
}
