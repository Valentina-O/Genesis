package org.valeneisa.Dtos;
import lombok.Data;

@Data

public class RespuestaSueno {
    private String modo; // "DESPERTAR" o "DORMIR"
    private String horaReferencia; // HH:mm
    private int minutosParaConciliar = 14;
}
