package org.valeneisa.controladores;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.autenticación.RespuestaAutenticacion;
import org.valeneisa.Dtos.autenticación.SolicitudLogin;
import org.valeneisa.Dtos.autenticación.SolicitudRegistro;
import org.valeneisa.Servicios.ServicioAutenticacion;

@RestController
@RequestMapping("/auth")
public class ControladorAutenticacion {

    private final ServicioAutenticacion servicioAutenticacion;

    public ControladorAutenticacion(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @PostMapping("/login")
    public RespuestaAutenticacion iniciarSesion(@RequestBody @Valid SolicitudLogin solicitud) {
        String token = servicioAutenticacion.login(solicitud);
        return new RespuestaAutenticacion(token);
    }

    @PostMapping("/register")
    public RespuestaAutenticacion registrar(@RequestBody @Valid SolicitudRegistro solicitud) {
        String token = servicioAutenticacion.register(solicitud);
        return new RespuestaAutenticacion(token);
    }
}