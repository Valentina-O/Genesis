package org.valeneisa.Dtos.autenticacion;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SolicitudLogin {

    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}