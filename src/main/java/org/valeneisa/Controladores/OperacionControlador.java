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
 * automáticamente los tokens correspondientes al usuario autenticado y registra
 * la transacción en el sistema.
 * </p>
 *
 * @see TokenServicio
 * @see ITransaccionRepositorio
 */
@RestController
@RequestMapping("/api/v1/operaciones")
@RequiredArgsConstructor
public class OperacionControlador {

    private final TokenServicio servicioToken;
    private final ITransaccionRepositorio transaccionRepositorio;

    /**
     * Calcula el Índice de Masa Corporal (IMC) del usuario (OP-03).
     * <p>
     * Valida que el peso y la altura sean valores positivos antes de ejecutar
     * el cálculo. El costo en tokens es descontado y registrado automáticamente.
     * </p>
     *
     * @param solicitud objeto {@link IMCSolicitud} con el peso en kg y la altura en cm.
     * @return {@link IMCRespuesta} con el resultado del IMC y los tokens consumidos.
     */
    @PostMapping("/imc")
    public IMCRespuesta calcularIMC(@RequestBody IMCSolicitud solicitud) {
        validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());
        CalculadoraIMC calc = new CalculadoraIMC();
        IMCRespuesta respuesta = calc.ejecutar(solicitud);

        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-03");
        return respuesta;
    }

    /**
     * Calcula el plan de amortización de un crédito (OP-01).
     * <p>
     * Valida que el precio, número de cuotas y tasa mensual sean valores positivos
     * antes de ejecutar el cálculo. El costo en tokens es descontado y registrado automáticamente.
     * </p>
     *
     * @param solicitud objeto {@link CreditoSolicitud} con el precio, cuotas y tasa mensual.
     * @return {@link CreditoRespuesta} con el detalle del crédito y los tokens consumidos.
     */
    @PostMapping("/credito")
    public CreditoRespuesta calcularCredito(@RequestBody CreditoSolicitud solicitud) {
        validarPositivo(solicitud.getPrecio(), (double) solicitud.getCuotas(), solicitud.getTasaMensual());
        CalculadoraCredito calc = new CalculadoraCredito();
        CreditoRespuesta respuesta = calc.ejecutar(solicitud);

        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-01");
        return respuesta;
    }

    /**
     * Calcula los ciclos de sueño recomendados según el modo y hora de referencia (OP-04).
     * <p>
     * Requiere que el modo y la hora de referencia estén presentes en la solicitud.
     * El costo en tokens es descontado y registrado automáticamente.
     * </p>
     *
     * @param solicitud objeto {@link SolicitudSueno} con el modo de cálculo y la hora de referencia.
     * @return {@link RespuestaSueno} con los ciclos recomendados y los tokens consumidos.
     * @throws RuntimeException si el modo o la hora de referencia son nulos.
     */
    @PostMapping("/sueno")
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
     * Realiza la conversión de una cantidad monetaria entre divisas (OP-02).
     * <p>
     * Valida que el monto y la tasa sugerida sean valores positivos antes de ejecutar
     * la conversión. El costo en tokens es descontado y registrado automáticamente.
     * </p>
     *
     * @param solicitud objeto {@link ConversorSolicitud} con el monto y la tasa de cambio sugerida.
     * @return {@link ConversorRespuesta} con el resultado de la conversión y los tokens consumidos.
     */
    @PostMapping("/moneda")
    public ConversorRespuesta calcularMoneda(@RequestBody ConversorSolicitud solicitud) {
        validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());
        ConversorMoneda calc = new ConversorMoneda();
        ConversorRespuesta respuesta = calc.ejecutar(solicitud);

        actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta, "OP-02");
        return respuesta;
    }

    /**
     * Calcula, cobra y registra el costo en tokens de una operación ejecutada.
     * <p>
     * Obtiene el usuario autenticado desde el contexto de seguridad, calcula el costo
     * total de la operación, procesa el cobro y asigna los tokens consumidos al objeto
     * de respuesta correspondiente.
     * </p>
     *
     * @param responseObj objeto de respuesta de la operación; debe ser una instancia de
     *                    {@link IMCRespuesta}, {@link CreditoRespuesta}, {@link RespuestaSueno}
     *                    o {@link ConversorRespuesta}.
     * @param costoBase   costo base en tokens definido por la calculadora de la operación.
     * @param req         objeto de solicitud original de la operación.
     * @param res         objeto de respuesta original de la operación.
     * @param codigoOp    código identificador de la operación (por ejemplo, {@code "OP-01"}).
     * @throws RuntimeException si el principal en el contexto de seguridad no es una instancia de {@link Usuario}.
     */
    private void actualizarTokens(Object responseObj, int costoBase, Object req, Object res, String codigoOp) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario)) {
            throw new RuntimeException("Usuario no autenticado correctamente");
        }
        Usuario usuario = (Usuario) principal;

        int costoTotal = servicioToken.calcularCostoTotal(costoBase, req, res);

        servicioToken.procesarTransaccion(usuario, codigoOp, costoTotal);

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
     * Valida que todos los valores numéricos recibidos sean estrictamente positivos.
     *
     * @param valores uno o más valores {@code double} a validar.
     * @throws RuntimeException si alguno de los valores es menor o igual a cero.
     */
    private void validarPositivo(double... valores) {
        for (double v : valores) {
            if (v <= 0) throw new RuntimeException("Error: Los valores deben ser mayores a cero.");
        }
    }

    /**
     * Retorna el historial completo de transacciones del usuario autenticado,
     * ordenado de la más reciente a la más antigua.
     *
     * @return lista de {@link Transaccion} asociadas al usuario en sesión,
     *         ordenadas de forma descendente por fecha.
     */
    @GetMapping("/mis-movimientos")
    public List<Transaccion> obtenerHistorial() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transaccionRepositorio.findByUsuarioOrderByFechaDesc(usuario);
    }
}