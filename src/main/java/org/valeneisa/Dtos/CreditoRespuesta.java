package org.valeneisa.Dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreditoRespuesta {
    private double cuotaMensual;
    private double totalPagado;
    private double totalIntereses;
    private List<FilaAmortizacion> tablaAmortizacion;
    private int tokensConsumidos;
}