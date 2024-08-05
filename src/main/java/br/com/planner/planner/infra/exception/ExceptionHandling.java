package br.com.planner.planner.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(ValidationNotFoundException.class)
    public ResponseEntity<Void> error404(ValidationNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(InternalServerErrorHandler.class)
    public ResponseEntity<String> error500(InternalServerErrorHandler exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + exception.getLocalizedMessage());
    }

    private record MessageError(String code, String message) {
        private MessageError(FieldError error) {
            this(error.getCode(), error.getDefaultMessage());
        }
    }
}
