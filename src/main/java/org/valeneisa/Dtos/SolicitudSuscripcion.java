package org.valeneisa.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa la solicitud para realizar una suscripción a un plan.
 * Contiene el identificador del plan seleccionado por el usuario.
 */
@Setter
@Getter
public class SolicitudSuscripcion {

    /**
     * Identificador del plan al que el usuario desea suscribirse.
     * Este campo es obligatorio.
     */
    @NotNull(message = "El planId es obligatorio")
    private Long planId;

}