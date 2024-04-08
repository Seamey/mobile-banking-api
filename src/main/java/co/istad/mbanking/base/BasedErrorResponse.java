package co.istad.mbanking.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasedErrorResponse {
    // make flexible error
    private BaseError error;
}
