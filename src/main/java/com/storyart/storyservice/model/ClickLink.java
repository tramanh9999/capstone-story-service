package com.storyart.storyservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "click_link")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClickLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int storyId;
    private String link;

    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
