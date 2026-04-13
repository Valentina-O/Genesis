package org.valeneisa.Seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key KEY;
    private final long expiracion;

    public JwtUtil(
            @Value("${jwt.secret}") String claveSecreta,
            @Value("${jwt.expiration}") long expiracion) {

        this.KEY = Keys.hmacShaKeyFor(claveSecreta.getBytes());
        this.expiracion = expiracion;
    }

    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerUsername(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }

    public boolean esValido(String token) {
        try {
            return !obtenerClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}