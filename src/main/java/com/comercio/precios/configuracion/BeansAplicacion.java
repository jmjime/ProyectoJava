package com.comercio.precios.configuracion;

import com.comercio.precios.aplicacion.CasoUsoConsultarPrecioAplicable;
import com.comercio.precios.dominio.puerto.entrada.ConsultarPrecioAplicable;
import com.comercio.precios.dominio.puerto.salida.PuertoRepositorioPrecios;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registra los beans de la capa de aplicación en el contexto Spring sin tener que anotar las
 * propias clases de {@code com.comercio.precios.aplicacion} con estereotipos de Spring.
 * Materializa ADR-5 (arranque Spring invertido hacia configuración).
 */
@Configuration
public class BeansAplicacion {

    /**
     * Declara el caso de uso como bean del puerto de entrada.
     *
     * @param puerto puerto de salida de persistencia (inyectado por Spring)
     * @return implementación del puerto de entrada
     */
    @Bean
    public ConsultarPrecioAplicable consultarPrecioAplicable(PuertoRepositorioPrecios puerto) {
        return new CasoUsoConsultarPrecioAplicable(puerto);
    }
}
