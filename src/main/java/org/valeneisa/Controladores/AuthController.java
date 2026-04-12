package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.autenticación.AuthResponse;
import org.valeneisa.Dtos.autenticación.LoginRequest;
import org.valeneisa.Dtos.autenticación.RegisterRequest;
import org.valeneisa.Servicios.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest request) {
        String token = authService.register(request);
        return new AuthResponse(token);
    }
}