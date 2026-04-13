package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.RespuestaPerfilUsuario;
import org.valeneisa.Dtos.SolicitudSuscripcion;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.Servicios.ServicioUsuario;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.tokens.Transaccion;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;
    private final ITransaccionRepositorio transaccionRepositorio;

    @GetMapping("/perfil")
    public RespuestaPerfilUsuario perfil(Authentication auth) {
        return servicioUsuario.getProfile(auth.getName());
    }

    @GetMapping("/historial")
    public List<Transaccion> historial(
            Authentication auth,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamaño
    ) {
        return servicioUsuario.getTransactions(auth.getName(), pagina, tamaño);
    }

    @GetMapping("/catalogo")
    public List<Operacion> catalogo() {
        return servicioUsuario.getCatalogo();
    }

    @PostMapping("/suscripcion")
    public String suscripcion(
            Authentication auth,
            @Valid @RequestBody SolicitudSuscripcion solicitud
    ) {
        return servicioUsuario.subscribe(
                auth.getName(),
                solicitud.getPlanId()
        );
    }
}