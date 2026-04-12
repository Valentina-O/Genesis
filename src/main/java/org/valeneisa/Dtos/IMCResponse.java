package org.valeneisa.Dtos;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor // Genera el constructor público con todos los campos
@NoArgsConstructor  // Genera el constructor público vacío
public class IMCResponse {
    private double imc;
    private String categoria;
    private double pesoMinSaludable;
    private double pesoMaxSaludable;
    private double diferenciaPeso;
    private int tokensConsumidos;
}