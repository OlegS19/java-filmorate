package ru.yandex.practicum.javafilmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NotNull
@Valid
public class Mpa {
    private Integer id;
    private String name;

}
