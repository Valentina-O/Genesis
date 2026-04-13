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

@Service
@RequiredArgsConstructor
public class TokenServicio {
    private final IUsuarioRepositorio usuarioRepositorio;
    private final ITransaccionRepositorio transaccionRepositorio;
    private final IOperacionRepositorio operacionRepositorio;

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
    public int calcularCostoTotal(int costoBase, Object req, Object res) {
        // Por ahora, si no hay lógica de recargos extras, devolvemos el costo base
        // Esto quita el error de compilación de inmediato
        return costoBase;
    }
}