package org.example.javatest.dto.error;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private Integer status;
    private String error;
    private String message;
    private Instant timestamp;
    private String path;
}
