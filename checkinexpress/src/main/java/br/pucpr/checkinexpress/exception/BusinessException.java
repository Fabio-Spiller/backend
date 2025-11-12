package br.pucpr.checkinexpress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// O status padrão é BAD_REQUEST, mas pode ser sobrescrito pelo atributo 'status'
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    // Adiciona um campo para armazenar o status HTTP customizado (se fornecido)
    private final HttpStatus status;

    // --- Construtores Existentes (Mantêm o BAD_REQUEST padrão) ---

    // Construtor 1: Aceita apenas a mensagem (usa o status padrão da anotação: 400 BAD_REQUEST)
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    // Construtor 2: Opção para envolver outra exceção (usa o status padrão)
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
    }

    // --- NOVO Construtor (Permite Status Customizado) ---

    /**
     * Construtor que permite definir o HttpStatus customizado, ignorando o
     * default definido na anotação @ResponseStatus.
     * @param status O HttpStatus desejado (ex: NOT_FOUND, FORBIDDEN).
     * @param message A mensagem de erro.
     */
    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    // --- Getter para o GlobalExceptionHandler ---

    public HttpStatus getStatus() {
        return status;
    }
}