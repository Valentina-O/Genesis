package org.valeneisa.Dtos;

import lombok.Data;

@Data

public class IMCRequest {
    private double pesoKg;    // peso en kilogramos
    private double alturaCm;  // altura en centímetros [cite: 80]
}
