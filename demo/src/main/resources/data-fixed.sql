-- ===================================================================
-- SCRIPT DE INICIALIZACIÓN COMPLETA Y SEGURA PARA TRAVELRESERVA
-- Corrige problemas de sintaxis PostgreSQL y agrega múltiples reservas
-- ===================================================================

-- ===== USUARIOS =====
INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Admin', 'Travel4U', 'admin@travel4u.com', '1234', '999999999', 'ADMIN', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@travel4u.com');

INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Carlos', 'Cliente', 'cliente@travel4u.com', '1234', '987654321', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'cliente@travel4u.com');

INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'María', 'González', 'maria.gonzalez@email.com', '1234', '912345678', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'maria.gonzalez@email.com');

INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Juan', 'Pérez', 'juan.perez@email.com', '1234', '923456789', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'juan.perez@email.com');

-- Usuario VIP con ID específico para las reservas
INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Usuario', 'VIP', 'usuario.vip@travel4u.com', '1234', '999888777', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'usuario.vip@travel4u.com');

-- ===== PROVEEDORES =====
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT * FROM (VALUES
    ('Latam Airlines', 'AEROLINEA', 'contacto@latam.com', 'contacto@latam.com', '123456789', true),
    ('Iberia', 'AEROLINEA', 'contacto@iberia.com', 'contacto@iberia.com', '987654321', true),
    ('Avianca', 'AEROLINEA', 'contacto@avianca.com', 'contacto@avianca.com', '555555555', true),
    ('Sky Airline', 'AEROLINEA', 'contacto@skyairline.com', 'contacto@skyairline.com', '444444444', true),
    ('JetSmart', 'AEROLINEA', 'contacto@jetsmart.com', 'contacto@jetsmart.com', '333333333', true),
    ('Copa Airlines', 'AEROLINEA', 'contacto@copa.com', 'contacto@copa.com', '111222333', true),
    ('Viva Air', 'AEROLINEA', 'contacto@vivaair.com', 'contacto@vivaair.com', '444555666', true),
    ('Peruvian Airlines', 'AEROLINEA', 'contacto@peruvian.com', 'contacto@peruvian.com', '777888999', true)
) AS v(nombre, tipo_proveedor, contacto, email, telefono, activo)
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = v.nombre);

-- ===== SERVICIOS (20+ VUELOS) =====
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT * FROM (VALUES
    ('VUELO', 'Vuelo Económico a Cusco', 'Peru', 'Peru', 'top ventas,aventura,nacional', 150.00, 20, 'Descubre la magia de Cusco y Machu Picchu.', 1, true),
    ('VUELO', 'Vuelo a Madrid', 'Peru', 'Espana', 'vacaciones,familiar,top ventas,internacional', 1250.50, 15, 'Explora la capital de España y Europa.', 2, true),
    ('VUELO', 'Lima a New York', 'Peru', 'Estados Unidos', 'internacional,negocios', 850.00, 12, 'Vuelo directo a la Gran Manzana', 1, true),
    ('VUELO', 'Lima a Londres', 'Peru', 'Reino Unido', 'internacional,europa', 1100.00, 8, 'Conexión directa a Londres', 2, true),
    ('VUELO', 'Lima a Tokyo', 'Peru', 'Japon', 'internacional,asia', 1350.00, 6, 'Experiencia asiática única', 3, true),
    ('VUELO', 'Lima a Dubai', 'Peru', 'Emiratos Arabes', 'internacional,lujo', 1200.00, 10, 'Lujo en el desierto', 2, true),
    ('VUELO', 'Lima a Mexico DF', 'Peru', 'Mexico', 'regional,cultura', 650.00, 20, 'Capital azteca te espera', 4, true),
    ('VUELO', 'Lima a Quito', 'Peru', 'Ecuador', 'regional,montaña', 280.00, 25, 'Ciudad mitad del mundo', 5, true),
    ('VUELO', 'Lima a La Paz', 'Peru', 'Bolivia', 'regional,altura', 320.00, 18, 'Ciudad más alta del mundo', 1, true),
    ('VUELO', 'Lima a Buenos Aires', 'Peru', 'Argentina', 'regional,tango', 450.00, 16, 'Ciudad del tango', 3, true),
    ('VUELO', 'Lima a Santiago', 'Peru', 'Chile', 'regional,vino', 380.00, 22, 'Capital chilena', 4, true),
    ('VUELO', 'Lima a Bogotá', 'Peru', 'Colombia', 'regional,café', 420.00, 18, 'Capital cafetera', 5, true),
    ('VUELO', 'Lima a Caracas', 'Peru', 'Venezuela', 'regional,caribe', 480.00, 12, 'Bellezas caribeñas', 6, true),
    ('VUELO', 'Lima a Panama', 'Peru', 'Panama', 'regional,canal', 580.00, 22, 'Canal de Panamá', 7, true),
    ('VUELO', 'Lima a Montevideo', 'Peru', 'Uruguay', 'regional,tranquilo', 420.00, 14, 'Tranquilidad uruguaya', 8, true),
    ('VUELO', 'Lima a Trujillo', 'Peru', 'Peru', 'nacional,costa', 95.00, 30, 'Ciudad de la eterna primavera', 2, true),
    ('VUELO', 'Lima a Arequipa', 'Peru', 'Peru', 'nacional,ciudad blanca', 120.00, 25, 'La Ciudad Blanca', 1, true),
    ('VUELO', 'Lima a Iquitos', 'Peru', 'Peru', 'nacional,selva,aventura', 180.00, 20, 'Explora la selva amazónica', 3, true),
    ('VUELO', 'Lima a Piura', 'Peru', 'Peru', 'nacional,playa', 110.00, 28, 'Playas del norte peruano', 4, true),
    ('VUELO', 'Lima a Tarapoto', 'Peru', 'Peru', 'nacional,selva', 140.00, 20, 'Puerta de entrada a la selva', 5, true)
) AS v(tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = v.nombre);

