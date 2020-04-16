package com.storyart.storyservice.model;

import com.storyart.storyservice.common.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "info_condition")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoCondition extends DateAudit {
    @Id
    @Size(max = 255)
    private String id;

    @Size(max = 255)
    private String informationId;

    @Size(max = 255)
    @NotBlank(message = "Loại điều kiện thông tin không được để trống")
    private String type; // >, <, =, >=, <=

    @Size(max = 255)
    @NotBlank(message = "Giá trị của điều kiện thông tin không được để trống")
    private String value;

    @Size(max = 255)
    @NotBlank(message = "Màn hình chuyển tiếp của điều kiện không được để trống")
    private String nextScreenId;
}
