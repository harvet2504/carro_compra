package com.carrocompra.app;

import com.carrocompra.app.EntityNotFoundException;
import com.carrocompra.app.customException.ErrorHandler;
import com.carrocompra.app.exceptions.ProductException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorHandler object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        ErrorHandler ErrorHandler = new ErrorHandler(BAD_REQUEST, error, ex.getRootCause());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorHandler object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        ErrorHandler ErrorHandler = new ErrorHandler(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex.getRootCause());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorHandler object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorHandler ErrorHandler = new ErrorHandler(BAD_REQUEST);
        ErrorHandler.setMessage("Validation error");
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ErrorHandler object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        ErrorHandler ErrorHandler = new ErrorHandler(BAD_REQUEST);
        ErrorHandler.setMessage("Validation error");
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ErrorHandler object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ErrorHandler ErrorHandler = new ErrorHandler(NOT_FOUND, ex);
        ErrorHandler.setMessage(ex.getMessage());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        ErrorHandler ErrorHandler = new ErrorHandler(NOT_FOUND, ex);
        ErrorHandler.setMessage(ex.getMessage());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorHandler object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        ErrorHandler ErrorHandler = new ErrorHandler(HttpStatus.BAD_REQUEST, error, ex.getRootCause());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(new ErrorHandler(HttpStatus.BAD_REQUEST, error, ex.getRootCause()));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorHandler object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        ErrorHandler ErrorHandler = new ErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR, error, ex.getRootCause());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorHandler ErrorHandler = new ErrorHandler(BAD_REQUEST);
        ErrorHandler.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        ErrorHandler.setBackendMessage(ex.getMessage());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ErrorHandler object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            HttpStatus status = HttpStatus.CONFLICT;
            ErrorHandler ErrorHandler = new ErrorHandler(HttpStatus.CONFLICT, "Database error", ex.getRootCause());
            writeLog(ErrorHandler, ex);
            return buildResponseEntity(ErrorHandler);
        }
        ErrorHandler ErrorHandler = new ErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR, ex.getRootCause());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ErrorHandler object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ErrorHandler ErrorHandler = new ErrorHandler(BAD_REQUEST);
        ErrorHandler.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        ErrorHandler.setBackendMessage(ex.getMessage());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);
    }

    /**
     * Handle Exception, handle IllegalArgumentException
     *
     * @param ex the Exception
     * @return the ErrorHandler object
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                    WebRequest request) {
        ErrorHandler ErrorHandler = new ErrorHandler(UNAUTHORIZED);
        ErrorHandler.setMessage(String.format("Usuario o password no válidos"));
        ErrorHandler.setBackendMessage(ex.getMessage());
        writeLog(ErrorHandler, ex);
        return buildResponseEntity(ErrorHandler);

    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Object> handleRunTimeException(ProductException e) {
        ErrorHandler ErrorHandler = new ErrorHandler(CONFLICT, e);
        ErrorHandler.setMessage(e.getMessage());
        writeLog(ErrorHandler, e);
        return buildResponseEntity(ErrorHandler);
    }

    private void writeLog(ErrorHandler ErrorHandler, Exception ex) {
        try {
            String pckName = this.getClass().getPackage().getName() + ".controller";
            String text = String.format("%s,\nfecha-hora:%s ", ErrorHandler.toString(), getTimestamp());
            List<StackTraceElement> list = Arrays.asList(ex.getStackTrace());
            StackTraceElement className = list.stream()
                    .filter(str -> str.getClassName().startsWith(pckName))
                    .findFirst()
                    .get();

            Class cls = Class.forName(className.getClassName());
            Object obj;

            try {
                obj = cls.newInstance();

            }catch (Exception e1){
                Class<?>[] type = { Object.class };
                Constructor<?> cons = cls.getConstructor(type);
                Object[] objCons = { null };
                obj = cons.newInstance(objCons);

            }


            Class[] paramString = new Class[1];
            paramString[0] = String.class;

            Method method = cls.getDeclaredMethod("writeLog", paramString);
            method.invoke(obj, new String(text));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorHandler ErrorHandler) {
        return new ResponseEntity<>(ErrorHandler, ErrorHandler.getStatus());
    }

}
