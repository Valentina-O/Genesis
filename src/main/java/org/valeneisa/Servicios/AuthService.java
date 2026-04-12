package org.valeneisa.Servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.valeneisa.Dtos.autenticación.LoginRequest;
import org.valeneisa.Dtos.autenticación.RegisterRequest;
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

    public String login(LoginRequest request) {

        System.out.println("\n🔥 === LOGIN ===");
        System.out.println("👤 Username: " + request.getUsername());

        Usuario usuario = usuarioRepo.buscarPorUsername(request.getUsername())
                .orElseThrow(() -> {
                    System.out.println("❌ Usuario no encontrado");
                    return new RuntimeException("Usuario no encontrado");
                });

        System.out.println("✅ Usuario encontrado");

        if (!passwordEncoder.matches(request.getPassword(), usuario.getContrasena())) {
            System.out.println("❌ Contraseña incorrecta");
            throw new RuntimeException("Contraseña incorrecta");
        }

        System.out.println("🔑 Contraseña correcta");

        if (!usuario.getEstaActivo()) {
            System.out.println("❌ Usuario inactivo");
            throw new RuntimeException("Usuario inactivo");
        }

        System.out.println("🟢 Usuario activo");

        String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRolUsuario().name());

        System.out.println("🎟️ Token generado: " + token);

        return token;
    }

    public String register(RegisterRequest request) {

        System.out.println("\n🔥 === REGISTER ===");

        if (usuarioRepo.buscarPorUsername(request.getUsername()).isPresent()) {
            System.out.println("❌ Usuario ya existe: " + request.getUsername());
            throw new RuntimeException("El usuario ya existe");
        }

        System.out.println("📩 Username: " + request.getUsername());
        System.out.println("📧 Email: " + request.getEmail());

        String passwordEncriptado = passwordEncoder.encode(request.getPassword());
        System.out.println("🔒 Password encriptado: " + passwordEncriptado);

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setCorreoElectronico(request.getEmail());
        usuario.setContrasena(passwordEncriptado);
        usuario.setRolUsuario(Rol.USER);
        usuario.setTokensDisponibles(0);
        usuario.setEstaActivo(true);

        usuarioRepo.guardar(usuario);

        System.out.println("✅ Usuario guardado en memoria");

        String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRolUsuario().name());

        System.out.println("🎟️ Token generado: " + token);

        return token;
    }
}