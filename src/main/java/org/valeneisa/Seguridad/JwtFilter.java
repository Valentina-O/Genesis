package org.valeneisa.Seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final IUsuarioRepositorio usuarioRepositorio;

    public JwtFilter(JwtUtil jwtUtil, IUsuarioRepositorio usuarioRepositorio) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {

                String token = header.substring(7);

                if (jwtUtil.esValido(token) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    String username = jwtUtil.extraerUsername(token);
                    String rol = jwtUtil.extraerRol(token);

                    Optional<Usuario> usuarioOpt = usuarioRepositorio.findByUsuario(username);

                    if (usuarioOpt.isEmpty()) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    Usuario usuario = usuarioOpt.get();

                    // 🔴 VALIDAR SI EL USUARIO ESTÁ ACTIVO
                    if (!Boolean.TRUE.equals(usuario.getEstaActivo())) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Usuario desactivado");
                        return;
                    }

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + rol);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singletonList(authority)
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");

            return;
        }

        filterChain.doFilter(request, response);
    }
}