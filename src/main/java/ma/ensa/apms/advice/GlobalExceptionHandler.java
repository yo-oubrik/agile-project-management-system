package ma.ensa.apms.advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.EntityNotFoundException;
import ma.ensa.apms.dto.Response.ApiErrorResponse;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.DuplicateResourceException;
import ma.ensa.apms.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex,
            WebRequest request) {
        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.BAD_REQUEST,
                "Validation failed", request);
        errorResponse.setValidationErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {

        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.NOT_FOUND,
                ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResource(
            DuplicateResourceException ex,
            WebRequest request) {
        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.CONFLICT,
                ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // @ExceptionHandler(BadCredentialsException.class)
    // public ResponseEntity<ApiErrorResponse>
    // handleBadCredentialsException(BadCredentialsException ex,
    // WebRequest request) {
    // ApiErrorResponse errorResponse =
    // buildApiErrorResponse(HttpStatus.UNAUTHORIZED,
    // "Incorrect username or password", request);
    // return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    // }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex,
            WebRequest request) {
        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.NOT_FOUND,
                "Resource not found", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request) {
        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
                "Method not allowed", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            WebRequest request) {
        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.NOT_FOUND,
                ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {
        logger.error("Unhandled exception", ex);
        ApiErrorResponse errorResponse = buildApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiErrorResponse buildApiErrorResponse(HttpStatus status, String message, WebRequest request) {

        /*
         * the substring(4) is used to remove the "uri=" prefix from the request
         * description
         */
        return ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).substring(4))
                .build();
    }
}
