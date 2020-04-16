package com.storyart.storyservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "screen_reading_time")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenReadingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int duration;
    private String screenId;
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }


}
