package org.valeneisa.tokens;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.valeneisa.usuario.entidad.Usuario;

public interface ITransaccionRepositorio extends JpaRepository<Transaccion, Long> {

    Page<Transaccion> findByUsuario(Usuario usuario, Pageable pageable);
}