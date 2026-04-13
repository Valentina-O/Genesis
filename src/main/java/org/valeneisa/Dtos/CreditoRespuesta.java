package org.valeneisa.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO que representa la respuesta de un cálculo de crédito.
 * Incluye los valores de la cuota mensual, el total pagado,
 * los intereses generados, la tabla de amortización y los
 * tokens consumidos en la operación.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditoRespuesta {

    /**
     * Valor de la cuota mensual a pagar.
     */
    private double cuotaMensual;

    /**
     * Total pagado al finalizar todas las cuotas.
     */
    private double totalPagado;

    /**
     * Total de intereses generados durante el crédito.
     */
    private double totalIntereses;

    /**
     * Tabla de amortización detallada del crédito.
     */
    private List<FilaAmortizacion> tablaAmortizacion;

    /**
     * Cantidad de tokens consumidos durante la operación,
     * usada para la trazabilidad de costos.
     */
    private int tokensConsumidos;
}