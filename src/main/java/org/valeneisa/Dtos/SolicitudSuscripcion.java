package org.valeneisa.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SolicitudSuscripcion {

    @NotNull(message = "El planId es obligatorio")
    private Long planId;

}