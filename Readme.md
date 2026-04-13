# 🚀 Proyecto Genesis — Breaze in the Moon

## 📌 Descripción

Genesis es una plataforma que permite a los usuarios consumir operaciones de cómputo mediante un sistema de tokens. Cada operación ejecutada tiene un costo calculado dinámicamente, permitiendo trazabilidad y control del consumo.

---

## 🧱 Tecnologías utilizadas

* Java 17
* Spring Boot
* Spring Security + JWT
* JPA / Hibernate
* H2 Database (entorno de desarrollo)
* OpenAPI (Swagger)
* Bruno (colección de pruebas)

---

## ⚙️ Cómo ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd Genesis
```

### 2. Ejecutar el proyecto

Desde tu IDE o con Maven:

```bash
mvn spring-boot:run
```

---

## 🌐 URL base

```
http://localhost:8080/api/v1
```

---

## 📚 Documentación API

Swagger UI disponible en:

```
http://localhost:8080/api/v1/swagger-ui.html
```

Archivo OpenAPI:

```
/src/main/resources/static/openapi.yml
```

---

## 🔐 Autenticación

El sistema utiliza JWT.

### Usuario administrador inicial

```
usuario: admin
contraseña: 123456
```

> ⚠️ La contraseña está encriptada con BCrypt en la base de datos.

---

## 🗄️ Base de datos

Se usa H2 en memoria.

Configuración:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create
```

Datos iniciales cargados automáticamente desde:

```
src/main/resources/import.sql
```

---

## 📦 Funcionalidades principales

### 👤 Usuario

* Registro
* Login (JWT)
* Consultar perfil
* Ver historial de transacciones
* Consultar catálogo de operaciones
* Suscribirse a planes

### 🛠️ Administrador

* Gestión de usuarios (activar/desactivar)
* Recarga de tokens
* CRUD de planes
* Activar/desactivar operaciones
* Actualizar tasa COP/USD
* Consultar métricas globales

---

## ⚡ Catálogo de operaciones

| Código | Operación         | Costo base |
| ------ | ----------------- | ---------- |
| OP-01  | Crédito           | 50 tokens  |
| OP-02  | Conversor COP/USD | 20 tokens  |
| OP-03  | IMC               | 15 tokens  |
| OP-04  | Sueño             | 20 tokens  |

---

## 💰 Modelo de tokens

```
tokens_entrada = floor(longitud JSON entrada / 4)
tokens_salida  = floor(longitud JSON salida / 4)
costo_total    = costo_base + tokens_entrada + tokens_salida
```

---

## 🧪 Pruebas

Se incluye colección de Bruno en:

```
Genesis-Dev/
```

Incluye:

* Login
* Registro
* Perfil
* Operaciones

---

## 🌱 Planes disponibles

| Plan       | Tokens |
| ---------- | ------ |
| Free       | 200    |
| Pro        | 1000   |
| Enterprise | 5000   |

---

## 📂 Estructura del proyecto

* `Controladores` → Endpoints REST
* `Servicios` → Lógica de negocio
* `Repositorios` → Acceso a datos
* `Dtos` → Requests/Responses
* `Seguridad` → JWT y configuración
* `Operaciones` → Lógica de cálculo

---

## 🧠 Principios aplicados

* Separación de capas
* Inyección de dependencias
* Uso de interfaces (desacoplamiento)
* Responsabilidad única (SRP)

---

## 📌 Notas

* No se permiten operaciones si el usuario no tiene tokens suficientes.
* En caso de error, no se descuentan tokens.
* Un usuario solo puede tener una suscripción activa.

---

## 📄 Entregables incluidos

* Código fuente
* `openapi.yml`
* `README.md`
* Script SQL (`import.sql`)
* Colección Bruno

---
 