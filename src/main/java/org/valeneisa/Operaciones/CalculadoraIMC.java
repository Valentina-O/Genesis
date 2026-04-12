package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.IMCRequest;
import org.valeneisa.Dtos.IMCResponse;
import org.valeneisa.Util.MatematicasUtil;

public class CalculadoraIMC implements IOperacion<IMCRequest, IMCResponse> {

    @Override
    public String obtenerCodigoOp() { return "OP-03"; }

    @Override
    public int obtenerCostoBase() { return 15; }

    @Override
    public IMCResponse ejecutar(IMCRequest solicitud) {
        // Validación Lógica
        if (solicitud.getPesoKg() <= 0 || solicitud.getAlturaCm() <= 0) {
            throw new RuntimeException("El peso y la altura deben ser mayores a cero.");
        }

        double alturaM = solicitud.getAlturaCm() / 100.0;
        double imc = solicitud.getPesoKg() / Math.pow(alturaM, 2);

        double pesoMin = 18.5 * Math.pow(alturaM, 2);
        double pesoMax = 24.9 * Math.pow(alturaM, 2);

        return IMCResponse.builder()
                .imc(MatematicasUtil.redondear(imc))
                .categoria(definirCategoria(imc))
                .pesoMinSaludable(MatematicasUtil.redondear(pesoMin))
                .pesoMaxSaludable(MatematicasUtil.redondear(pesoMax))
                .diferenciaPeso(calcularDiferencia(solicitud.getPesoKg(), pesoMin, pesoMax))
                .build();
    }

    private String definirCategoria(double imc) {
        if (imc < 18.5) return "Bajo peso";
        if (imc < 25.0) return "Peso normal";
        if (imc < 30.0) return "Sobrepeso";
        return "Obesidad";
    }

    private double calcularDiferencia(double actual, double min, double max) {
        if (actual < min) return MatematicasUtil.redondear(min - actual);
        if (actual > max) return MatematicasUtil.redondear(actual - max);
        return 0.0;
    }
}