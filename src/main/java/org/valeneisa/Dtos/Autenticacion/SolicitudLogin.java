package org.valeneisa.Dtos.Autenticacion;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa la solicitud de inicio de sesión.
 * Contiene las credenciales necesarias para autenticar a un usuario.
 */
@Setter
@Getter
public class SolicitudLogin {

    /**
     * Nombre de usuario.
     */
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    /**
     * Contraseña del usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

}