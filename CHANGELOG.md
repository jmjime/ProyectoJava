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

> Esta entrada corresponde a los subconjuntos **control-versiones** y **persistencia-eficiente** del cambio `revision` (ver `openspec/changes/revision/`). Las capacidades `dominio-hexagonal` y `testing-robusto` permanecen pendientes de aplicar en commits posteriores.

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
