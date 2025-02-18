package com.mesofi.myth.collection.mgmt.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.mesofi.myth.collection.mgmt.exceptions.CatalogItemNotFoundException;
import com.mesofi.myth.collection.mgmt.exceptions.FigurineNotFoundException;
import com.mesofi.myth.collection.mgmt.model.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Handle the validations.
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDetails> handleValidationExceptions(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    List<FieldError> errors = ex.getBindingResult().getFieldErrors();

    String[] errorMessages =
        errors.stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .toArray(String[]::new);

    return createErrorDetails(BAD_REQUEST, errorMessages, null, request);
  }

  // Handle the case where the message cannot be readable (400 error)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(BAD_REQUEST)
  public ResponseEntity<ErrorDetails> handleMessageNotReadableException(
      HttpMessageNotReadableException ex, HttpServletRequest request) {

    return createErrorDetails(
        BAD_REQUEST, "The required request body is missing.", ex.getMessage(), request);
  }

  // Handle the case where no resource is found for a URL (404 error)
  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public ResponseEntity<ErrorDetails> handleNoResourceFoundException(
      NoResourceFoundException ex, HttpServletRequest request) {

    return createErrorDetails(
        NOT_FOUND, "The requested URL was not found on this server.", null, request);
  }

  // Handle unsupported HTTP methods (405 error)
  //  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  //  @ResponseStatus(METHOD_NOT_ALLOWED)
  //  public ResponseEntity<ErrorDetails> handleHttpRequestMethodNotSupported(
  //      HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
  //
  //    return createErrorDetails(
  //        METHOD_NOT_ALLOWED,
  //        "The requested HTTP method is not supported for this endpoint.",
  //        request);
  //  }

  // Handle the case where no resource is found for a URL (404 error)
  @ExceptionHandler(CatalogItemNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public ResponseEntity<ErrorDetails> handleHttpRequestCatalogItemNotFound(
      CatalogItemNotFoundException ex, HttpServletRequest request) {
    return createErrorDetails(
        NOT_FOUND, "The catalog for the given identifier was not found.", null, request);
  }

  // Handle the case where no resource is found for a URL (404 error)
  @ExceptionHandler(FigurineNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public ResponseEntity<ErrorDetails> handleHttpRequestFigurineNotFound(
      FigurineNotFoundException ex, HttpServletRequest request) {
    return createErrorDetails(
        NOT_FOUND, "The figurine for the given identifier was not found.", null, request);
  }

  private ResponseEntity<ErrorDetails> createErrorDetails(
      HttpStatus httpStatus, String messages, String detailMessage, HttpServletRequest request) {

    return createErrorDetails(httpStatus, new String[] {messages}, detailMessage, request);
  }

  private ResponseEntity<ErrorDetails> createErrorDetails(
      HttpStatus httpStatus, String[] messages, String detailMessage, HttpServletRequest request) {

    ErrorDetails resp =
        new ErrorDetails(
            httpStatus.getReasonPhrase(), messages, detailMessage, request.getRequestURI());
    return new ResponseEntity<>(resp, httpStatus);
  }
}
