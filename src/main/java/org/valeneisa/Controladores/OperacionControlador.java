package org.valeneisa.Controladores;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Operaciones.*;
import org.valeneisa.Dtos.*;
import org.valeneisa.tokens.ServicioToken;
import lombok.RequiredArgsConstructor;

    @RestController
    @RequestMapping("/api/v1/operaciones")
    @RequiredArgsConstructor
    public class OperacionControlador {

        private final ServicioToken servicioToken;

        // --- OP-03: IMC ---
        @PostMapping("/imc")
        public IMCRespuesta calcularIMC(@RequestBody IMCSolicitud solicitud) {
            validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());

            CalculadoraIMC calc = new CalculadoraIMC();
            IMCRespuesta respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- OP-01: CRÉDITO ---
        @PostMapping("/credito")
        public CreditoSolicitud calcularCredito(@RequestBody CreditoRespuesta solicitud) {
            validarPositivo(solicitud.getPrecio(), solicitud.getCuotas(), solicitud.getTasaMensual());

            CalculadoraCredito calc = new CalculadoraCredito();
            CreditoSolicitud respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- OP-04: SUEÑO ---
        @PostMapping("/sueno")
        public SolicitudSueno calcularSueno(@RequestBody RespuestaSueno solicitud) {
            // Validación lógica: verificar que el modo sea válido
            if (solicitud.getModo() == null || solicitud.getHoraReferencia() == null) {
                throw new RuntimeException("Modo y hora son obligatorios");
            }

            CalculadoraSueno calc = new CalculadoraSueno();
            SolicitudSueno respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- OP-02: CONVERSOR ---
        @PostMapping("/moneda")
        public ConversorRespuesta calcularMoneda(@RequestBody ConversorSolicitud solicitud) {
            validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());

            ConversorMoneda calc = new ConversorMoneda();
            ConversorRespuesta respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- MÉTODOS AUXILIARES PARA LIMPIEZA (DRY) ---

        private void actualizarTokens(Object responseObj, int costoBase, Object req, Object res) {
            int costoTotal = servicioToken.calcularCostoTotal(costoBase, req, res);

            // Usamos reflexión simple o casteos para asignar los tokens
            if (responseObj instanceof IMCRespuesta) ((IMCRespuesta) responseObj).setTokensConsumidos(costoTotal);
            if (responseObj instanceof CreditoSolicitud) ((CreditoSolicitud) responseObj).setTokensConsumidos(costoTotal);
            if (responseObj instanceof SolicitudSueno) ((SolicitudSueno) responseObj).setTokensConsumidos(costoTotal);
            if (responseObj instanceof ConversorRespuesta) ((ConversorRespuesta) responseObj).setTokensConsumidos(costoTotal);
        }

        private void validarPositivo(double... valores) {
            for (double v : valores) {
                if (v <= 0) throw new RuntimeException("Error: Los valores deben ser mayores a cero.");
            }
        }
}
