package org.valeneisa.usuario.repositorio;

import org.springframework.stereotype.Repository;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.*;

@Repository
public class UsuarioMemoriaRepositorio {

    private Map<String, Usuario> usuarios = new HashMap<>();

    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getUsername(), usuario);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return Optional.ofNullable(usuarios.get(username));
    }
}