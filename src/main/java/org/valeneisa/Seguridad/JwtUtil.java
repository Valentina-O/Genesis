package org.valeneisa.Seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String CLAVE_SECRETA = "genesis_secreto_genesis_secreto_123456"; // mínimo 32 chars
    private final long EXPIRACION = 86400000; // 1 día

    // ✅ CLAVE SEGURA (IMPORTANTE)
    private final Key KEY = Keys.hmacShaKeyFor(CLAVE_SECRETA.getBytes());

    public String generarToken(String username, String rol) {

        System.out.println("🔐 Generando token para: " + username);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACION))
                .signWith(KEY, SignatureAlgorithm.HS256) // ✅ CORRECTO
                .compact();

        System.out.println("✅ Token generado: " + token);

        return token;
    }

    public String extraerUsername(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }

    public boolean esValido(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Token inválido: " + e.getMessage());
            return false;
        }
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder() // ✅ CORRECTO
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}