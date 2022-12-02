package ru.inside.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDtoResponse {
    private Long id;
    private String name;
    private String message;
}
