package org.valeneisa.Dtos;
import lombok.Data;

@Data

public class ConversorPedido {
    private double monto;
    private String monedaOrigen; // "COP" o "USD"
    private String monedaDestino; // "COP" o "USD"
    private double tasaSugerida; // La envía el "Admin" (ej: 3950.0)
}
