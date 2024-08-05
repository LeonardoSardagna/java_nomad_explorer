package br.com.planner.planner.infra.exception;

public class InternalServerErrorHandler extends RuntimeException{
    public InternalServerErrorHandler(String message) {
        super(message);
    }
}
