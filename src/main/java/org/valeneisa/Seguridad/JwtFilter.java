package org.valeneisa.Seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * 🔐 Filtro de autenticación JWT
 *
 * Este filtro intercepta TODAS las peticiones HTTP y:
 *
 * 1. Extrae el token JWT del header Authorization
 * 2. Valida el token
 * 3. Obtiene el usuario desde la base de datos
 * 4. Verifica que el usuario esté activo
 * 5. Construye la autenticación de Spring Security
 *
 * ⚠️ IMPORTANTE:
 * - El rol viene como "ADMIN" o "USER"
 * - Se convierte a "ROLE_ADMIN" o "ROLE_USER"
 *   porque Spring Security lo exige
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Utilidad para manejar tokens JWT
     */
    private final JwtUtil jwtUtil;

    /**
     * Repositorio de usuarios
     */
    private final IUsuarioRepositorio usuarioRepositorio;

    /**
     * Método principal que se ejecuta en cada request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            // 🔎 1. Obtener header Authorization
            String header = request.getHeader("Authorization");

            // Validar que exista y tenga formato Bearer
            if (header != null && header.startsWith("Bearer ")) {

                String token = header.substring(7);

                // 🔐 2. Validar token y que no haya autenticación previa
                if (jwtUtil.esValido(token) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 👤 3. Extraer datos del token
                    String username = jwtUtil.extraerUsername(token);
                    String rol = jwtUtil.extraerRol(token);

                    // 🔎 4. Buscar usuario en base de datos
                    Optional<Usuario> usuarioOpt = usuarioRepositorio.findByUsuario(username);

                    if (usuarioOpt.isEmpty()) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    Usuario usuario = usuarioOpt.get();

                    // 🚫 5. Validar si el usuario está activo
                    if (!Boolean.TRUE.equals(usuario.getEstaActivo())) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Usuario desactivado");
                        return;
                    }

                    // 🔑 6. Crear autoridad con prefijo ROLE_
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + rol);

                    // 🧠 7. Crear objeto de autenticación
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singletonList(authority)
                            );

                    // ✅ 8. Guardar autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (Exception e) {

            // ❌ Si algo falla → token inválido
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");
            return;
        }

        // 🔄 Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}