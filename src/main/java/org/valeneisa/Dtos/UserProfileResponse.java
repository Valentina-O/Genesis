package org.valeneisa.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {

    private Long idUsuario;
    private String username;
    private String correoElectronico;
    private String rolUsuario;
    private int tokensDisponibles;
    private boolean estaActivo;
    private String planActivo;
    // getters y setters
}