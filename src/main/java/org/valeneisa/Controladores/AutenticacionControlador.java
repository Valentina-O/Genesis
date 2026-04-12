package org.valeneisa.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.Autenticacion.SolicitudLogin;
import org.valeneisa.Dtos.Autenticacion.SolicitudRegistro;
import org.valeneisa.Dtos.Autenticacion.RespuestaAutenticacion;
import org.valeneisa.Servicios.ServicioAutenticacion;

@RestController
@RequestMapping("/auth")
public class AutenticacionControlador {

    private final ServicioAutenticacion servicioAutenticacion;

    public AutenticacionControlador(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    // Cambiado de /register a /registrar para que coincida con el YML
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody SolicitudRegistro solicitud) {
        String token = servicioAutenticacion.register(solicitud);
        return new ResponseEntity<>(new RespuestaAutenticacion(token), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<RespuestaAutenticacion> login(@RequestBody SolicitudLogin solicitud) {
        String token = servicioAutenticacion.login(solicitud);
        return ResponseEntity.ok(new RespuestaAutenticacion(token));
    }
}