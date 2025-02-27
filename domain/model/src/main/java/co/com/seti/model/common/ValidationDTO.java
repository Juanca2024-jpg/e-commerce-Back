package co.com.seti.model.common;


public record ValidationDTO(
        String field,
        String error
) {
}