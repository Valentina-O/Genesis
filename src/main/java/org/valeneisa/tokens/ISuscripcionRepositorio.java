package org.valeneisa.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.Optional;

public interface ISuscripcionRepositorio extends JpaRepository<Suscripcion, Long> {

    Optional<Suscripcion> findByUsuarioAndEstaActivaTrue(Usuario usuario);
}