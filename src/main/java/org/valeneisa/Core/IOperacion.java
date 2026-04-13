package org.valeneisa.Core;

/**
 * Contrato base para todas las operaciones de la plataforma.
 * <p>
 * Define la estructura genérica que debe implementar cada operación,
 * garantizando que exponga su código identificador, su costo base en tokens
 * y la lógica de ejecución tipada por solicitud y respuesta.
 * </p>
 *
 * @param <T_REQ> tipo del objeto de solicitud que recibe la operación.
 * @param <T_RES> tipo del objeto de respuesta que retorna la operación.
 */
public interface IOperacion<T_REQ, T_RES> {

    /**
     * Retorna el código identificador único de la operación (por ejemplo, {@code "OP-01"}).
     *
     * @return cadena con el código de la operación.
     */
    String obtenerCodigoOp();

    /**
     * Retorna el costo base en tokens que tiene la operación antes de aplicar
     * cualquier factor adicional de cálculo.
     *
     * @return número entero que representa el costo base en tokens.
     */
    int obtenerCostoBase();

    /**
     * Ejecuta la lógica de la operación a partir de los datos de la solicitud.
     *
     * @param solicitud objeto de tipo {@code T_REQ} con los datos de entrada requeridos.
     * @return objeto de tipo {@code T_RES} con el resultado de la operación.
     */
    T_RES ejecutar(T_REQ solicitud);
}