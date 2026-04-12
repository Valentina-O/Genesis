package org.valeneisa.Core;

public interface IOperacion<T_REQ, T_RES> {
    String obtenerCodigoOp();
    int obtenerCostoBase();
    T_RES ejecutar(T_REQ solicitud);
}