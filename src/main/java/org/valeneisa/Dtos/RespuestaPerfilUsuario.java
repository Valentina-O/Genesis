package org.valeneisa.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaPerfilUsuario {

    private Long idUsuario;
    private String usuario;
    private String correoElectronico;
    private String rolUsuario;
    private int tokensDisponibles;
    private boolean estaActivo;
    private String planActivo;
}