package org.valeneisa.Dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class FilaAmortizacion {
    private int periodo;
    private double cuota;
    private double capitalAmortizado;
    private double interesPagado;
    private double saldoRestante;
}
