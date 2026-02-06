package com.victor.picpay.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleInvalidArguments(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                exception.getStatusCode(),
                "Um ou mais campos estão inválidos. Faça o ajuste e tente novamente."
        );

        problemDetail.setTitle("Erro de Validação");

        var errors = toMapErrors(exception.getBindingResult().getFieldErrors());

        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                exception.getMessage());

        problemDetail.setTitle("Erro ao encontrar usuário");

        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ProblemDetail handleTransactionNotFound(TransactionNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                exception.getMessage());

        problemDetail.setTitle("Erro ao encontrar transação");


        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ProblemDetail handleWalletNotFound(WalletNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                exception.getMessage());

        problemDetail.setTitle("Erro ao encontrar carteira");

        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(UserDataAlreadyExists.class)
    public ProblemDetail handleUserNotFound(UserDataAlreadyExists exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                exception.getMessage());

        problemDetail.setTitle("Cliente já existente");

        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                exception.getMessage());

        problemDetail.setTitle("Requisição Inválida");

        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    private Map<String, String> toMapErrors(List<FieldError> fieldErrorList) {
        return fieldErrorList.stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                )
        );
    }
}