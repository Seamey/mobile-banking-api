package co.istad.mbanking.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseError<T> {
    // Request Entity, Too large, Bad Request , ....
    private String code;  // code error for developer
    // Detail error for use
    private T description;

}
