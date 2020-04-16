package com.storyart.storyservice.model;

import com.storyart.storyservice.common.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;



@Entity
@Table(name = "reading_history")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingHistory extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer userId;

    @NotNull
    private Integer storyId;

    private String listScreenId;
    private boolean isReachingEnd;

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
