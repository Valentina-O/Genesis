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

<<<<<<<< HEAD:src/main/java/org/valeneisa/Dtos/SolicitudSueno.java
public class SolicitudSueno {
========
public class RespuestaSueno {
>>>>>>>> feature/user:src/main/java/org/valeneisa/Dtos/RespuestaSueno.java
    private List<OpcionSueno> opciones;
    private int tokensConsumidos;
}

