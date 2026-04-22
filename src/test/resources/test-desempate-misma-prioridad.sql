-- Dos tarifas solapadas con la misma prioridad (producto ficticio 99901): gana la de fecha_inicio más reciente.
INSERT INTO precios (id_cadena, fecha_inicio, fecha_fin, lista_precios, id_producto, prioridad, precio, moneda)
VALUES
    (1, TIMESTAMP '2020-07-01 00:00:00', TIMESTAMP '2020-07-01 23:59:59', 10, 99901, 5, 11.11, 'EUR'),
    (1, TIMESTAMP '2020-07-01 08:00:00', TIMESTAMP '2020-07-01 12:00:00', 11, 99901, 5, 99.99, 'EUR');
