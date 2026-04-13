package org.valeneisa.Seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Clase de configuración de seguridad de la aplicación.
 * Define las reglas de acceso, configuración de CORS, manejo de sesiones
 * y el filtro JWT para la autenticación.
 */
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    /**
     * Filtro encargado de validar los tokens JWT en cada petición.
     */
    private final JwtFilter filtroJwt;

    /**
     * Constructor que inyecta el filtro JWT.
     *
     * @param filtroJwt Filtro de autenticación basado en JWT.
     */
    public ConfiguracionSeguridad(JwtFilter filtroJwt) {
        this.filtroJwt = filtroJwt;
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * Define políticas de CORS, rutas públicas, rutas protegidas por rol
     * y desactiva el manejo de sesiones (stateless).
     *
     * @param http Configuración de seguridad HTTP.
     * @return Cadena de filtros configurada.
     * @throws Exception En caso de error en la configuración.
     */
    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))

                .authorizeHttpRequests(auth -> auth

                        // PUBLICOS
                        .requestMatchers(
                                "/auth/**",
                                "/openapi.yml",
                                "/api/v1/openapi.yml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        //  ROLES
                        .requestMatchers("/api/v1/usuario/**").hasRole("USER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define el codificador de contraseñas utilizando BCrypt.
     *
     * @return Instancia de PasswordEncoder.
     */
    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }
}