-- Datos de semilla dedicados al test @DataJpaTest del repositorio.
-- Usa productos/cadenas distintos a los del enunciado para que cada caso sea independiente.

-- Caso A (camino feliz + sin solape múltiple): producto 10000, cadena 1.
-- Una única tarifa vigente en 2020-09-01 12:00.
INSERT INTO precios (id_cadena, fecha_inicio, fecha_fin, lista_precios, id_producto, prioridad, precio, moneda)
VALUES (1, TIMESTAMP '2020-09-01 00:00:00', TIMESTAMP '2020-09-30 23:59:59', 1, 10000, 0, 15.00, 'EUR');

-- Caso B (misma prioridad, solape): producto 10001, cadena 1.
-- Dos tarifas con misma prioridad solapadas; gana la de fecha_inicio más reciente.
INSERT INTO precios (id_cadena, fecha_inicio, fecha_fin, lista_precios, id_producto, prioridad, precio, moneda)
VALUES
    (1, TIMESTAMP '2020-09-01 00:00:00', TIMESTAMP '2020-09-01 23:59:59', 30, 10001, 2, 11.11, 'EUR'),
    (1, TIMESTAMP '2020-09-01 08:00:00', TIMESTAMP '2020-09-01 12:00:00', 31, 10001, 2, 99.99, 'EUR');

-- Caso C (misma prioridad, mismo intervalo): producto 10002, cadena 1.
-- Dos filas con idéntica vigencia y prioridad; gana la de id mayor (segunda insertada).
INSERT INTO precios (id_cadena, fecha_inicio, fecha_fin, lista_precios, id_producto, prioridad, precio, moneda)
VALUES
    (1, TIMESTAMP '2020-09-02 09:00:00', TIMESTAMP '2020-09-02 17:00:00', 40, 10002, 3, 10.00, 'EUR'),
    (1, TIMESTAMP '2020-09-02 09:00:00', TIMESTAMP '2020-09-02 17:00:00', 41, 10002, 3, 20.00, 'EUR');
