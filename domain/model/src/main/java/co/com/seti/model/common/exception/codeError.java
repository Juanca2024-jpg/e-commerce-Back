package co.com.seti.model.common.exception;

import lombok.Getter;

@Getter
public enum codeError {
    FOUND(302),
    NOT_FOUND(404);

    private final Integer code;

    codeError(Integer code) {
        this.code = code;
    }
}
