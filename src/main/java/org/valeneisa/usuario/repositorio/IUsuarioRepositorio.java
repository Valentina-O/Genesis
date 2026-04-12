package org.valeneisa.usuario.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.Optional;

public interface IUsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    Optional<Usuario> findByUsuario(String usuario);
}