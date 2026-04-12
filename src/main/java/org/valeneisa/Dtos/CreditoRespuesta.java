package org.valeneisa.Dtos;
 feature/operations
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

    private int tokensConsumidos; // Trazabilidad de costos [cite: 3]
}

    private int tokensConsumidos;
}
