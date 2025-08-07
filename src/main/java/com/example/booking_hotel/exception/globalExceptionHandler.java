package com.example.booking_hotel.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.booking_hotel.dto.response.ExceptionResponse;

@ControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handlingRuntimeException(RuntimeException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        exceptionResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ExceptionResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setCode(errorCode.getCode());
        exceptionResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(exceptionResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ExceptionResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ExceptionResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    // Lỗi khi Validation thuộc tính
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
        MissingServletRequestParameterException.class
    })
    public ResponseEntity<ExceptionResponse> handleValidationException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setTimestamp(new Date());
        exceptionResponse.setCode(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setPath(request.getDescription(false).replace("uri=", ""));

        List<String> messages = new ArrayList<>();

        // MethodArgumentNotValidException  xử lý lỗi validate từ @Valid hoặc @Validated trong Spring khi dùng với:
        // @RequestBody
        // @ModelAttribute
        if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                messages.add(fieldError.getDefaultMessage());
            }
        }
        // ConstraintViolationException Khi validate @RequestParam, @PathVariable với @Validated.
        else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) ex;
            for (ConstraintViolation<?> violation : violationException.getConstraintViolations()) {
                messages.add(violation.getMessage());
            }

        }
        // MissingServletRequestParameterException  Khi thiếu tham số bắt buộc trong URL hoặc query string.
        else if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException missingParamEx = (MissingServletRequestParameterException) ex;

            messages.add("Thiếu tham số: " + missingParamEx.getParameterName());
        }

        String errorMessages = String.join(". ", messages);
        exceptionResponse.setMessage(errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
