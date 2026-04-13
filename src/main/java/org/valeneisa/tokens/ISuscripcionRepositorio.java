package org.valeneisa.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.Optional;

/**
 * Repositorio para la entidad Suscripcion.
 * Permite realizar operaciones CRUD y consultas específicas
 * relacionadas con las suscripciones de los usuarios.
 */
public interface ISuscripcionRepositorio extends JpaRepository<Suscripcion, Long> {

    /**
     * Busca la suscripción activa de un usuario.
     *
     * @param usuario Usuario a consultar.
     * @return Suscripción activa si existe.
     */
    Optional<Suscripcion> findByUsuarioAndEstaActivaTrue(Usuario usuario);
}