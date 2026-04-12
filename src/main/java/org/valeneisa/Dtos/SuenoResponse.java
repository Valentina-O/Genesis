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

public class SuenoResponse {
    private List<OpcionSueno> opciones;
    private int tokensConsumidos;
}

