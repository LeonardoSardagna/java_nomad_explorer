package br.com.planner.planner.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> error404(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<MessageError>> error400(MethodArgumentNotValidException exception) {
        var erro = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(erro.stream().map(MessageError::new).toList());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> error500(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + exception.getLocalizedMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> errorResponseResponseEntity(MissingServletRequestParameterException exception) {
        var response = "Missing request parameter: " + exception.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private record MessageError(String code, String message) {
        private MessageError(FieldError error) {
            this(error.getCode(), error.getDefaultMessage());
        }
    }
}
