package com.storyart.storyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> implements Serializable {
    private boolean success;
    private HashMap<String, String> errors = new HashMap<>();
    private T data;
}
