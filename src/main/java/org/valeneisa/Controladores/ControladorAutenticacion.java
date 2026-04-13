package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.Autenticacion.RespuestaAutenticacion;
import org.valeneisa.Dtos.Autenticacion.SolicitudLogin;
import org.valeneisa.Dtos.Autenticacion.SolicitudRegistro;
import org.valeneisa.Servicios.ServicioAutenticacion;
/**
 * Controlador REST para la autenticación de usuarios en la plataforma.
 * <p>
 * Expone endpoints públicos bajo el prefijo {@code /auth} para el inicio de sesión
 * y el registro de nuevos usuarios. Cada operación exitosa retorna un token JWT
 * encapsulado en una {@link RespuestaAutenticacion}.
 * </p>
 *
 * @see ServicioAutenticacion
 * @see RespuestaAutenticacion
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ControladorAutenticacion {

    private final ServicioAutenticacion servicioAutenticacion;
    /**
     * Autentica a un usuario registrado y retorna un token JWT de acceso.
     * <p>
     * Valida las credenciales recibidas en el cuerpo de la solicitud. Si son correctas,
     * delega en {@link ServicioAutenticacion#login(SolicitudLogin)} para generar el token.
     * </p>
     *
     * @param request objeto {@link SolicitudLogin} con las credenciales del usuario
     *                (correo y contraseña). Debe pasar las validaciones de {@code @Valid}.
     * @return {@link ResponseEntity} con estado {@code 200 OK} y el token JWT dentro
     *         de un {@link RespuestaAutenticacion}.
     */
    @PostMapping("/login")
    public ResponseEntity<RespuestaAutenticacion> login(
            @RequestBody @Valid SolicitudLogin request) {

        String token = servicioAutenticacion.login(request);
        return ResponseEntity.ok(new RespuestaAutenticacion(token));
    }
    /**
     * Registra un nuevo usuario en la plataforma y retorna un token JWT de acceso.
     * <p>
     * Recibe los datos del nuevo usuario, los valida y delega en
     * {@link ServicioAutenticacion#register(SolicitudRegistro)} para crear la cuenta
     * y generar el token correspondiente.
     * </p>
     *
     * @param request objeto {@link SolicitudRegistro} con los datos del nuevo usuario.
     *                Debe pasar las validaciones de {@code @Valid}.
     * @return {@link ResponseEntity} con estado {@code 201 CREATED} y el token JWT dentro
     *         de un {@link RespuestaAutenticacion}.
     */

    @PostMapping("/registrar")
    public ResponseEntity<RespuestaAutenticacion> register(
            @RequestBody @Valid SolicitudRegistro request) {

        String token = servicioAutenticacion.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespuestaAutenticacion(token));
    }
}