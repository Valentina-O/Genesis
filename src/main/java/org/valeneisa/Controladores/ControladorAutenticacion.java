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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ControladorAutenticacion {

    private final ServicioAutenticacion servicioAutenticacion;

    @PostMapping("/login")
    public ResponseEntity<RespuestaAutenticacion> login(
            @RequestBody @Valid SolicitudLogin request) {

        String token = servicioAutenticacion.login(request);
        return ResponseEntity.ok(new RespuestaAutenticacion(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<RespuestaAutenticacion> register(
            @RequestBody @Valid SolicitudRegistro request) {

        String token = servicioAutenticacion.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespuestaAutenticacion(token));
    }
}