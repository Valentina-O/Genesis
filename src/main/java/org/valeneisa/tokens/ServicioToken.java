package org.valeneisa.tokens;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ServicioToken {

    private final ObjectMapper mapeador = new ObjectMapper();

    public int calcularCostoTotal(int costoBase, Object entrada, Object salida) {
        try {

            String jsonEntrada = mapeador.writeValueAsString(entrada);
            String jsonSalida = mapeador.writeValueAsString(salida);

            int tokensEntrada = (int) Math.floor(jsonEntrada.length() / 4.0);
            int tokensSalida = (int) Math.floor(jsonSalida.length() / 4.0);

            return costoBase + tokensEntrada + tokensSalida;

        } catch (Exception e) {
            return costoBase;
        }
    }
}