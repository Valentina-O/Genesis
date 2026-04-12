package org.valeneisa.Dtos;

import lombok.Data;

@Data
public class SolicitudSueno {
    private String modo;
    private String horaReferencia;
    private int minutosParaConciliar = 14;
}