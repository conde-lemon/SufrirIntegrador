-- Limpiamos las tablas en el orden correcto para evitar errores de clave foránea.
-- Se debe empezar por las tablas más "hijas" (las que tienen las claves foráneas).

DELETE FROM reserva_equipaje;  -- ¡NUEVO! La última dependencia encontrada.
DELETE FROM paquete_reserva;
DELETE FROM pago;
DELETE FROM detalle_reserva;
DELETE FROM reserva;
DELETE FROM paquete;
DELETE FROM servicio;
DELETE FROM proveedor;

-- --- AHORA INSERTAMOS LOS DATOS PARA LA PRUEBA ---

-- Asumimos que necesitamos un proveedor para los servicios.
INSERT INTO proveedor (id_proveedor, nombre, tipo_proveedor, activo) VALUES (1, 'Test Airline', 'AEROLINEA', true);

-- Insertamos el vuelo que SÍ debe ser encontrado por la primera prueba.
INSERT INTO servicio (id_servicio, tipo_servicio, nombre, origen, destino, precio_base, disponibilidad, activo, id_proveedor)
VALUES (100, 'VUELO', 'Vuelo a Madrid', 'Peru', 'Espana', 850.00, 100, true, 1);

-- Insertamos otro vuelo que servirá como SUGERENCIA para la segunda prueba.
INSERT INTO servicio (id_servicio, tipo_servicio, nombre, origen, destino, precio_base, disponibilidad, activo, id_proveedor)
VALUES (101, 'VUELO', 'Vuelo a Buenos Aires', 'Chile', 'Argentina', 300.00, 50, true, 1);

-- Insertamos un vuelo INACTIVO para asegurarnos de que no es encontrado ni sugerido.
INSERT INTO servicio (id_servicio, tipo_servicio, nombre, origen, destino, precio_base, disponibilidad, activo, id_proveedor)
VALUES (102, 'VUELO', 'Vuelo a Cancun (Inactivo)', 'Colombia', 'Mexico', 450.00, 20, false, 1);