-- ===== RESERVAS MÚLTIPLES =====
-- Reservas para usuarios existentes
INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 150.00, CURRENT_TIMESTAMP + INTERVAL '15 days', CURRENT_TIMESTAMP + INTERVAL '18 days', 'PEN', 'Reserva - Vuelo a Cusco', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'cliente@travel4u.com' 
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones LIKE '%Vuelo a Cusco%');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 1250.50, CURRENT_TIMESTAMP + INTERVAL '45 days', CURRENT_TIMESTAMP + INTERVAL '52 days', 'PEN', 'Reserva - Vuelo a Madrid', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'maria.gonzalez@email.com' 
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones LIKE '%Vuelo a Madrid%');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 450.00, CURRENT_TIMESTAMP + INTERVAL '30 days', CURRENT_TIMESTAMP + INTERVAL '35 days', 'PEN', 'Reserva - Buenos Aires', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'juan.perez@email.com' 
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones LIKE '%Buenos Aires%');

-- Múltiples reservas para usuario VIP
INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 850.00, CURRENT_TIMESTAMP + INTERVAL '20 days', CURRENT_TIMESTAMP + INTERVAL '27 days', 'PEN', 'Viaje de negocios a New York', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Viaje de negocios a New York');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 1100.00, CURRENT_TIMESTAMP + INTERVAL '35 days', CURRENT_TIMESTAMP + INTERVAL '42 days', 'PEN', 'Vacaciones en Londres', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Vacaciones en Londres');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 280.00, CURRENT_TIMESTAMP + INTERVAL '10 days', CURRENT_TIMESTAMP + INTERVAL '13 days', 'PEN', 'Fin de semana en Quito', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Fin de semana en Quito');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 95.00, CURRENT_TIMESTAMP + INTERVAL '50 days', CURRENT_TIMESTAMP + INTERVAL '53 days', 'PEN', 'Visita familiar en Trujillo', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Visita familiar en Trujillo');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 650.00, CURRENT_TIMESTAMP + INTERVAL '60 days', CURRENT_TIMESTAMP + INTERVAL '67 days', 'PEN', 'Exploración cultural en México', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Exploración cultural en México');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 420.00, CURRENT_TIMESTAMP + INTERVAL '35 days', CURRENT_TIMESTAMP + INTERVAL '42 days', 'PEN', 'Relax en Montevideo', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Relax en Montevideo');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 140.00, CURRENT_TIMESTAMP + INTERVAL '25 days', CURRENT_TIMESTAMP + INTERVAL '28 days', 'PEN', 'Aventura en Tarapoto', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Aventura en Tarapoto');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 1200.00, CURRENT_TIMESTAMP + INTERVAL '60 days', CURRENT_TIMESTAMP + INTERVAL '67 days', 'PEN', 'Lujo en Dubai', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'usuario.vip@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Lujo en Dubai');

-- Reservas adicionales para otros usuarios
INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 380.00, CURRENT_TIMESTAMP + INTERVAL '40 days', CURRENT_TIMESTAMP + INTERVAL '47 days', 'PEN', 'Escapada a Santiago', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'admin@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Escapada a Santiago');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 180.00, CURRENT_TIMESTAMP + INTERVAL '55 days', CURRENT_TIMESTAMP + INTERVAL '58 days', 'PEN', 'Aventura en Iquitos', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u WHERE u.email = 'cliente@travel4u.com'
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones = 'Aventura en Iquitos');

-- ===== MENSAJE DE CONFIRMACIÓN =====
SELECT 'Script de inicialización completa ejecutado exitosamente' as mensaje;