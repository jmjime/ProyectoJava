-- Misma prioridad, mismo intervalo: desempate por id DESC (segunda inserción = id mayor = ganadora).
INSERT INTO precios (id_cadena, fecha_inicio, fecha_fin, lista_precios, id_producto, prioridad, precio, moneda)
VALUES
    (1, TIMESTAMP '2020-08-01 09:00:00', TIMESTAMP '2020-08-01 17:00:00', 20, 99902, 3, 10.00, 'EUR'),
    (1, TIMESTAMP '2020-08-01 09:00:00', TIMESTAMP '2020-08-01 17:00:00', 21, 99902, 3, 20.00, 'EUR');
