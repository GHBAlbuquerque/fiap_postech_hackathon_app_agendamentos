package com.fiap.hackathon.common.exceptions;

import com.fiap.hackathon.common.exceptions.custom.*;
import com.fiap.hackathon.common.exceptions.model.ExceptionDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class ExceptionControllerHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(ExceptionControllerHandler.class);

    @ExceptionHandler(value = {AlreadyRegisteredException.class})
    public ResponseEntity<ExceptionDetails> resourceException(AlreadyRegisteredException ex, WebRequest request) {

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400",
                "The request could not be completed due to a conflict.",
                ex.getCode().name(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getErrors());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntitySearchException.class})
    public ResponseEntity<ExceptionDetails> resourceException(EntitySearchException ex, WebRequest request) {

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404",
                "The requested resource was not found.",
                ex.getCode().name(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getErrors());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CreateEntityException.class})
    public ResponseEntity<ExceptionDetails> resourceException(CreateEntityException ex, WebRequest request) {

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400",
                "Couldn't create entity on database. Try again with different values.",
                ex.getCode().name(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getErrors());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AppointmentConflictException.class})
    public ResponseEntity<ExceptionDetails> resourceException(AppointmentConflictException ex, WebRequest request) {

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400",
                "Couldn't schedule appointment. Please try again with a different day and/or timeslot.",
                ex.getCode().name(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getErrors());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AppointmentCreationException.class})
    public ResponseEntity<ExceptionDetails> resourceException(AppointmentCreationException ex, WebRequest request) {

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400",
                "Couldn't create appointment. Try again with different values.",
                ex.getCode().name(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getErrors());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AppointmentUpdateException.class})
    public ResponseEntity<ExceptionDetails> resourceException(AppointmentUpdateException ex, WebRequest request) {

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400",
                "Couldn't cancel appointment.",
                ex.getCode().name(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getErrors());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ExceptionDetails> resourceException(AuthenticationException ex, WebRequest request) {

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400",
                "Invalid Requester.",
                ex.getCode().name(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getErrors());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUncaughtException(Exception ex, WebRequest request) {
        logger.error("Uncaught Exception. {}", ex.getMessage());
        logger.error("Class: {}", ex.getClass());

        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        final var message = new ExceptionDetails(
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/500",
                "Internal server error. Please contact the admin.",
                "NO-CODE",
                "Unindentified error.",
                status.value(),
                new Date(),
                null);

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);
    }


}
