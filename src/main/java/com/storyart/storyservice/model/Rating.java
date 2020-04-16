package com.storyart.storyservice.model;

import com.storyart.storyservice.common.DateAudit;
import com.storyart.storyservice.model.ids.RatingId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.Date;
import java.util.Objects;

import javax.persistence.*;


@Entity
@Table(name = "rating")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RatingId.class)
public class Rating extends DateAudit {
    @Id
    private int userId;

    @Id
    private int storyId;

    @Range(min=0, max=5)
    private double stars;

    private Date createdAt;

    private Date updatedAt = new Date();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("run equal");
        if(obj == null) return false;
        if(!(obj instanceof Rating)) return false;
        Rating rating = (Rating) obj;
        return rating.storyId == storyId && rating.userId == userId;
    }
}
