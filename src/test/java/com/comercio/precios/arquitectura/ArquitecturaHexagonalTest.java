package com.comercio.precios.arquitectura;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de arquitectura hexagonal basadas en análisis de bytecode (ArchUnit).
 *
 * <p>Vigilan dos fronteras explícitas de ADR-1 y ADR-5:
 * <ul>
 *   <li>La capa de dominio y la de aplicación NO pueden depender de {@code org.springframework..}
 *       (materializa ADR-5: el arranque Spring se invierte hacia {@code configuracion}).</li>
 *   <li>El dominio NO puede depender de {@code jakarta.persistence..} (la entidad JPA vive en
 *       el adaptador de salida, no en el dominio).</li>
 * </ul>
 *
 * <p>No se usa {@code @AnalyzeClasses} para mantener el test como una unidad JUnit 5 normal y que
 * aparezca en el mismo reporte de Surefire que el resto de pruebas.
 */
class ArquitecturaHexagonalTest {

    private static final String PAQUETE_RAIZ = "com.comercio.precios";

    private static final JavaClasses CLASES_PRODUCCION =
            new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages(PAQUETE_RAIZ);

    @Test
    @DisplayName("Dominio y aplicación no dependen de org.springframework..")
    void dominio_y_aplicacion_sin_spring() {
        ArchRule regla =
                noClasses()
                        .that()
                        .resideInAnyPackage(
                                PAQUETE_RAIZ + ".dominio..", PAQUETE_RAIZ + ".aplicacion..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAPackage("org.springframework..")
                        .because(
                                "ADR-1 (hexagonal) y ADR-5: el arranque Spring se declara en"
                                        + " com.comercio.precios.configuracion, nunca dentro del"
                                        + " dominio o de la aplicación");

        regla.check(CLASES_PRODUCCION);
    }

    @Test
    @DisplayName("Dominio no depende de jakarta.persistence..")
    void dominio_sin_jpa() {
        ArchRule regla =
                noClasses()
                        .that()
                        .resideInAPackage(PAQUETE_RAIZ + ".dominio..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAPackage("jakarta.persistence..")
                        .because(
                                "El modelo de persistencia (EntidadPrecioJpa) vive en el adaptador"
                                        + " de salida; el dominio no conoce JPA");

        regla.check(CLASES_PRODUCCION);
    }
}
