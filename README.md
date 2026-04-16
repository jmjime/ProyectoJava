# Servicio de consulta de precios

Microservicio **Spring Boot 4.0.5** con **Java 17** y **Maven 3.9+** que expone un **GET REST** para obtener la tarifa y el precio aplicable a un producto de una cadena en una fecha y hora concretas. Usa **H2 en memoria** con los datos de ejemplo del enunciado, **arquitectura hexagonal**, **Lombok** y **AOP** (`spring-boot-starter-aspectj`) para registro transversal.

## Requisitos

- **JDK 17** (`JAVA_HOME` debe apuntar al JDK 17 para alinear compilación y ejecución con el IDE y Maven)
- **Maven 3.9 o superior** (el `pom.xml` incluye Enforcer para comprobarlo)
- **Docker** (opcional), para construir y ejecutar con Compose

## Arquitectura

Resumen: dominio puro con puerto de salida, caso de uso en aplicación, adaptadores REST y JPA. Detalle ampliado, diagramas y flujo de petición en **[docs/arquitectura-hexagonal.md](docs/arquitectura-hexagonal.md)**.

## Ejecución local

```bash
mvn spring-boot:run
```

Consola H2 (opcional): [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL `jdbc:h2:mem:preciosdb`, usuario `sa`, contraseña vacía).

### Perfiles Spring (`SPRING_PROFILES_ACTIVE`)

Spring Boot activa perfiles con la variable de entorno **`SPRING_PROFILES_ACTIVE`** (varios perfiles separados por comas). Sin definirla, se usa el **perfil por defecto** del `application.yml`.

Ejemplos en local:

- **PowerShell (Windows):** `$env:SPRING_PROFILES_ACTIVE = "prod"; mvn spring-boot:run`
- **Bash:** `export SPRING_PROFILES_ACTIVE=prod && mvn spring-boot:run`

Alternativa solo Maven (sin variable de entorno): `mvn spring-boot:run -Dspring-boot.run.profiles=prod`

## Documentación OpenAPI y Swagger UI

Con la app en marcha (`mvn spring-boot:run` o Docker):

- **Swagger UI** (probar el GET desde el navegador): [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (redirige a la interfaz interactiva de springdoc).
- **Documento OpenAPI 3 (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

La librería es **springdoc-openapi** (compatible con Spring Boot 4); no se usa Springfox.

En **producción** el perfil `prod` desactiva estos endpoints (`springdoc.api-docs.enabled=false`, `springdoc.swagger-ui.enabled=false`). Si en algún despliegue necesitas documentación en vivo, protégela (por ejemplo con Spring Security o red) en lugar de exponerla públicamente.

## API

**GET** `/api/precios`

| Parámetro         | Descripción                          | Ejemplo              |
|-------------------|--------------------------------------|----------------------|
| `fechaAplicacion` | `LocalDateTime` ISO-8601 (sin zona) | `2020-06-14T10:00:00` |
| `idProducto`      | Long                                 | `35455`              |
| `idCadena`        | Long                                 | `1`                  |

**Respuesta 200** (JSON): `idProducto`, `idCadena`, `tarifa`, `fechaInicio`, `fechaFin`, `precio`, `moneda`.

**404** si no hay tarifa aplicable (`mensaje` en JSON).

### Ejemplo con curl

```bash
curl -s "http://localhost:8080/api/precios?fechaAplicacion=2020-06-14T16:00:00&idProducto=35455&idCadena=1"
```

## Pruebas

```bash
mvn test
```

**Cobertura (JaCoCo):** informe HTML en `target/site/jacoco/index.html`. Umbral mínimo de líneas del bundle (propiedad `jacoco.line.minimum` en el `pom`) en la fase `verify`:

```bash
mvn verify
```

Hay **pruebas de integración** (`ControladorPrecioTest`, `OpenApiDocsTest`) sobre el contexto completo, **pruebas unitarias** del caso de uso (`ObtenerPrecioAplicableCasoUsoTest`) y del mapeo DTO (`RespuestaPrecioDtoTest`). Surefire incluye `*Test.java`, `*Tests.java` y `*IT.java`.

## Docker

```bash
docker compose up --build
```

La aplicación escucha en el puerto **8080**. La base de datos sigue siendo **H2 en memoria** dentro del proceso (sin contenedor de BD adicional).

**Perfil en contenedor:** el `docker-compose.yml` inyecta **`SPRING_PROFILES_ACTIVE`** desde el entorno de la máquina host o desde un fichero **`.env`** en la raíz del proyecto (Docker Compose lo usa para sustituir variables en el YAML; el fichero `.env` no debe subirse al repositorio). Plantilla: [`.env.example`](.env.example).

- Sin `.env` o sin esa variable: perfil por defecto (OpenAPI/Swagger activos).
- Con `SPRING_PROFILES_ACTIVE=prod` en `.env` o exportada: perfil `prod` (OpenAPI desactivado, como en `application.yml`).

Ejemplo sin fichero `.env`: `SPRING_PROFILES_ACTIVE=prod docker compose up --build` (sintaxis típica en Unix; en PowerShell: `$env:SPRING_PROFILES_ACTIVE="prod"; docker compose up --build`).

Con imagen suelta: `docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 servicio-precios:local`

## Notas Spring Boot 4

- AOP: dependencia **`spring-boot-starter-aspectj`** (sustituye al antiguo `spring-boot-starter-aop`).
- Tests MVC: dependencia de test **`spring-boot-starter-webmvc-test`** e import `org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc`.

## IDE y Lombok

Instala el **plugin Lombok** en IntelliJ IDEA o VS Code para que el IDE reconozca las anotaciones generadas.
