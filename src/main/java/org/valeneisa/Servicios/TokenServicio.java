package org.valeneisa.Servicios;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.valeneisa.Operaciones.IOperacionRepositorio;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.tokens.Transaccion;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

import java.time.LocalDateTime;

/**
 * Servicio encargado de la gestión de tokens de los usuarios.
 * Controla el consumo de tokens, valida saldos y registra las transacciones.
 */
@Service
@RequiredArgsConstructor
public class TokenServicio {

    /**
     * Repositorio de usuarios.
     */
    private final IUsuarioRepositorio usuarioRepositorio;

    /**
     * Repositorio de transacciones.
     */
    private final ITransaccionRepositorio transaccionRepositorio;

    /**
     * Repositorio de operaciones.
     */
    private final IOperacionRepositorio operacionRepositorio;

    /**
     * Descuenta tokens a un usuario según el costo base de una operación
     * y registra la transacción correspondiente.
     *
     * @param usuario Usuario al que se le descontarán tokens.
     * @param operacion Operación realizada.
     */
    @Transactional
    public void descontarTokens(Usuario usuario, Operacion operacion) {
        int costo = operacion.getCostoBase();

        if (usuario.getTokensDisponibles() < costo) {
            throw new RuntimeException("Saldo insuficiente de tokens");
        }

        // Restar tokens
        usuario.setTokensDisponibles(usuario.getTokensDisponibles() - costo);
        usuarioRepositorio.save(usuario);

        // Registrar la transacción (Lo que ya hiciste)
        Transaccion t = new Transaccion();
        t.setUsuario(usuario);
        t.setOperacion(operacion);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());
        transaccionRepositorio.save(t);
    }

    /**
     * Procesa una transacción manualmente usando el código de operación
     * y un costo determinado.
     *
     * @param usuario Usuario que realiza la operación.
     * @param codigoOp Código de la operación.
     * @param costo Cantidad de tokens a descontar.
     */
    @Transactional
    public void procesarTransaccion(Usuario usuario, String codigoOp, Integer costo) {
        // Validar saldo
        if (usuario.getTokensDisponibles() < costo) {
            throw new RuntimeException("Tokens insuficientes para esta operación");
        }

        // Restar saldo al usuario
        usuario.setTokensDisponibles(usuario.getTokensDisponibles() - costo);
        usuarioRepositorio.save(usuario);

        // Guardar el rastro (Métrica para Juan Pablo)
        Operacion op = operacionRepositorio.findByCodigo(codigoOp)
                .orElseThrow(() -> new RuntimeException("Operación no encontrada"));

        Transaccion t = new Transaccion();
        t.setUsuario(usuario);
        t.setOperacion(op);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());
        transaccionRepositorio.save(t);
    }

    /**
     * Calcula el costo total de una operación.
     * Actualmente devuelve el costo base, pero puede extenderse
     * para incluir recargos adicionales según la lógica de negocio.
     *
     * @param costoBase Costo base de la operación.
     * @param req Objeto de solicitud.
     * @param res Objeto de respuesta.
     * @return Costo total calculado.
     */
    public int calcularCostoTotal(int costoBase, Object req, Object res) {
        // Por ahora, si no hay lógica de recargos extras, devolvemos el costo base
        // Esto quita el error de compilación de inmediato
        return costoBase;
    }
}