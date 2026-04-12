package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.Autenticacion.RespuestaAutenticacion;
import org.valeneisa.Dtos.Autenticacion.SolicitudLogin;
import org.valeneisa.Dtos.Autenticacion.SolicitudRegistro;
import org.valeneisa.Servicios.AuthService;

@RestController
@RequestMapping("/auth")
public class ControladorAutenticacion {

    private final AuthService authService;

    public ControladorAutenticacion(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public RespuestaAutenticacion login(@RequestBody @Valid SolicitudLogin request) {
        String token = authService.login(request);
        return new RespuestaAutenticacion(token);
    }

    @PostMapping("/register")
    public RespuestaAutenticacion register(@RequestBody @Valid SolicitudRegistro request) {
        String token = authService.register(request);
        return new RespuestaAutenticacion(token);
    }
}