package org.valeneisa.Dtos;

import jakarta.validation.constraints.NotNull;

public class SubscribeRequest {

    @NotNull(message = "El planId es obligatorio")
    private Long planId;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}