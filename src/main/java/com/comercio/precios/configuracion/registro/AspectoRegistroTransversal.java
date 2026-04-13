package com.comercio.precios.configuracion.registro;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspecto que registra la entrada, salida, duración y errores de los beans de aplicación y adaptadores.
 */
@Aspect
@Component
@Slf4j
public class AspectoRegistroTransversal {

    /**
     * Punto de corte: métodos públicos en paquetes de aplicación o adaptadores.
     */
    @Pointcut(
            "execution(public * com.comercio.precios.service..*(..)) "
                    + "|| execution(public * com.comercio.precios.adaptador.entrada..*(..)) "
                    + "|| execution(public * com.comercio.precios.adaptador.salida.persistencia.AdaptadorRepositorioPrecios.*(..))")
    public void capasInstrumentadas() {}

    /**
     * Excluye métodos anotados con {@link SinRegistroAuditoria}.
     */
    @Pointcut("@annotation(com.comercio.precios.configuracion.registro.SinRegistroAuditoria)")
    public void sinRegistro() {}

    /**
     * Envuelve la ejecución para registrar trazas en castellano.
     *
     * @param joinPoint unión actual
     * @return resultado del método objetivo
     * @throws Throwable propaga la excepción del objetivo
     */
    @Around("capasInstrumentadas() && !sinRegistro()")
    public Object registrarAlrededor(ProceedingJoinPoint joinPoint) throws Throwable {
        String firma = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Entrada: {} con {} argumento(s)", firma, args.length);
        long inicio = System.nanoTime();
        try {
            Object resultado = joinPoint.proceed();
            long ms = (System.nanoTime() - inicio) / 1_000_000L;
            log.info("Salida correcta: {} en {} ms", firma, ms);
            return resultado;
        } catch (Throwable error) {
            long ms = (System.nanoTime() - inicio) / 1_000_000L;
            log.error("Error en {} tras {} ms: {} - {}", firma, ms, error.getClass().getSimpleName(), error.getMessage());
            throw error;
        }
    }
}
