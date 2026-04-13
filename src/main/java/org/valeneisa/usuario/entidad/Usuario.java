    package org.valeneisa.usuario.entidad;

    import jakarta.persistence.*;
    import lombok.*;
    import org.valeneisa.tokens.Suscripcion;

    @Entity
    @Table(name = "usuarios")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idUsuario;

        @Column(name = "usuario", nullable = false, unique = true)
        private String usuario;

        @Column(name = "correoElectronico", nullable = false, unique = true)
        private String correoElectronico;

        @Column(name = "contrasena", nullable = false)
        private String contrasena;

        @Enumerated(EnumType.STRING)
        @Column(name = "rol")
        private Rol rolUsuario;

        @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
        private Suscripcion suscripcion;

        @Column(name = "tokens_disponibles")
        private Integer tokensDisponibles;

        @Column(name = "esta_activo")
        private Boolean estaActivo;
    }