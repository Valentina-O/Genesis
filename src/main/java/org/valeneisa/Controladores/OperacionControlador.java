package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.*;
import org.valeneisa.Operaciones.*;
import org.valeneisa.Servicios.ServicioUsuario;

@RestController
@RequestMapping("/api/v1/operaciones")
@RequiredArgsConstructor
public class OperacionControlador {

    private final ServicioUsuario servicioUsuario;

    // --- OP-03: IMC ---
    @PostMapping("/imc")
    public IMCRespuesta calcularIMC(
            Authentication auth,
            @Valid @RequestBody IMCSolicitud solicitud) {

        validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());

        return servicioUsuario.ejecutarOperacion(
                auth.getName(),
                solicitud,
                new CalculadoraIMC()
        );
    }

    // --- OP-01: CRÉDITO ---
    @PostMapping("/credito")
    public CreditoRespuesta calcularCredito(
            Authentication auth,
            @Valid @RequestBody CreditoSolicitud solicitud) {

        validarPositivo(
                solicitud.getPrecio(),
                solicitud.getCuotas(),
                solicitud.getTasaMensual()
        );

        return servicioUsuario.ejecutarOperacion(
                auth.getName(),
                solicitud,
                new CalculadoraCredito()
        );
    }

    // --- OP-04: SUEÑO ---
    @PostMapping("/sueno")
    public RespuestaSueno calcularSueno(
            Authentication auth,
            @Valid @RequestBody SolicitudSueno solicitud) {

        // Nota: En sueño no validamos positivo porque la hora puede ser 0
        return servicioUsuario.ejecutarOperacion(
                auth.getName(),
                solicitud,
                new CalculadoraSueno()
        );
    }

    // --- OP-02: CONVERSOR ---
    @PostMapping("/moneda")
    public ConversorRespuesta calcularMoneda(
            Authentication auth,
            @Valid @RequestBody ConversorSolicitud solicitud) {

        validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());

        return servicioUsuario.ejecutarOperacion(
                auth.getName(),
                solicitud,
                new ConversorMoneda()
        );
    }

    // --- MÉTODOS AUXILIARES ---
    private void validarPositivo(double... valores) {
        for (double v : valores) {
            if (v <= 0) {
                throw new RuntimeException("Error: Los valores deben ser mayores a cero.");
            }
        }
    }
}