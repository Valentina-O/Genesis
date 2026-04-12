package org.valeneisa.Dtos.autenticación;

public class AuthResponse {

    private String token;
    private String tipo = "Bearer";

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }
}
