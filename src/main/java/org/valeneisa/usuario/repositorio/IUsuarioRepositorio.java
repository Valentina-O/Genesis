package org.valeneisa.usuario.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Permite realizar operaciones CRUD y consultas específicas
 * relacionadas con los usuarios del sistema.
 */
@Repository
public interface IUsuarioRepositorio extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correoElectronico Correo del usuario.
     * @return Usuario encontrado (si existe).
     */
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param usuario Nombre de usuario.
     * @return Usuario encontrado (si existe).
     */
    Optional<Usuario> findByUsuario(String usuario);
}