package org.company.insurance.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    private int code;

    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime errorTime;

    private String status;

    private Map<String, String> message;

    public ErrorResponse(
            HttpStatus httpStatus,
            Map<String, String> message
    ) {
        this.errorTime=LocalDateTime.now();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }
}