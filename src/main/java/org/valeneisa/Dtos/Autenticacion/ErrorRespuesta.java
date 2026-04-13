package org.valeneisa.Dtos.Autenticacion;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter

public class ErrorRespuesta {
    private int codigo;
    private String mensaje;
    private LocalDateTime timestamp;

    public ErrorRespuesta(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

}
