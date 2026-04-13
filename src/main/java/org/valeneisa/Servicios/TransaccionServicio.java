package org.valeneisa.Servicios;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.tokens.Transaccion;
import org.valeneisa.usuario.entidad.Usuario;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransaccionServicio {
    private final ITransaccionRepositorio transaccionRepo;

    public void registrarConsumo(Usuario usuario, Operacion operacion, Integer costo) {
        Transaccion t = new Transaccion();
        t.setUsuario(usuario);
        t.setOperacion(operacion);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());
        transaccionRepo.save(t);
    }
}