package org.valeneisa.Dtos;

import lombok.Data;

@Data
public class CreditoSolicitud {
    private double precio;
    private int cuotas;
    private double tasaMensual;
}