package com.example.demo.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Color {
    WHITE("Белый"),
    BLACK("Черный"),
    GREEN("Зеленый"),
    RED("Красный"),
    BLUE("Синий");

    private final String description;
}
