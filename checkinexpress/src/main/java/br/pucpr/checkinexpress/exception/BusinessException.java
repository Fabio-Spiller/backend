package br.pucpr.checkinexpress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    // Construtor que aceita uma mensagem de erro
    public BusinessException(String message) {
        super(message);
    }

    // Construtor opcional para envolver outra exceção
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}