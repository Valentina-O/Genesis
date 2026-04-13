package org.valeneisa.Dtos.Autenticacion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa la solicitud de registro de un nuevo usuario.
 * Contiene la información necesaria para crear una cuenta en el sistema.
 */
@Getter
@Setter
public class SolicitudRegistro {

    /**
     * Nombre de usuario.
     */
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    /**
     * Correo electrónico del usuario.
     */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    private String correo;

    /**
     * Contraseña del usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}