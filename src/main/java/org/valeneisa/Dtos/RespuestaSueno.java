package org.valeneisa.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que representa la respuesta de un cálculo relacionado con el sueño.
 * Contiene las diferentes opciones de horarios de sueño, los tokens consumidos
 * y un mensaje informativo sobre el resultado de la operación.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaSueno {

    /**
     * Lista de opciones de sueño calculadas.
     */
    private List<OpcionSueno> opciones;

    /**
     * Cantidad de tokens consumidos durante la operación.
     */
    private int tokensConsumidos;

    /**
     * Mensaje informativo sobre el resultado del cálculo.
     */
    private String mensaje;
}