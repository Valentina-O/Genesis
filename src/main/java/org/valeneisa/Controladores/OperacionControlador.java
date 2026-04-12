package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Operaciones.*;
import org.valeneisa.Dtos.*;
import org.valeneisa.Servicios.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/operaciones")
@RequiredArgsConstructor
public class OperacionControlador {

    private final UserService userService;

    @PostMapping("/imc")
    public Object calcularIMC(Authentication auth, @Valid @RequestBody IMCRequest solicitud) {
        validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());

        CalculadoraIMC calc = new CalculadoraIMC();

        return userService.ejecutarOperacion(
                auth.getName(),
                solicitud,
                calc
        );
    }

    @PostMapping("/credito")
    public Object calcularCredito(Authentication auth, @Valid @RequestBody CreditoRequest solicitud) {
        validarPositivo(solicitud.getPrecio(), solicitud.getCuotas(), solicitud.getTasaMensual());

        CalculadoraCredito calc = new CalculadoraCredito();

        return userService.ejecutarOperacion(
                auth.getName(),
                solicitud,
                calc
        );
    }

    @PostMapping("/sueno")
    public Object calcularSueno(Authentication auth, @Valid @RequestBody SuenoRequest solicitud) {

        if (solicitud.getModo() == null || solicitud.getHoraReferencia() == null) {
            throw new RuntimeException("Modo y hora son obligatorios");
        }

        CalculadoraSueno calc = new CalculadoraSueno();

        return userService.ejecutarOperacion(
                auth.getName(),
                solicitud,
                calc
        );
    }

    @PostMapping("/moneda")
    public Object calcularMoneda(Authentication auth, @Valid @RequestBody ConversorRequest solicitud) {
        validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());

        ConversorMoneda calc = new ConversorMoneda();

        return userService.ejecutarOperacion(
                auth.getName(),
                solicitud,
                calc
        );
    }

    private void validarPositivo(double... valores) {
        for (double v : valores) {
            if (v <= 0) throw new RuntimeException("Valores deben ser mayores a cero");
        }
    }
}