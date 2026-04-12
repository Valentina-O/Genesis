package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.SubscribeRequest;
import org.valeneisa.Dtos.UserProfileResponse;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.Servicios.UserService;
import org.valeneisa.tokens.Transaccion;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 🔹 PERFIL
    @GetMapping("/profile")
    public UserProfileResponse getProfile(Authentication auth) {
        return userService.getProfile(auth.getName());
    }

    // 🔹 HISTORIAL
    @GetMapping( "/historial")
    public List<Transaccion> getTransactions(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getTransactions(auth.getName(), page, size);
    }

    // 🔹 CATÁLOGO
    @GetMapping("/catalogo")
    public List<Operacion> getCatalogo() {
        return userService.getCatalogo();
    }

    // 🔹 SUSCRIPCIÓN
    @PostMapping("/subscribe")
    public String subscribe(
            Authentication auth,
            @Valid @RequestBody SubscribeRequest request
    ) {
        return userService.subscribe(auth.getName(), request.getPlanId());
    }
}