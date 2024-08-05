package br.com.planner.planner.infra.exception;

public class ValidationNotFoundException extends RuntimeException{
    public ValidationNotFoundException(String message) {
        super(message);
    }
}
