package org.valeneisa.Dtos;
import lombok.Data;

@Data

<<<<<<<< HEAD:src/main/java/org/valeneisa/Dtos/CreditoRespuesta.java
<<<<<<<< HEAD:src/main/java/org/valeneisa/Dtos/CreditoRespuesta.java
public class CreditoRespuesta {
========
public class CreditoSolicitud {
>>>>>>>> feature/user:src/main/java/org/valeneisa/Dtos/CreditoSolicitud.java
========
public class CreditoSolicitud {
>>>>>>>> feature/user:src/main/java/org/valeneisa/Dtos/CreditoSolicitud.java
    private double precio;        // monto a financiar [cite: 47]
    private int cuotas;           // número de cuotas mensuales [cite: 49]
    private double tasaMensual; // tasa de interés mensual en porcentaje [cite: 50]
}
