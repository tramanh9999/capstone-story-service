package com.storyart.storyservice.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

public interface ReadStatisticDto {
    int getReadCount();
    Date getDateCreated();
}
