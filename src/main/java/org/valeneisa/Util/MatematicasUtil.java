package org.valeneisa.Util;

/**
 * Clase utilitaria para operaciones matemáticas comunes.
 * Actualmente proporciona métodos para redondeo de valores.
 */
public class MatematicasUtil {

    /**
     * Redondea un número decimal a dos cifras decimales.
     *
     * @param valor Valor a redondear.
     * @return Valor redondeado a dos decimales.
     */
    public static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}