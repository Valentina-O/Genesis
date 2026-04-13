package org.valeneisa.Controladores;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Operaciones.*;
import org.valeneisa.Dtos.*;
import org.valeneisa.Servicios.TokenServicio;
import org.valeneisa.tokens.ITransaccionRepositorio;
import lombok.RequiredArgsConstructor;
import org.valeneisa.tokens.Transaccion;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operaciones")
@RequiredArgsConstructor
public class OperacionControlador {

    private final TokenServicio servicioToken;
    private final ITransaccionRepositorio transaccionRepositorio;

    // --- OP-03: IMC ---
    @PostMapping("/imc")
    public IMCRespuesta calcularIMC(@RequestBody IMCSolicitud solicitud) {
        validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());
        CalculadoraIMC calc = new CalculadoraIMC();
        IMCRespuesta respuesta = calc.ejecutar(solicitud);

        // Agregamos el código "OP-03" al final
        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-03");
        return respuesta;
    }

    // --- OP-01: CRÉDITO ---
    @PostMapping("/credito")
    public CreditoSolicitud calcularCredito(@RequestBody CreditoRespuesta solicitud) {
        validarPositivo(solicitud.getPrecio(), solicitud.getCuotas(), solicitud.getTasaMensual());
        CalculadoraCredito calc = new CalculadoraCredito();
        CreditoSolicitud respuesta = calc.ejecutar(solicitud);

        // Agregamos el código "OP-01"
        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-01");
        return respuesta;
    }

    // --- OP-04: SUEÑO ---
    @PostMapping("/sueno")
    public SolicitudSueno calcularSueno(@RequestBody RespuestaSueno solicitud) {
        if (solicitud.getModo() == null || solicitud.getHoraReferencia() == null) {
            throw new RuntimeException("Modo y hora son obligatorios");
        }
        CalculadoraSueno calc = new CalculadoraSueno();
        SolicitudSueno respuesta = calc.ejecutar(solicitud);

        // Agregamos el código "OP-04"
        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-04");
        return respuesta;
    }

    // --- OP-02: CONVERSOR ---
    @PostMapping("/moneda")
    public ConversorRespuesta calcularMoneda(@RequestBody ConversorSolicitud solicitud) {
        validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());
        ConversorMoneda calc = new ConversorMoneda();
        ConversorRespuesta respuesta = calc.ejecutar(solicitud);

        // Agregamos el código "OP-02"
        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-02");
        return respuesta;
    }

    private void actualizarTokens(Object responseObj, int costoBase, Object req, Object res, String codigoOp) {
        // 1. Obtener usuario
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Calcular costo
        int costoTotal = servicioToken.calcularCostoTotal(costoBase, req, res);

        // 3. Procesar cobro y guardar (Trabajo de Juan Pablo)
        servicioToken.procesarTransaccion(usuario, codigoOp, costoTotal);

        // 4. Asignar tokens consumidos al objeto de respuesta (Casteos corregidos)
        if (responseObj instanceof IMCRespuesta) {
            ((IMCRespuesta) responseObj).setTokensConsumidos(costoTotal);
        } else if (responseObj instanceof CreditoSolicitud) {
            ((CreditoSolicitud) responseObj).setTokensConsumidos(costoTotal);
        } else if (responseObj instanceof SolicitudSueno) {
            ((SolicitudSueno) responseObj).setTokensConsumidos(costoTotal);
        } else if (responseObj instanceof ConversorRespuesta) {
            ((ConversorRespuesta) responseObj).setTokensConsumidos(costoTotal);
        }
    }

    private void validarPositivo(double... valores) {
        for (double v : valores) {
            if (v <= 0) throw new RuntimeException("Error: Los valores deben ser mayores a cero.");
        }
    }

    @GetMapping("/mis-movimientos")
    public List<Transaccion> obtenerHistorial() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transaccionRepositorio.findByUsuarioOrderByFechaDesc(usuario);
    }

}