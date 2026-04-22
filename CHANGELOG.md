# Changelog

Todos los cambios relevantes de este proyecto se documentan en este fichero.

El formato se basa en [Keep a Changelog 1.1.0](https://keepachangelog.com/es-ES/1.1.0/)
y el proyecto adhiere a [Semantic Versioning 2.0.0](https://semver.org/lang/es/).

## [Unreleased]

### Added

- Plantilla de mensaje de commit `.gitmessage` con los tipos permitidos de Conventional Commits 1.0.0 y ejemplos en castellano.
- `CHANGELOG.md` siguiendo *Keep a Changelog 1.1.0* como registro único de cambios del proyecto.
- Sección *Convenciones de commits y ramas* en `README.md` con tipos permitidos, política de ramas (`feat/`, `fix/`, `chore/`, …) y flujo de Pull Requests.
- **ADR-5** (propuesta) y **ADR-6** (adoptada) en `plan/analisis_tecnico.md`, enlazando el cambio `openspec/changes/revision/`.

### Changed

- `.gitignore` consolidado con secciones comentadas (build, IDEs, OS, logs, entorno local) incluyendo `build/`, `out/`, `Thumbs.db` y la excepción `!.env.example`.
- `RepositorioPrecioJpa` resuelve la tarifa aplicable con un método derivado `findFirst…OrderBy…` envuelto en un `buscarPrecioAplicable(idProducto, idCadena, fecha)` de firma corta; se elimina la `@Query` JPQL y el uso de `Page<…>` + `PageRequest.of(0,1)`, con lo que Hibernate ya no emite `count` complementario para esta consulta.
- `AdaptadorRepositorioPrecios` invoca al repositorio con el orden canónico `(idProducto, idCadena, fecha)` definido por el puerto de salida (antes invertía producto y cadena al pasarlos a la `@Query`).
- `EntidadPrecioJpa` queda sellada frente a mutación externa: `@Setter` público eliminado; `@NoArgsConstructor` pasa a `protected` (para Hibernate) y se añade `@AllArgsConstructor(access = PACKAGE)` para construcción desde pruebas del propio paquete. El índice `idx_precios_cadena_producto_prioridad` sobre `(id_cadena, id_producto, prioridad)` se preserva sin cambios.
- `com.comercio.precios.dominio.Precio` pasa de `class` a `record` Java 17 con canonical constructor que valida invariantes de negocio (no-nulos, `fechaInicio <= fechaFin`, `precioFinal >= 0`, `moneda` de 3 letras mayúsculas). `RespuestaPrecioDto` se adapta a los accessors de `record`.
- Paquete `com.comercio.precios.service` renombrado a `com.comercio.precios.aplicacion`; clase `ObtenerPrecioAplicableCasoUso` renombrada a `CasoUsoConsultarPrecioAplicable` e implementa el puerto de entrada `ConsultarPrecioAplicable`. Método `ejecutar` renombrado a `consultar`. `PrecioNoEncontradoException` acompaña al nuevo paquete.
- `ControladorPrecio` pasa a depender del puerto de entrada `ConsultarPrecioAplicable` en vez de la clase concreta; `ControladorPrecioWebMvcTest`, `ManejadorExcepcionesGlobal` y el pointcut de `AspectoRegistroTransversal` se actualizan en consecuencia (el pointcut ahora cubre `com.comercio.precios.aplicacion..*`).

### Added

- Puerto entrante `com.comercio.precios.dominio.puerto.entrada.ConsultarPrecioAplicable` y reubicación del puerto saliente `PuertoRepositorioPrecios` a `com.comercio.precios.dominio.puerto.salida`.
- `com.comercio.precios.configuracion.BeansAplicacion` registra el caso de uso como `@Bean`, materializando ADR-5 (arranque Spring invertido hacia configuración). El dominio y la aplicación quedan libres de imports de `org.springframework.*`.
- Test de arquitectura `ArquitecturaHexagonalTest` con ArchUnit 1.4.1 (`scope=test`) que verifica: (a) `dominio` y `aplicacion` no dependen de `org.springframework..`; (b) `dominio` no depende de `jakarta.persistence..`.
- `RepositorioPrecioJpaTest` (`@DataJpaTest` + `@Sql`) que fija el contrato del método derivado: camino feliz, desempate por `fechaInicio` más reciente, desempate por `id` mayor y `Optional.empty()` sin filas. Habilita `hibernate.generate_statistics` y asegura con `Statistics#getQueryExecutionCount` que la consulta se resuelve con una única `SELECT … LIMIT 1` (sin `count` paginador).
- `AdaptadorRepositorioPreciosTest` con Mockito (mapeo entidad → dominio campo a campo y orden canónico de argumentos al repositorio mediante `ArgumentCaptor`).
- Casos adicionales en `PrecioTest` para las invariantes de rango: fechas incoherentes, `precioFinal` negativo y formato de `moneda` (longitud y mayúsculas) lanzan `IllegalArgumentException`.
- Perfil de pruebas dedicado: `src/test/resources/application-test.yml` con H2 aislada, `ddl-auto=create-drop` y springdoc habilitado; `@ActiveProfiles("test")` aplicado a `ControladorPrecioTest`, `OpenApiDocsTest` y `ControladorPrecioWebMvcTest`.
- Dependencia `org.springframework.boot:spring-boot-starter-data-jpa-test` (scope `test`) requerida por Spring Boot 4 para `@DataJpaTest`.

### Changed

- Umbrales JaCoCo elevados a `LINE ≥ 0.85` y `BRANCH ≥ 0.75` a nivel `BUNDLE`; se añaden exclusiones explícitas (`PreciosApplication`, `BeansAplicacion`, `*Dto`, `EntidadPrecioJpa`) para que la medida se centre en la lógica de negocio y adaptación.

> Esta entrada acumula los subconjuntos **control-versiones**, **persistencia-eficiente**, **dominio-hexagonal** y **testing-robusto** del cambio `revision` (ver `openspec/changes/revision/`). Queda pendiente el grupo 7 (*validación final y PR*) antes del archivo.

## [0.1.0] - 2026-04-16

### Added

- Servicio Spring Boot 4.0.5 con Java 17 y Maven 3.9+ que expone `GET /api/precios` devolviendo tarifa, vigencia y precio aplicables para una terna (producto, cadena, fecha de aplicación).
- Arquitectura hexagonal con dominio, caso de uso, adaptador REST (`ControladorPrecio`), adaptador JPA (`AdaptadorRepositorioPrecios`) y manejador global de excepciones (`ManejadorExcepcionesGlobal`).
- Regla de desempate de tarifas aplicables: `prioridad DESC`, `fechaInicio DESC`, `id DESC`, documentada e implementada (tarea `T-4`).
- Documentación OpenAPI 3 vía springdoc en el perfil `default` y desactivada en el perfil `prod`.
- Persistencia H2 en memoria con `data.sql` de ejemplo.
- Suite de pruebas: integración MVC (`ControladorPrecioTest`, `OpenApiDocsTest`), slice MVC (`ControladorPrecioWebMvcTest`) y pruebas unitarias de dominio, caso de uso y DTO.
- Cobertura JaCoCo con umbral mínimo de líneas `0.70` en `mvn verify`.
- Soporte Docker vía `docker-compose.yml` y variable `SPRING_PROFILES_ACTIVE`.
- Plan de producto bajo `plan/` con análisis funcional, técnico y tareas validados (`[v]` en T-1…T-6) según `AGENTS.md`.

[Unreleased]: https://example.invalid/servicio-precios/compare/v0.1.0...HEAD
[0.1.0]: https://example.invalid/servicio-precios/releases/tag/v0.1.0
