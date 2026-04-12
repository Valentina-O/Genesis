package org.valeneisa.Servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.valeneisa.Dtos.autenticación.LoginRequest;
import org.valeneisa.Dtos.autenticación.RegisterRequest;
import org.valeneisa.Seguridad.JwtUtil;
import org.valeneisa.usuario.entidad.Rol;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final IUsuarioRepositorio usuarioRepo;

    public AuthService(JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       IUsuarioRepositorio usuarioRepo) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepo = usuarioRepo;
    }

    public String login(LoginRequest request) {

        Usuario usuario = usuarioRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!usuario.getEstaActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return jwtUtil.generarToken(usuario.getUsername(), usuario.getRolUsuario().name());
    }
    public String register(RegisterRequest request) {

        if (usuarioRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        String passwordEncriptado = passwordEncoder.encode(request.getPassword());

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setCorreoElectronico(request.getEmail());
        usuario.setContrasena(passwordEncriptado);
        usuario.setRolUsuario(Rol.USER);
        usuario.setTokensDisponibles(0);
        usuario.setEstaActivo(true);

        usuarioRepo.save(usuario);

        String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRolUsuario().name());

        return token;
    }
}