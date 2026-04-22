package com.comercio.precios.adaptador.entrada.web;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comercio.precios.dominio.puerto.entrada.ConsultarPrecioAplicable;

/**
 * Adaptador REST de entrada: expone la consulta de precio aplicable.
 */
@RestController
@RequestMapping(path = "/api/precios", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Precios", description = "Consulta del precio aplicable por producto, cadena y fecha de aplicación.")
@RequiredArgsConstructor
public class ControladorPrecio {

    private final ConsultarPrecioAplicable consultarPrecioAplicable;

    /**
     * Consulta el precio aplicable para un producto y cadena en una fecha concreta.
     *
     * @param fechaAplicacion fecha y hora de aplicación (ISO-8601 sin zona, p. ej. {@code 2020-06-14T10:00:00})
     * @param idProducto      identificador del producto
     * @param idCadena        identificador de la cadena
     * @return cuerpo JSON con tarifa, intervalo y precio
     */
    @Operation(
            summary = "Consultar precio aplicable",
            description =
                    "Devuelve la tarifa vigente y el precio final (PVP) para el producto y la cadena indicados en la fecha"
                            + " de aplicación. Si no existe tarifa aplicable, la respuesta es 404.")
    @GetMapping
    public RespuestaPrecioDto consultar(
            @Parameter(
                            description =
                                    "Fecha y hora de aplicación en ISO-8601 sin zona (p. ej. 2020-06-14T10:00:00)",
                            required = true,
                            example = "2020-06-14T10:00:00")
                    @RequestParam("fechaAplicacion")
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime fechaAplicacion,
            @Parameter(description = "Identificador del producto", required = true, example = "35455")
                    @RequestParam("idProducto")
                    Long idProducto,
            @Parameter(description = "Identificador de la cadena comercial", required = true, example = "1")
                    @RequestParam("idCadena")
                    Long idCadena) {
        return RespuestaPrecioDto.desdeDominio(
                consultarPrecioAplicable.consultar(idProducto, idCadena, fechaAplicacion));
    }
}
