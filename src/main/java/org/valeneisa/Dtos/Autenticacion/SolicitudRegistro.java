package org.valeneisa.Dtos.Autenticacion;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
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

}