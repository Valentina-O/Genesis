package org.valeneisa.Controladores;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.valeneisa.Dtos.ConversorSolicitud;
import org.valeneisa.Dtos.CreditoSolicitud;
import org.valeneisa.Dtos.IMCSolicitud;
import org.valeneisa.Dtos.SolicitudSueno;
import org.valeneisa.Operaciones.CalculadoraCredito;
import org.valeneisa.Operaciones.CalculadoraIMC;
import org.valeneisa.Operaciones.CalculadoraSueno;
import org.valeneisa.Operaciones.ConversorMoneda;
import org.valeneisa.Servicios.ServicioUsuario;

@RestController
@RequestMapping("/api/v1/operaciones")
@RequiredArgsConstructor
public class ControladorOperacion {

    private final ServicioUsuario servicioUsuario;

    @PostMapping("/imc")
    public Object imc(Authentication autenticacion,
                      @Valid @RequestBody IMCSolicitud solicitud) {

        validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());

        return servicioUsuario.ejecutarOperacion(
                autenticacion.getName(),
                solicitud,
                new CalculadoraIMC()
        );
    }

    @PostMapping("/credito")
    public Object credito(Authentication autenticacion,
                          @Valid @RequestBody CreditoSolicitud solicitud) {

        validarPositivo(solicitud.getPrecio(),
                solicitud.getCuotas(),
                solicitud.getTasaMensual());

        return servicioUsuario.ejecutarOperacion(
                autenticacion.getName(),
                solicitud,
                new CalculadoraCredito()
        );
    }

    @PostMapping("/sueno")
    public Object sueno(Authentication autenticacion,
                        @Valid @RequestBody SolicitudSueno solicitud) {

        if (solicitud.getModo() == null || solicitud.getHoraReferencia() == null) {
            throw new RuntimeException("Modo y hora son obligatorios");
        }

        return servicioUsuario.ejecutarOperacion(
                autenticacion.getName(),
                solicitud,
                new CalculadoraSueno()
        );
    }

    @PostMapping("/moneda")
    public Object moneda(Authentication autenticacion,
                         @Valid @RequestBody ConversorSolicitud solicitud) {

        validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());

        return servicioUsuario.ejecutarOperacion(
                autenticacion.getName(),
                solicitud,
                new ConversorMoneda()
        );
    }

    private void validarPositivo(double... valores) {
        for (double valor : valores) {
            if (valor <= 0) {
                throw new RuntimeException("Los valores deben ser mayores a cero");
            }
        }
    }
}