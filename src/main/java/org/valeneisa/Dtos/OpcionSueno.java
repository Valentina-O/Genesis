package org.valeneisa.Dtos;
import lombok.Data;

@Data

public class OpcionSueno {
    private int ciclos;
    private String horaCalculada;
    private String calidad; // "Mínimo", "Recomendado", "Ideal"
}

