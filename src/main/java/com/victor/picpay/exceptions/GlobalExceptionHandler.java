package com.victor.picpay.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
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
    public ProblemDetail handleMethodArgument(MethodArgumentNotValidException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos estão inválidos."
        );

        problem.setTitle("Erro de Validação");

        problem.setProperty("errors", toMapErrors(exception.getBindingResult().getFieldErrors()));

        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            TransactionNotFoundException.class,
            WalletNotFoundException.class
    })
    public ProblemDetail handleUserNotFound(RuntimeException exception) {
        return createProblemDetails(HttpStatus.NOT_FOUND, "Recurso não encontrado", exception.getMessage());
    }

    @ExceptionHandler(UserDataAlreadyExists.class)
    public ProblemDetail handleConflict(UserDataAlreadyExists exception) {
        return createProblemDetails(HttpStatus.CONFLICT, "Conflito de dados", exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
        return createProblemDetails(HttpStatus.BAD_REQUEST, "Requisição inválida", exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException exception) {
        return createProblemDetails(HttpStatus.FORBIDDEN, "Acesso Negado", "Você não tem permissão para acessar este recurso.");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUncaught(Exception exception) {
        return createProblemDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Erro Interno", "Ocorreu um erro inesperado no servidor.");
    }

    private ProblemDetail createProblemDetails(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());
        return problem;
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