package ee.mihkel.rendipood.exception;

import ee.mihkel.rendipood.model.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(RuntimeException e) {
        ErrorMessage message = new ErrorMessage();
        message.setTimestamp(new Date());
        message.setStatus(400);
        message.setError(e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }
}
