package pl.itger.wine;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class WineError {


    private HttpStatus status;
    private String message;
    private List<String> errors;

    public WineError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public WineError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}

