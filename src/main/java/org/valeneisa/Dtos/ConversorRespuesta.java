    package org.valeneisa.Dtos;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor

    public class ConversorRespuesta {
        private double resultado;
        private String monedaDestino;
        private double tasaUtilizada;
        private int tokensConsumidos;
    }
