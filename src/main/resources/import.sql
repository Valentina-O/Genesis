INSERT INTO planes (nombre, tokens_otorgados, esta_activo) VALUES ('Free', 200, true);
INSERT INTO planes (nombre, tokens_otorgados, esta_activo) VALUES ('Pro', 1000, true);
INSERT INTO planes (nombre, tokens_otorgados, esta_activo) VALUES ('Enterprise', 5000, true);

INSERT INTO tasa_cambio (valor, fecha_actualizacion) VALUES (4000.0, CURRENT_TIMESTAMP);

INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-01', '¿Cuánto me cuesta ese crédito?', 50, true);

INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-02', 'Conversor COP USD', 20, true);

INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-03', 'Calculadora de IMC', 15, true);

INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-04', 'Calculadora de sueño', 20, true);

INSERT INTO usuarios (
    usuario,
    correoElectronico,
    contrasena,
    rol,
    estaActivo,
    tokensDisponibles
) VALUES (
             'admin',
             'admin@genesis.com',
             '$2a$12$cZCvlpkmSYQ4qNzQyOf7bOHqERFINyHNz7E8iMBD/vOKbirhgt6da',
             'ADMIN',
             true,
             10000
         );