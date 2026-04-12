package org.valeneisa.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SolicitudSuscripcion {

    @NotNull(message = "El planId es obligatorio")
    private Long planId;

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}