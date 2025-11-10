package br.pucpr.checkinexpress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Indica ao Spring para monitorar exceções em todos os Controllers
public class GlobalExceptionHandler {

    // Método para tratar a BusinessException
    // Será acionado quando o UserService lançar new BusinessException(...)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {

        // Cria a resposta de erro com status 400 e a mensagem da exceção
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        // Retorna a resposta HTTP 400
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}