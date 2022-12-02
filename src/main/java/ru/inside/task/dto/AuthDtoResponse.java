package ru.inside.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthDtoResponse {
    private String token;
    private String error;
}
