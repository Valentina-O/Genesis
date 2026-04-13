package org.valeneisa.Servicios;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.tokens.Transaccion;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServicio {
    private final IUsuarioRepositorio usuarioRepo;
    private final ITransaccionRepositorio transaccionRepo;

    @Transactional
    public void descontarTokens(Usuario usuario, Operacion operacion) {
        int costo = operacion.getCostoBase();

        if (usuario.getTokensDisponibles() < costo) {
            throw new RuntimeException("Saldo insuficiente de tokens");
        }

        // Restar tokens
        usuario.setTokensDisponibles(usuario.getTokensDisponibles() - costo);
        usuarioRepo.save(usuario);

        // Registrar la transacción (Lo que ya hiciste)
        Transaccion t = new Transaccion();
        t.setUsuario(usuario);
        t.setOperacion(operacion);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());
        transaccionRepo.save(t);
    }
}