package com.bf.image.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private int code;

    private String message;

    public CustomException (String message) {
        super(message);
        this.message = message;
    }


}
