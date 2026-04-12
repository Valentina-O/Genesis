package org.valeneisa.Dtos.autenticacion;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class SolicitudRegistro {

    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "Mínimo 6 caracteres")
    private String contrasena;

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}