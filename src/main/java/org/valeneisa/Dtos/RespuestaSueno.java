package org.valeneisa.Dtos;
import lombok.Data;

@Data

<<<<<<<< HEAD:src/main/java/org/valeneisa/Dtos/RespuestaSueno.java
public class RespuestaSueno {
========
public class SolicitudSueno {
>>>>>>>> feature/user:src/main/java/org/valeneisa/Dtos/SolicitudSueno.java
    private String modo; // "DESPERTAR" o "DORMIR"
    private String horaReferencia; // HH:mm
    private int minutosParaConciliar = 14;
}
