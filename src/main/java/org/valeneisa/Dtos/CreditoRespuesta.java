package org.valeneisa.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditoRespuesta {

    private double cuotaMensual;
    private double totalPagado;
    private double totalIntereses;
    private List<FilaAmortizacion> tablaAmortizacion;

    // Trazabilidad de costos para que el usuario sepa cuánto gastó
    private int tokensConsumidos;
}