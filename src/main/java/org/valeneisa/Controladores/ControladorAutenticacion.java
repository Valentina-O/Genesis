package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.Autenticacion.RespuestaAutenticacion;
import org.valeneisa.Dtos.Autenticacion.SolicitudLogin;
import org.valeneisa.Dtos.Autenticacion.SolicitudRegistro;
import org.valeneisa.Servicios.ServicioAutenticacion;

@RestController
@RequestMapping("/auth")
public class ControladorAutenticacion {

    private final ServicioAutenticacion servicioAutenticacion;

    public ControladorAutenticacion(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @PostMapping("/login")
    public RespuestaAutenticacion login(@RequestBody @Valid SolicitudLogin request) {
        String token = servicioAutenticacion.login(request);
        return new RespuestaAutenticacion(token);
    }

    @PostMapping("/register")
    public RespuestaAutenticacion register(@RequestBody @Valid SolicitudRegistro request) {
        String token = servicioAutenticacion.register(request);
        return new RespuestaAutenticacion(token);
    }
}