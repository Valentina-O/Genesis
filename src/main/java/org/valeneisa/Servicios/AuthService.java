package org.valeneisa.Servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.valeneisa.Dtos.Autenticacion.SolicitudLogin;
import org.valeneisa.Dtos.Autenticacion.SolicitudRegistro;
import org.valeneisa.Seguridad.JwtUtil;
import org.valeneisa.usuario.entidad.Rol;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.UsuarioMemoriaRepositorio;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UsuarioMemoriaRepositorio usuarioRepo;

    public AuthService(JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       UsuarioMemoriaRepositorio usuarioRepo) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepo = usuarioRepo;
    }

    public String login(SolicitudLogin request) {

        Usuario usuario = usuarioRepo.buscarPorUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!usuario.getEstaActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return jwtUtil.generarToken(usuario.getUsuario(), usuario.getRolUsuario().name());
    }

    public String register(SolicitudRegistro request) {

        if (usuarioRepo.buscarPorUsuario(request.getUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        String passwordEncriptado = passwordEncoder.encode(request.getContrasena());

        Usuario usuario = new Usuario();
        usuario.setUsuario(request.getUsuario());
        usuario.setCorreoElectronico(request.getCorreo());
        usuario.setContrasena(passwordEncriptado);
        usuario.setRolUsuario(Rol.USER);
        usuario.setTokensDisponibles(0);
        usuario.setEstaActivo(true);

        usuarioRepo.guardar(usuario);

        return jwtUtil.generarToken(usuario.getUsuario(), usuario.getRolUsuario().name());
    }
}