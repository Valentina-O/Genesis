package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.IMCSolicitud;
import org.valeneisa.Dtos.IMCRespuesta;
import org.valeneisa.Util.MatematicasUtil;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de calcular el Índice de Masa Corporal (IMC).
 * Implementa la interfaz IOperacion para procesar solicitudes de tipo IMCSolicitud
 * y retornar una respuesta de tipo IMCRespuesta.
 */
@Component
public class CalculadoraIMC implements IOperacion<IMCSolicitud, IMCRespuesta> {

    /**
     * Retorna el código único de la operación.
     *
     * @return Código de la operación.
     */
    @Override
    public String obtenerCodigoOp() {
        return "OP-03";
    }

    /**
     * Retorna el costo base en tokens de esta operación.
     *
     * @return Costo base en tokens.
     */
    @Override
    public int obtenerCostoBase() {
        return 15;
    }

    /**
     * Ejecuta el cálculo del IMC con base en la solicitud recibida.
     * Calcula el índice, la categoría, el rango de peso saludable
     * y la diferencia respecto al peso actual.
     *
     * @param solicitud Datos necesarios para calcular el IMC.
     * @return Respuesta con los resultados del cálculo.
     */
    @Override
    public IMCRespuesta ejecutar(IMCSolicitud solicitud) {
        // La validación positiva ya la hace el controlador,
        // pero dejarla aquí como refuerzo es buena práctica.
        if (solicitud.getPesoKg() <= 0 || solicitud.getAlturaCm() <= 0) {
            throw new RuntimeException("El peso y la altura deben ser mayores a cero.");
        }

        double alturaM = solicitud.getAlturaCm() / 100.0;
        double imc = solicitud.getPesoKg() / Math.pow(alturaM, 2);

        double pesoMin = 18.5 * Math.pow(alturaM, 2);
        double pesoMax = 24.9 * Math.pow(alturaM, 2);

        return IMCRespuesta.builder()
                .imc(MatematicasUtil.redondear(imc))
                .categoria(definirCategoria(imc))
                .pesoMinSaludable(MatematicasUtil.redondear(pesoMin))
                .pesoMaxSaludable(MatematicasUtil.redondear(pesoMax))
                .diferenciaPeso(calcularDiferencia(solicitud.getPesoKg(), pesoMin, pesoMax))
                .build();
    }

    /**
     * Determina la categoría del IMC según su valor.
     *
     * @param imc Valor calculado del índice de masa corporal.
     * @return Categoría correspondiente.
     */
    private String definirCategoria(double imc) {
        if (imc < 18.5) return "Bajo peso";
        if (imc < 25.0) return "Peso normal";
        if (imc < 30.0) return "Sobrepeso";
        return "Obesidad";
    }

    /**
     * Calcula la diferencia entre el peso actual y el rango saludable.
     *
     * @param actual Peso actual del usuario.
     * @param min Peso mínimo saludable.
     * @param max Peso máximo saludable.
     * @return Diferencia de peso redondeada.
     */
    private double calcularDiferencia(double actual, double min, double max) {
        if (actual < min) return MatematicasUtil.redondear(min - actual);
        if (actual > max) return MatematicasUtil.redondear(actual - max);
        return 0.0;
    }
}