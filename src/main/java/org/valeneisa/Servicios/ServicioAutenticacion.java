package org.valeneisa.Servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.valeneisa.Dtos.Autenticacion.SolicitudLogin;
import org.valeneisa.Dtos.Autenticacion.SolicitudRegistro;
import org.valeneisa.Seguridad.JwtUtil;
import org.valeneisa.usuario.entidad.Rol;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

/**
 * Servicio encargado de la autenticación de usuarios.
 * Permite el registro de nuevos usuarios y el inicio de sesión,
 * generando tokens JWT para la autorización.
 */
@Service
@RequiredArgsConstructor
public class ServicioAutenticacion {

    /**
     * Codificador de contraseñas.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Utilidad para generación de tokens JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Repositorio de usuarios.
     */
    private final IUsuarioRepositorio usuarioRepo;

    /**
     * Realiza el proceso de inicio de sesión.
     *
     * @param request Datos de login (usuario y contraseña).
     * @return Token JWT generado.
     */
    public String login(SolicitudLogin request) {

        Usuario usuario = usuarioRepo.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!usuario.getEstaActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return jwtUtil.generarToken(
                usuario.getUsuario(),
                usuario.getRolUsuario().name()
        );
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request Datos de registro.
     * @return Token JWT generado para el nuevo usuario.
     */
    public String register(SolicitudRegistro request) {

        if (usuarioRepo.findByUsuario(request.getUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (usuarioRepo.findByCorreoElectronico(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        String passwordEncriptado = passwordEncoder.encode(request.getContrasena());

        Usuario usuario = new Usuario();
        usuario.setUsuario(request.getUsuario());
        usuario.setCorreoElectronico(request.getCorreo());
        usuario.setContrasena(passwordEncriptado);
        usuario.setRolUsuario(Rol.USER);
        usuario.setTokensDisponibles(0);
        usuario.setEstaActivo(true);

        usuarioRepo.save(usuario);

        return jwtUtil.generarToken(
                usuario.getUsuario(),
                usuario.getRolUsuario().name()
        );
    }
}