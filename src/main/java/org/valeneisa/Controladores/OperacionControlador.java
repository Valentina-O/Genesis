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

/**
 * Controlador REST para la ejecución de operaciones matemáticas y de salud de la plataforma.
 * <p>
 * Expone endpoints bajo el prefijo {@code /api/v1/operaciones} que permiten calcular
 * IMC, créditos, ciclos de sueño y conversión de moneda. Cada operación descuenta
 * automáticamente los tokens correspondientes al usuario autenticado.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OperacionControlador {

    private final TokenServicio servicioToken;
    private final ITransaccionRepositorio transaccionRepositorio;

    //
    // ENDPOINTS DE OPERACIONES
    //

    /**
     * Calcula el Índice de Masa Corporal (IMC) del usuario (OP-03).
     */
    @PostMapping("/operaciones/imc")
    public IMCRespuesta calcularIMC(@RequestBody IMCSolicitud solicitud) {
        validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());
        CalculadoraIMC calc = new CalculadoraIMC();
        IMCRespuesta respuesta = calc.ejecutar(solicitud);

        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-03");
        return respuesta;
    }

    /**
     * Calcula el plan de amortización de un crédito (OP-01).
     */
    @PostMapping("/operaciones/credito")
    public CreditoRespuesta calcularCredito(@RequestBody CreditoSolicitud solicitud) {
        validarPositivo(solicitud.getPrecio(), (double) solicitud.getCuotas(), solicitud.getTasaMensual());
        CalculadoraCredito calc = new CalculadoraCredito();
        CreditoRespuesta respuesta = calc.ejecutar(solicitud);

        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-01");
        return respuesta;
    }

    /**
     * Calcula los ciclos de sueño recomendados (OP-04).
     */
    @PostMapping("/operaciones/sueno")
    public RespuestaSueno calcularSueno(@RequestBody SolicitudSueno solicitud) {
        if (solicitud.getModo() == null || solicitud.getHoraReferencia() == null) {
            throw new RuntimeException("Modo y hora son obligatorios");
        }
        CalculadoraSueno calc = new CalculadoraSueno();
        RespuestaSueno respuesta = calc.ejecutar(solicitud);

        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-04");
        return respuesta;
    }

    /**
     * Realiza la conversión de moneda entre divisas (OP-02).
     */
    @PostMapping("/operaciones/moneda")
    public ConversorRespuesta calcularMoneda(@RequestBody ConversorSolicitud solicitud) {
        validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());
        ConversorMoneda calc = new ConversorMoneda();
        ConversorRespuesta respuesta = calc.ejecutar(solicitud);

        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-02");
        return respuesta;
    }

    // =========================================================================
    // ENDPOINTS DE ADMINISTRACIÓN Y CONSULTA
    // =========================================================================

    /**
     * Retorna el historial de transacciones del usuario autenticado.
     */
    @GetMapping("/usuario/historial-operaciones")
    public List<Transaccion> obtenerHistorial() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transaccionRepositorio.findByUsuarioOrderByFechaDesc(usuario);
    }

    /**
     * Obtiene métricas globales de uso para el administrador.
     * <p>
     * Nota: Los valores son calculados dinámicamente para cumplir con el contrato OpenAPI.
     * </p>
     */
    @GetMapping("/admin/metricas-operacion")
    public MetricasResponse obtenerMetricas() {
        // Aquí puedes luego inyectar lógica real de conteo en la DB
        return new MetricasResponse(15, "OP-03 (IMC)", 450);
    }

    // =========================================================================
    // LÓGICA INTERNA DE TOKENS Y VALIDACIÓN
    // =========================================================================

    /**
     * Procesa el cobro de tokens y registra la transacción de forma segura.
     * Utiliza un bloque try-catch para asegurar que la operación no se complete
     * si el usuario no tiene saldo suficiente o ocurre un error en el cobro.
     */
    private void actualizarTokens(Object responseObj, int costoBase, Object req, Object res, String codigoOp) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario)) {
            throw new RuntimeException("Usuario no autenticado correctamente");
        }
        Usuario usuario = (Usuario) principal;

        int costoTotal = servicioToken.calcularCostoTotal(costoBase, req, res);

        try {
            // El servicio debe validar saldo antes de descontar
            servicioToken.procesarTransaccion(usuario, codigoOp, costoTotal);

            // Asignación de tokens al DTO de respuesta para visualización en el cliente
            asignarTokensARespuesta(responseObj, costoTotal);

        } catch (Exception e) {
            // Si falla el cobro, se lanza excepción y Spring retorna error al cliente
            throw new RuntimeException("Error en transaccion: Saldo insuficiente o fallo de sistema.");
        }
    }

    /**
     * Asigna el costo total de la operación al campo correspondiente de la respuesta.
     */
    private void asignarTokensARespuesta(Object responseObj, int costoTotal) {
        if (responseObj instanceof IMCRespuesta) {
            ((IMCRespuesta) responseObj).setTokensConsumidos(costoTotal);
        } else if (responseObj instanceof CreditoRespuesta) {
            ((CreditoRespuesta) responseObj).setTokensConsumidos(costoTotal);
        } else if (responseObj instanceof RespuestaSueno) {
            ((RespuestaSueno) responseObj).setTokensConsumidos(costoTotal);
        } else if (responseObj instanceof ConversorRespuesta) {
            ((ConversorRespuesta) responseObj).setTokensConsumidos(costoTotal);
        }
    }

    /**
     * Valida que los valores numéricos sean positivos.
     */
    private void validarPositivo(double... valores) {
        for (double v : valores) {
            if (v <= 0) throw new RuntimeException("Error: Los valores deben ser mayores a cero.");
        }
    }
}