package org.valeneisa.Dtos;
import lombok.Data;

@Data

public class CreditoRespuesta {
    private double precio;        // monto a financiar [cite: 47]
    private int cuotas;           // número de cuotas mensuales [cite: 49]
    private double tasaMensual; // tasa de interés mensual en porcentaje [cite: 50]
}