package co.istad.mbanking.exception;

import co.istad.mbanking.base.BaseError;
import co.istad.mbanking.base.BasedErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class ServiceException {

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> handleServiceErrors(ResponseStatusException ex) {
        BaseError<String> basedError = new BaseError<>();
        basedError.setCode(ex.getStatusCode().toString());
        basedError.setDescription(ex.getReason());

        BasedErrorResponse basedErrorResponse = new BasedErrorResponse();
        basedErrorResponse.setError(basedError);
        return ResponseEntity.ok(basedErrorResponse);
    }

}
