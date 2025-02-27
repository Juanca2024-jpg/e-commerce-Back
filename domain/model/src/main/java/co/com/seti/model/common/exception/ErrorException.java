package co.com.seti.model.common.exception;

import lombok.Getter;

@Getter
public class ErrorException extends Exception {

    private final Integer code;

    public ErrorException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
