package org.valeneisa.Seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase utilitaria para la gestión de tokens JWT.
 * Permite generar, validar y extraer información de los tokens.
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar los tokens.
     */
    private final Key KEY;

    /**
     * Tiempo de expiración del token en milisegundos.
     */
    private final long expiracion;

    /**
     * Constructor que inicializa la clave secreta y el tiempo de expiración.
     *
     * @param claveSecreta Clave secreta definida en configuración.
     * @param expiracion Tiempo de expiración del token.
     */
    public JwtUtil(
            @Value("${jwt.secret}") String claveSecreta,
            @Value("${jwt.expiration}") long expiracion) {

        this.KEY = Keys.hmacShaKeyFor(claveSecreta.getBytes());
        this.expiracion = expiracion;
    }

    /**
     * Genera un token JWT con el username y rol del usuario.
     *
     * @param username Nombre de usuario.
     * @param rol Rol del usuario.
     * @return Token JWT generado.
     */
    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el username desde el token.
     *
     * @param token Token JWT.
     * @return Nombre de usuario.
     */
    public String extraerUsername(String token) {
        return obtenerClaims(token).getSubject();
    }

    /**
     * Extrae el rol del usuario desde el token.
     *
     * @param token Token JWT.
     * @return Rol del usuario.
     */
    public String extraerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }

    /**
     * Valida si el token es correcto y no ha expirado.
     *
     * @param token Token JWT.
     * @return true si es válido, false en caso contrario.
     */
    public boolean esValido(String token) {
        try {
            return !obtenerClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene los claims (datos) contenidos en el token.
     *
     * @param token Token JWT.
     * @return Claims extraídos del token.
     */
    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}