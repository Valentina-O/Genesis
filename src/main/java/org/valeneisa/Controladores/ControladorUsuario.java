package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.RespuestaPerfilUsuario;
import org.valeneisa.Dtos.SolicitudSuscripcion;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.Servicios.ServicioUsuario;
import org.valeneisa.tokens.Transaccion;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    // 🔹 PERFIL
    @GetMapping("/perfil")
    public RespuestaPerfilUsuario perfil(Authentication autenticacion) {
        return servicioUsuario.getProfile(autenticacion.getName());
    }

    // 🔹 HISTORIAL
    @GetMapping("/historial")
    public List<Transaccion> historial(
            Authentication autenticacion,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano
    ) {
        return servicioUsuario.getTransactions(
                autenticacion.getName(),
                pagina,
                tamano
        );
    }

    // 🔹 CATÁLOGO
    @GetMapping("/catalogo")
    public List<Operacion> catalogo() {
        return servicioUsuario.getCatalogo();
    }

    // 🔹 SUSCRIPCIÓN
    @PostMapping("/suscripcion")
    public String suscripcion(
            Authentication autenticacion,
            @Valid @RequestBody SolicitudSuscripcion solicitud
    ) {
        return servicioUsuario.subscribe(
                autenticacion.getName(),
                solicitud.getPlanId()
        );
    }
}