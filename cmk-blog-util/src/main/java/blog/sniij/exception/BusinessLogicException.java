package blog.sniij.exception;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public BusinessLogicException(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
