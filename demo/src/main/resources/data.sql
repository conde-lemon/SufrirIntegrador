-- =================================================================
-- DATOS DE EJEMPLO PARA USUARIOS Y RESERVAS
-- =================================================================

-- Insertar un usuario de tipo CLIENTE si no existe
INSERT INTO usuarios (id_usuario, nombres, apellidos, email, password, rol, activo, fecha_registro)
SELECT 100, 'Carlos', 'Cliente', 'cliente@travel4u.com', '1234', 'CLIENTE', true, '2025-10-01 10:00:00' -- CAMBIO: Contraseña en texto plano
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'cliente@travel4u.com');

-- (El resto de tu archivo data.sql permanece igual)

-- Insertar una Reserva 1 (Vuelo a Madrid) para el cliente
INSERT INTO reserva (id_reserva, estado, total, fecha_inicio, fecha_fin, moneda, id_usuario, created_at, observaciones)
SELECT 10, 'Confirmada', 1250.50, '2025-11-15 08:00:00', '2025-11-15 22:00:00', 'PEN', 100, '2025-10-02 11:00:00', 'Vuelo directo a Madrid con escala en Bogotá.'
    WHERE NOT EXISTS (SELECT 1 FROM reserva WHERE id_reserva = 10);

-- Detalle para la Reserva 1
INSERT INTO detalle_reserva (id_detalle_reserva, cantidad, precio_unitario, subtotal, id_reserva, id_servicio)
SELECT 10, 1, 1250.50, 1250.50, 10, (SELECT id_servicio FROM servicio WHERE nombre = 'Vuelo a Madrid')
    WHERE NOT EXISTS (SELECT 1 FROM detalle_reserva WHERE id_detalle_reserva = 10);

-- Pago para la Reserva 1
INSERT INTO pago (id_pago, monto, metodo_pago, estado_pago, fecha_pago, id_reserva)
SELECT 10, 1250.50, 'TARJETA_CREDITO', 'COMPLETADO', '2025-10-02 11:05:00', 10
    WHERE NOT EXISTS (SELECT 1 FROM pago WHERE id_pago = 10);


-- Insertar una Reserva 2 (Vuelo a Cusco) para el mismo cliente
INSERT INTO reserva (id_reserva, estado, total, fecha_inicio, fecha_fin, moneda, id_usuario, created_at, observaciones)
SELECT 11, 'Pendiente', 300.00, '2025-12-01 06:00:00', '2025-12-01 07:30:00', 'PEN', 100, '2025-10-10 15:00:00', 'Vuelo matutino a Cusco para 2 personas.'
    WHERE NOT EXISTS (SELECT 1 FROM reserva WHERE id_reserva = 11);

-- Detalle para la Reserva 2
INSERT INTO detalle_reserva (id_detalle_reserva, cantidad, precio_unitario, subtotal, id_reserva, id_servicio)
SELECT 11, 2, 150.00, 300.00, 11, (SELECT id_servicio FROM servicio WHERE nombre = 'Vuelo Económico a Cusco')
    WHERE NOT EXISTS (SELECT 1 FROM detalle_reserva WHERE id_detalle_reserva = 11);

-- No insertamos pago para la Reserva 2 para simular un estado 'Pendiente'.