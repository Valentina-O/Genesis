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

<<<<<<<< HEAD:src/main/java/org/valeneisa/Dtos/CreditoSolicitud.java
<<<<<<<< HEAD:src/main/java/org/valeneisa/Dtos/CreditoSolicitud.java
public class CreditoSolicitud {
========
public class CreditoRespuesta {
>>>>>>>> feature/user:src/main/java/org/valeneisa/Dtos/CreditoRespuesta.java
========
public class CreditoRespuesta {
>>>>>>>> feature/user:src/main/java/org/valeneisa/Dtos/CreditoRespuesta.java
    private double cuotaMensual;
    private double totalPagado;
    private double totalIntereses;
    private List<FilaAmortizacion> tablaAmortizacion;
    private int tokensConsumidos; // Trazabilidad de costos [cite: 3]
}
