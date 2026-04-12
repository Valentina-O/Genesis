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
        public IMCResponse calcularIMC(@RequestBody IMCRequest solicitud) {
            validarPositivo(solicitud.getPesoKg(), solicitud.getAlturaCm());

            CalculadoraIMC calc = new CalculadoraIMC();
            IMCResponse respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- OP-01: CRÉDITO ---
        @PostMapping("/credito")
        public CreditoResponse calcularCredito(@RequestBody CreditoRequest solicitud) {
            validarPositivo(solicitud.getPrecio(), solicitud.getCuotas(), solicitud.getTasaMensual());

            CalculadoraCredito calc = new CalculadoraCredito();
            CreditoResponse respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- OP-04: SUEÑO ---
        @PostMapping("/sueno")
        public SuenoResponse calcularSueno(@RequestBody SuenoRequest solicitud) {
            // Validación lógica: verificar que el modo sea válido
            if (solicitud.getModo() == null || solicitud.getHoraReferencia() == null) {
                throw new RuntimeException("Modo y hora son obligatorios");
            }

            CalculadoraSueno calc = new CalculadoraSueno();
            SuenoResponse respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- OP-02: CONVERSOR ---
        @PostMapping("/moneda")
        public ConversorResponse calcularMoneda(@RequestBody ConversorRequest solicitud) {
            validarPositivo(solicitud.getMonto(), solicitud.getTasaSugerida());

            ConversorMoneda calc = new ConversorMoneda();
            ConversorResponse respuesta = calc.ejecutar(solicitud);

            actualizarTokens(respuesta, calc.obtenerCostoBase(), solicitud, respuesta);
            return respuesta;
        }

        // --- MÉTODOS AUXILIARES PARA LIMPIEZA (DRY) ---

        private void actualizarTokens(Object responseObj, int costoBase, Object req, Object res) {
            int costoTotal = servicioToken.calcularCostoTotal(costoBase, req, res);

            // Usamos reflexión simple o casteos para asignar los tokens
            if (responseObj instanceof IMCResponse) ((IMCResponse) responseObj).setTokensConsumidos(costoTotal);
            if (responseObj instanceof CreditoResponse) ((CreditoResponse) responseObj).setTokensConsumidos(costoTotal);
            if (responseObj instanceof SuenoResponse) ((SuenoResponse) responseObj).setTokensConsumidos(costoTotal);
            if (responseObj instanceof ConversorResponse) ((ConversorResponse) responseObj).setTokensConsumidos(costoTotal);
        }

        private void validarPositivo(double... valores) {
            for (double v : valores) {
                if (v <= 0) throw new RuntimeException("Error: Los valores deben ser mayores a cero.");
            }
        }
}
