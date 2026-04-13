package org.valeneisa.Dtos;

import lombok.Data;

/**
 * DTO que representa una opción de horario de sueño basada en ciclos.
 * Incluye la cantidad de ciclos, la hora calculada para dormir o despertar
 * y la calidad estimada del descanso.
 */
@Data
public class OpcionSueno {

    /**
     * Número de ciclos de sueño considerados.
     */
    private int ciclos;

    /**
     * Hora calculada para dormir o despertar.
     */
    private String horaCalculada;

    /**
     * Nivel de calidad del sueño estimado (por ejemplo: "Mínimo", "Recomendado", "Ideal").
     */
    private String calidad; // "Mínimo", "Recomendado", "Ideal"
}