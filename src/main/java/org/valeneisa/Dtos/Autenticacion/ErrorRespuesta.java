package org.valeneisa.Dtos.Autenticacion;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO que representa una respuesta de error en el sistema.
 * Incluye el código de estado, mensaje descriptivo y la marca de tiempo.
 */
@Getter
@Setter
public class ErrorRespuesta {

    /**
     * Código del error (generalmente HTTP).
     */
    private int codigo;

    /**
     * Mensaje descriptivo del error.
     */
    private String mensaje;

    /**
     * Fecha y hora en que ocurrió el error.
     */
    private LocalDateTime timestamp;

    /**
     * Constructor que inicializa el error con código y mensaje,
     * asignando automáticamente la fecha actual.
     *
     * @param codigo Código del error.
     * @param mensaje Mensaje descriptivo.
     */
    public ErrorRespuesta(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

}