package springboot.restfull.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
