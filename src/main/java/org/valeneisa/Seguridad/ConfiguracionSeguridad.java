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

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private final JwtFilter filtroJwt;

    public ConfiguracionSeguridad(JwtFilter filtroJwt) {
        this.filtroJwt = filtroJwt;
    }

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

                        // 🔓 PUBLICOS
                        .requestMatchers(
                                "/auth/**", // 🔥 ESTA ES LA CLAVE
                                "/openapi.yml",
                                "/api/v1/openapi.yml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 🔐 ROLES
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

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }
}