-- =============================================
-- MIGRACIÓN V3: AGREGAR MÁS SERVICIOS Y RESERVAS
-- Preserva datos existentes
-- =============================================

-- Insertar más proveedores si no existen
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT * FROM (VALUES
    ('Copa Airlines', 'AEROLINEA', 'contacto@copa.com', 'contacto@copa.com', '111222333', true),
    ('Viva Air', 'AEROLINEA', 'contacto@vivaair.com', 'contacto@vivaair.com', '444555666', true),
    ('Peruvian Airlines', 'AEROLINEA', 'contacto@peruvian.com', 'contacto@peruvian.com', '777888999', true),
    ('Star Peru', 'AEROLINEA', 'contacto@starperu.com', 'contacto@starperu.com', '101112131', true),
    ('LC Peru', 'AEROLINEA', 'contacto@lcperu.com', 'contacto@lcperu.com', '141516171', true)
) AS v(nombre, tipo_proveedor, contacto, email, telefono, activo)
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = v.nombre);

-- Insertar 15+ servicios nuevos
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT * FROM (VALUES
    ('VUELO', 'Lima a New York', 'Peru', 'Estados Unidos', 'internacional,negocios', 850.00, 12, 'Vuelo directo a la Gran Manzana', 1, true),
    ('VUELO', 'Lima a Londres', 'Peru', 'Reino Unido', 'internacional,europa', 1100.00, 8, 'Conexión directa a Londres', 2, true),
    ('VUELO', 'Lima a Tokyo', 'Peru', 'Japon', 'internacional,asia', 1350.00, 6, 'Experiencia asiática única', 3, true),
    ('VUELO', 'Lima a Sydney', 'Peru', 'Australia', 'internacional,oceania', 1450.00, 4, 'Aventura en Australia', 1, true),
    ('VUELO', 'Lima a Dubai', 'Peru', 'Emiratos Arabes', 'internacional,lujo', 1200.00, 10, 'Lujo en el desierto', 2, true),
    ('VUELO', 'Lima a Toronto', 'Peru', 'Canada', 'internacional,frio', 900.00, 15, 'Experiencia canadiense', 3, true),
    ('VUELO', 'Lima a Mexico DF', 'Peru', 'Mexico', 'regional,cultura', 650.00, 20, 'Capital azteca te espera', 4, true),
    ('VUELO', 'Lima a Quito', 'Peru', 'Ecuador', 'regional,montaña', 280.00, 25, 'Ciudad mitad del mundo', 5, true),
    ('VUELO', 'Lima a La Paz', 'Peru', 'Bolivia', 'regional,altura', 320.00, 18, 'Ciudad más alta del mundo', 6, true),
    ('VUELO', 'Lima a Caracas', 'Peru', 'Venezuela', 'regional,caribe', 480.00, 12, 'Bellezas caribeñas', 7, true),
    ('VUELO', 'Lima a Montevideo', 'Peru', 'Uruguay', 'regional,tranquilo', 420.00, 14, 'Tranquilidad uruguaya', 1, true),
    ('VUELO', 'Lima a Asuncion', 'Peru', 'Paraguay', 'regional,cultura', 380.00, 16, 'Corazón de Sudamérica', 2, true),
    ('VUELO', 'Lima a Panama', 'Peru', 'Panama', 'regional,canal', 580.00, 22, 'Canal de Panamá', 3, true),
    ('VUELO', 'Lima a San Jose', 'Peru', 'Costa Rica', 'regional,naturaleza', 620.00, 18, 'Pura vida costarricense', 4, true),
    ('VUELO', 'Lima a Guatemala', 'Peru', 'Guatemala', 'regional,maya', 680.00, 15, 'Cultura maya viva', 5, true),
    ('VUELO', 'Lima a Trujillo', 'Peru', 'Peru', 'nacional,costa', 95.00, 30, 'Ciudad de la eterna primavera', 6, true),
    ('VUELO', 'Lima a Piura', 'Peru', 'Peru', 'nacional,playa', 110.00, 28, 'Playas del norte peruano', 7, true),
    ('VUELO', 'Lima a Chiclayo', 'Peru', 'Peru', 'nacional,gastronomia', 105.00, 25, 'Capital gastronómica del norte', 1, true),
    ('VUELO', 'Lima a Tarapoto', 'Peru', 'Peru', 'nacional,selva', 140.00, 20, 'Puerta de entrada a la selva', 2, true),
    ('VUELO', 'Lima a Pucallpa', 'Peru', 'Peru', 'nacional,amazonia', 160.00, 18, 'Corazón de la Amazonía', 3, true)
) AS v(tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = v.nombre);

-- Insertar reservas para el usuario ID 17 (solo si existe)
INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT * FROM (VALUES
    ('Confirmada', 850.00, '2025-03-15 08:00:00'::timestamp, '2025-03-22 20:00:00'::timestamp, 'PEN', 'Viaje de negocios a New York', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Pendiente', 1100.00, '2025-04-10 14:30:00'::timestamp, '2025-04-17 16:45:00'::timestamp, 'PEN', 'Vacaciones en Londres', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Confirmada', 280.00, '2025-02-20 09:15:00'::timestamp, '2025-02-23 18:30:00'::timestamp, 'PEN', 'Fin de semana en Quito', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Confirmada', 95.00, '2025-05-05 07:00:00'::timestamp, '2025-05-08 21:00:00'::timestamp, 'PEN', 'Visita familiar en Trujillo', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Pendiente', 650.00, '2025-06-12 11:20:00'::timestamp, '2025-06-19 15:40:00'::timestamp, 'PEN', 'Exploración cultural en México', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Confirmada', 420.00, '2025-07-08 13:45:00'::timestamp, '2025-07-15 19:20:00'::timestamp, 'PEN', 'Relax en Montevideo', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Confirmada', 140.00, '2025-08-03 06:30:00'::timestamp, '2025-08-06 22:15:00'::timestamp, 'PEN', 'Aventura en Tarapoto', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Pendiente', 1200.00, '2025-09-20 16:00:00'::timestamp, '2025-09-27 12:30:00'::timestamp, 'PEN', 'Lujo en Dubai', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
) AS v(estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
WHERE EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17);

-- Mensaje de confirmación
SELECT 'Servicios y reservas adicionales agregados exitosamente' as mensaje;