package com.comercio.precios.aplicacion;

/**
 * Indica que no existe ninguna tarifa aplicable para la combinación producto-cadena-fecha solicitada.
 */
public class PrecioNoEncontradoException extends RuntimeException {

    /**
     * Construye la excepción con un mensaje descriptivo en castellano.
     *
     * @param mensaje texto explicativo para registros o respuesta traducida
     */
    public PrecioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
