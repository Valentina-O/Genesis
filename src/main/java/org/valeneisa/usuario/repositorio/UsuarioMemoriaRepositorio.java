package org.valeneisa.usuario.repositorio;

import org.springframework.stereotype.Repository;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UsuarioMemoriaRepositorio {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getUsuario(), usuario);
    }

    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return Optional.ofNullable(usuarios.get(usuario));
    }
}