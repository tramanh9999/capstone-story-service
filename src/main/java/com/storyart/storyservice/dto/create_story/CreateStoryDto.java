package com.storyart.storyservice.dto.create_story;

import com.storyart.storyservice.common.constants.ANIMATION_TYPES;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoryDto implements Serializable {

    private int id;

    @NonNull
    @Size(min = 5, max = 250, message = "Tiêu đề truyện phải có ít nhất 5 kí tự và không quá 250 kí tự")
    private String title;

    private int userId;

    @Size(min = 10, message = "Nội dung giói thiệu truyện phải ít nhất 10 kí tự")
    private String intro;

    @Size(max = 100)
    @Pattern(regexp = "FADE|SLIDE|GROW|COLLAPSE|ZOOM", message = "Chỉ được chọn 1 trong các animation FADE|SLIDE|GROW|COLLAPSE|ZOOM")
    private String animation;

    @Size(max = 1000, message = "Ảnh không quá 1000 kí tự")
    private String image;

    @NotBlank(message = "Chưa có màn hình đầu tiên")
    private String firstScreenId;

    private boolean published;

    @Valid
    private List<CreateStoryScreenDto> screens;

    @Valid
    private List<CreateStoryInformationDto> informations;

    @Valid
    private List<CreateStoryInformationActionDto> informationActions;

    private Set<Integer> tags;
}
