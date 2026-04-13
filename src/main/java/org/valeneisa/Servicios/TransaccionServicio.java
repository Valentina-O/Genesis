package org.valeneisa.Servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.tokens.Transaccion;
import org.valeneisa.usuario.entidad.Usuario;

import java.time.LocalDateTime;

/**
 * Servicio encargado de registrar las transacciones de consumo de tokens.
 * Permite guardar el historial de operaciones realizadas por los usuarios.
 */
@Service
@RequiredArgsConstructor
public class TransaccionServicio {

    /**
     * Repositorio de transacciones.
     */
    private final ITransaccionRepositorio transaccionRepo;

    /**
     * Registra una transacción de consumo de tokens asociada a un usuario y una operación.
     *
     * @param usuario Usuario que realizó la operación.
     * @param operacion Operación ejecutada.
     * @param costo Cantidad de tokens consumidos.
     */
    public void registrarConsumo(Usuario usuario, Operacion operacion, Integer costo) {
        Transaccion t = new Transaccion();
        t.setUsuario(usuario);
        t.setOperacion(operacion);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());
        transaccionRepo.save(t);
    }
}