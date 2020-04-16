package com.storyart.storyservice.model;

import com.storyart.storyservice.common.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class ReactionId implements Serializable {
    private int userId;
    private int commentId;
}

@Entity
@Table(name = "reaction")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ReactionId.class)
public class Reaction extends DateAudit {

    @Id
    private int userId;

    @Id
    private int commentId;

    private String type;

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean isActive;

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
