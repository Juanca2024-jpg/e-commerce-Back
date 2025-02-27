package co.com.seti.model.common.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorExceptionTest {
    @Test
    public void errorExceptionTest() {
        ErrorException errorException = new ErrorException("Not found", 404);
        Assertions.assertEquals("Not found", errorException.getMessage());
        Assertions.assertEquals(404, errorException.getCode());
    }
}