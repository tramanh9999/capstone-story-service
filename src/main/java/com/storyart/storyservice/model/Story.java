package com.storyart.storyservice.model;

import com.storyart.storyservice.common.DateAudit;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "story")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Story extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(min = 5, max = 250, message = "Tiêu đề truyện phải có ít nhất 5 kí tự và không quá 250 kí tự")
    private String title;

    private int userId;

    @Size(min = 10, message = "Nội dung giói thiệu truyện phải ít nhất 10 kí tự")
    private String intro;

    @Size(max = 100)
    private String animation;

    @Size(max = 1000, message = "Ảnh không quá 1000 kí tự")
    private String image;

    private String firstScreenId;

    @Column(columnDefinition = "float default 0")
    @Range(min = 0, max = 5)
    private double avgRate;

    @Column(columnDefinition="tinyint(1) default 0")
    private boolean active;

    private boolean published;

    @Column(columnDefinition="tinyint(1) default 0")
    private boolean deactiveByAdmin;

    private Date createdAt;

    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
