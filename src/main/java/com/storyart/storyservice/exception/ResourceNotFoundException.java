package com.storyart.storyservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resouceName;
    private String fieldName;
    private Object fieldValue;


    public ResourceNotFoundException(String resouceName, String fieldName, Object fieldValue) {


        super(String.format("%s not found  with %s: '%s' ", resouceName, fieldName, fieldValue));


        this.resouceName = resouceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;


    }

}
