package br.pucpr.checkinexpress.exception;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
}
