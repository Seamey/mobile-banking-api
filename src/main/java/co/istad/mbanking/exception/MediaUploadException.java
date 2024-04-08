package co.istad.mbanking.exception;

import co.istad.mbanking.base.BaseError;
import co.istad.mbanking.base.BasedErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestController
@Slf4j
public class MediaUploadException {
    @Value("${spring.servlet.multipart.max-request-size}")
    private  String maxSize;
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception){
//        BaseError basedError = BaseError.builder()
//                .code(HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase())
//                .description("Media upload size maximum is "+maxSize)
//                .build();

    /// Map not use for interprise
@ExceptionHandler(MaxUploadSizeExceededException.class)
        BasedErrorResponse  handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception){
        BaseError<String> basedError = BaseError.<String>builder()
                .code(HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase())
                .description("Media upload size maximum is: "+maxSize)
                .build();
        return new BasedErrorResponse(basedError);
    }
}
