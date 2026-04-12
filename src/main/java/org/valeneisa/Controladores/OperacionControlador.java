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

    @PostMapping("/sueno")
    public RespuestaSueno calcularSueno(
            Authentication auth,
            @Valid @RequestBody SolicitudSueno solicitud) {

        return servicioUsuario.ejecutarOperacion(
                auth.getName(),
                solicitud,
                new CalculadoraSueno()
        );
    }

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

    private void validarPositivo(double... valores) {
        for (double v : valores) {
            if (v <= 0) {
                throw new RuntimeException("Valores deben ser mayores a cero");
            }
        }
    }
}