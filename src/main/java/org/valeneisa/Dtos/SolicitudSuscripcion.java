package org.valeneisa.Dtos;

import jakarta.validation.constraints.NotNull;

public class SolicitudSuscripcion {

    @NotNull(message = "El planId es obligatorio")
    private Long planId;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}