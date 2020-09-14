package com.carrocompra.app.customException;

import com.carrocompra.app.LowerCaseClassNameResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public
class ErrorHandler {

    private HttpStatus status;
    private int code;
    private String message;
    private String backendMessage;

    private ErrorHandler() {
    }

    @Override
    public String toString() {
        return "--------ERROR--------------" + '\n' +
                "status:" + status + "," + '\n' +
                "code:" + code + "," + '\n' +
                "message:" + message + "," + '\n' +
                "backendMessage:"+ backendMessage ;
    }

    public ErrorHandler(HttpStatus status) {
        this();
        this.status = status;
        this.code = status.value();
    }

    public ErrorHandler(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.code = status.value();
        this.message = "Unexpected error";
        this.backendMessage = ex.getLocalizedMessage();
    }

    public ErrorHandler(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.code = status.value();
        this.message = message;
        this.backendMessage = ex.getLocalizedMessage() ;
    }


}

