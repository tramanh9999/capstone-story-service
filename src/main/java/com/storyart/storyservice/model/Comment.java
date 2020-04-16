package com.storyart.storyservice.model;

import com.storyart.storyservice.common.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "comment")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int userId;
    private int storyId;

    @NotBlank(message = "")
    @Column(length = 10000)
    @Size(max = 10000)
    private String content;

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean active;

    @Column(columnDefinition="tinyint(1) default 0")
    private boolean disableByAdmin;

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
