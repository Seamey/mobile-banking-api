package co.istad.mbanking.features.madia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder  // use for build object  thorugh builder partten
public record MediaResponse(
        // must be unique (the name of image or file )
        String name,
        String contentType, // can be png,JPEG, MP4
        String extension,
        String uri,   // part of file that clicent access of it ( we need to pass make be can access it)
       @JsonInclude(JsonInclude.Include.NON_NULL)  // mean if it null don't show it
        Long size
) {
}
