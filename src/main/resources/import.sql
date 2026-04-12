-- 1. Insertar Planes Base (Requerimiento obligatorio) [cite: 28, 29]
INSERT INTO planes (nombre, tokens_otorgados, esta_activo) VALUES ('Free', 200, true);
INSERT INTO planes (nombre, tokens_otorgados, esta_activo) VALUES ('Pro', 1000, true);
INSERT INTO planes (nombre, tokens_otorgados, esta_activo) VALUES ('Enterprise', 5000, true);

-- 2. Insertar Tasa de Cambio Inicial (Para OP-02) [cite: 21, 73]
INSERT INTO tasa_cambio (valor, fecha_actualizacion) VALUES (4000.0, NOW());

-- 3. Insertar Catálogo de Operaciones (Para que Valentina y Juan puedan trabajar) [cite: 11, 41]
INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-01', '¿Cuánto me cuesta ese crédito?', 50, true);

INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-02', 'Conversor COP USD', 20, true);

INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-03', 'Calculadora de IMC', 15, true);

INSERT INTO operaciones (codigo, nombre, costo_base, esta_activo)
VALUES ('OP-04', 'Calculadora de sueño', 20, true);