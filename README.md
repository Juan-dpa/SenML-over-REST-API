# Proyecto DIS REST — Agricultura IoT

API REST para gestión de parcelas, dispositivos y mediciones de sensores. Los datos de medición usan el formato SenML (RFC 8428). Consta de dos módulos: **servidor** (Spring Boot) y **cliente** (CLI interactivo).

## Requisitos

- Java 17
- Maven 3.x
- PostgreSQL

## Base de datos

```bash
psql -d <tu_base_de_datos> -f bbdd/agricultura.sql
psql -d <tu_base_de_datos> -f bbdd/agriculturaInsert.sql
```

Las credenciales de conexión están hardcodeadas en los DAOs (`jdbc:postgresql://localhost:5432/juadelavi`, usuario `juadelavi`). Cámbialas antes de ejecutar si tu configuración es distinta.

## Dependencia local SenML

El JAR de la API SenML (team-ethernet) se incluye en `libs/` y Maven lo resuelve automáticamente. No se necesita ningún paso de instalación adicional.

## Ejecución

Desde el directorio de cada módulo:

```bash
# Terminal 1 — servidor (puerto 8080)
cd servidor
mvn org.springframework.boot:spring-boot-maven-plugin:run

# Terminal 2 — cliente (menú interactivo)
cd cliente
mvn org.springframework.boot:spring-boot-maven-plugin:run
```

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/parcelas` | Listar parcelas |
| POST | `/parcelas` | Crear parcela |
| PUT | `/parcelas/{id}` | Actualizar parcela |
| DELETE | `/parcelas/{id}` | Eliminar parcela |
| GET | `/dispositivos` | Listar dispositivos |
| POST | `/dispositivos` | Crear dispositivo |
| PUT | `/dispositivos/{id}` | Actualizar dispositivo |
| DELETE | `/dispositivos/{id}` | Eliminar dispositivo |
| POST | `/parcelas/{id}/mediciones/senml` | Enviar mediciones (SenML) |
| GET | `/parcelas/{id}/mediciones/senml` | Consultar mediciones de parcela (SenML) |
| GET | `/dispositivos/{urn}/mediciones/senml` | Consultar mediciones de dispositivo (SenML) |
| DELETE | `/parcelas/{pid}/mediciones/{mid}` | Eliminar medición |
