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

public class CreditoResponse {
    private double cuotaMensual;
    private double totalPagado;
    private double totalIntereses;
    private List<FilaAmortizacion> tablaAmortizacion;
    private int tokensConsumidos; // Trazabilidad de costos [cite: 3]
}
