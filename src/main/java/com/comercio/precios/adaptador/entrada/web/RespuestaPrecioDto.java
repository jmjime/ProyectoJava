package com.comercio.precios.adaptador.entrada.web;

import com.comercio.precios.dominio.Precio;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida REST con los datos del precio aplicable.
 *
 * @param idProducto  identificador del producto
 * @param idCadena    identificador de la cadena
 * @param tarifa      identificador de la tarifa (lista de precios)
 * @param fechaInicio inicio de vigencia de la tarifa
 * @param fechaFin    fin de vigencia de la tarifa
 * @param precio      precio final (PVP)
 * @param moneda      código ISO de moneda
 */
@Schema(description = "Cuerpo JSON con la tarifa aplicable y el precio resultante.")
public record RespuestaPrecioDto(
        @Schema(description = "Identificador del producto", example = "35455") Long idProducto,
        @Schema(description = "Identificador de la cadena comercial", example = "1") Long idCadena,
        @Schema(description = "Identificador de la lista de precios (tarifa) aplicada", example = "1")
                Long tarifa,
        @Schema(description = "Inicio de vigencia de la tarifa en la respuesta") LocalDateTime fechaInicio,
        @Schema(description = "Fin de vigencia de la tarifa en la respuesta") LocalDateTime fechaFin,
        @Schema(description = "Precio final (PVP) en la moneda indicada", example = "35.50") BigDecimal precio,
        @Schema(description = "Código ISO 4217 de la moneda", example = "EUR") String moneda) {

    /**
     * Construye el DTO a partir del modelo de dominio.
     *
     * @param precio entidad de dominio
     * @return DTO listo para serializar a JSON
     */
    public static RespuestaPrecioDto desdeDominio(Precio precio) {
        return new RespuestaPrecioDto(
                precio.idProducto(),
                precio.idCadena(),
                precio.tarifa(),
                precio.fechaInicio(),
                precio.fechaFin(),
                precio.precioFinal(),
                precio.moneda());
    }
}
