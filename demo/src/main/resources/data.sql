-- ===================================================================
-- SCRIPT DE INICIALIZACIÓN SEGURA PARA TRAVELRESERVA
-- Solo inserta datos que no existen para evitar duplicados
-- PRESERVA TODOS LOS DATOS EXISTENTES
-- ===================================================================

-- ===== USUARIOS =====
-- Usuario Administrador
INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Admin', 'Travel4U', 'admin@travel4u.com', '1234', '999999999', 'ADMIN', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@travel4u.com');

-- Usuarios de prueba
INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Carlos', 'Cliente', 'cliente@travel4u.com', '1234', '987654321', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'cliente@travel4u.com');

INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'María', 'González', 'maria.gonzalez@email.com', '1234', '912345678', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'maria.gonzalez@email.com');

INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Juan', 'Pérez', 'juan.perez@email.com', '1234', '923456789', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'juan.perez@email.com');

-- ===== PROVEEDORES =====
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 'Latam Airlines', 'AEROLINEA', 'contacto@latam.com', 'contacto@latam.com', '123456789', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Latam Airlines');

INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 'Iberia', 'AEROLINEA', 'contacto@iberia.com', 'contacto@iberia.com', '987654321', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Iberia');

INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 'Avianca', 'AEROLINEA', 'contacto@avianca.com', 'contacto@avianca.com', '555555555', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Avianca');

INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 'Sky Airline', 'AEROLINEA', 'contacto@skyairline.com', 'contacto@skyairline.com', '444444444', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Sky Airline');

INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 'JetSmart', 'AEROLINEA', 'contacto@jetsmart.com', 'contacto@jetsmart.com', '333333333', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'JetSmart');

-- ===== SERVICIOS (VUELOS) =====
-- Vuelos nacionales Perú
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo Económico a Cusco', 'Peru', 'Peru', 'top ventas,aventura,nacional', 150.00, 20, 'Descubre la magia de Cusco y Machu Picchu.', 1, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo Económico a Cusco');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Arequipa', 'Peru', 'Peru', 'nacional,ciudad blanca', 120.00, 15, 'Visita la hermosa Ciudad Blanca.', 1, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Arequipa');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Iquitos', 'Peru', 'Peru', 'nacional,selva,aventura', 180.00, 12, 'Explora la selva amazónica peruana.', 2, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Iquitos');

-- Vuelos internacionales desde Perú
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo a Madrid', 'Peru', 'Espana', 'vacaciones,familiar,top ventas,internacional', 1250.50, 15, 'Explora la capital de España y Europa.', 2, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo a Madrid');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Escapada a Buenos Aires', 'Peru', 'Argentina', 'invierno,gastronomia,internacional', 450.00, 10, 'Disfruta del tango y la buena comida argentina.', 1, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Escapada a Buenos Aires');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Conexión a París', 'Peru', 'Francia', 'romantico,vacaciones,internacional', 1100.00, 8, 'La ciudad del amor te espera.', 2, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Conexión a París');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Miami', 'Peru', 'Estados Unidos', 'playa,compras,internacional', 650.00, 18, 'Gateway a Estados Unidos y el Caribe.', 1, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Miami');

-- Vuelos regionales
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo Directo a Cancún', 'Mexico', 'Mexico', 'playa,verano,familiar', 380.00, 25, 'Relájate en las playas del Caribe mexicano.', 3, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo Directo a Cancún');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo a Lima', 'Brasil', 'Peru', 'negocios,puente aereo', 350.00, 18, 'Conexión directa entre São Paulo y Lima.', 3, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo a Lima');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo a Bogotá', 'Mexico', 'Colombia', 'negocios,cultural', 420.00, 12, 'Descubre la capital colombiana.', 3, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo a Bogotá');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Santiago a Lima', 'Chile', 'Peru', 'negocios,regional', 280.00, 20, 'Conexión entre las capitales del Pacífico.', 4, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Santiago a Lima');

-- ===== EQUIPAJES =====
INSERT INTO equipaje (tipo, peso_max, precio, dimension_largo, dimension_ancho, dimension_alto, descripcion, activo)
SELECT 'Equipaje de Mano', 8.00, 0.00, 55.00, 40.00, 20.00, 'Equipaje de mano estándar incluido', true
    WHERE NOT EXISTS (SELECT 1 FROM equipaje WHERE tipo = 'Equipaje de Mano');

INSERT INTO equipaje (tipo, peso_max, precio, dimension_largo, dimension_ancho, dimension_alto, descripcion, activo)
SELECT 'Equipaje Facturado 23kg', 23.00, 50.00, 75.00, 50.00, 30.00, 'Equipaje facturado estándar hasta 23kg', true
    WHERE NOT EXISTS (SELECT 1 FROM equipaje WHERE tipo = 'Equipaje Facturado 23kg');

INSERT INTO equipaje (tipo, peso_max, precio, dimension_largo, dimension_ancho, dimension_alto, descripcion, activo)
SELECT 'Equipaje Extra 32kg', 32.00, 80.00, 75.00, 50.00, 30.00, 'Equipaje facturado extra hasta 32kg', true
    WHERE NOT EXISTS (SELECT 1 FROM equipaje WHERE tipo = 'Equipaje Extra 32kg');

INSERT INTO equipaje (tipo, peso_max, precio, dimension_largo, dimension_ancho, dimension_alto, descripcion, activo)
SELECT 'Equipaje Deportivo', 30.00, 120.00, 100.00, 60.00, 40.00, 'Equipaje especial para equipos deportivos', true
    WHERE NOT EXISTS (SELECT 1 FROM equipaje WHERE tipo = 'Equipaje Deportivo');

-- ===== PAQUETES =====
INSERT INTO paquete (nombre, descripcion, precio_total, fecha_paquete)
SELECT 'Paquete Cusco Mágico', 'Incluye vuelo, hotel 3 estrellas y tour a Machu Picchu', 850.00, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM paquete WHERE nombre = 'Paquete Cusco Mágico');

INSERT INTO paquete (nombre, descripcion, precio_total, fecha_paquete)
SELECT 'Escapada a Europa', 'Vuelo a Madrid + 5 noches de hotel + city tour', 1800.00, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM paquete WHERE nombre = 'Escapada a Europa');

INSERT INTO paquete (nombre, descripcion, precio_total, fecha_paquete)
SELECT 'Aventura Amazónica', 'Vuelo a Iquitos + lodge en la selva + excursiones', 650.00, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM paquete WHERE nombre = 'Aventura Amazónica');

-- ===== OFERTAS/PROMOCIONES =====
INSERT INTO oferta (nombre, descripcion, precio, descuento, fecha_inicio, fecha_fin, etiquetas, url, fuente, fecha_extraccion, activa, fecha_creacion)
SELECT 'Super Oferta Cusco', 'Vuelo a Cusco con 30% de descuento', 150.00, 30.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days', 'cusco,descuento,nacional', null, 'SISTEMA', CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM oferta WHERE nombre = 'Super Oferta Cusco');

INSERT INTO oferta (nombre, descripcion, precio, descuento, fecha_inicio, fecha_fin, etiquetas, url, fuente, fecha_extraccion, activa, fecha_creacion)
SELECT 'Promo Europa Verano', 'Vuelos a Europa con hasta 25% de descuento', 1250.50, 25.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '60 days', 'europa,verano,internacional', null, 'SISTEMA', CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM oferta WHERE nombre = 'Promo Europa Verano');

INSERT INTO oferta (nombre, descripcion, precio, descuento, fecha_inicio, fecha_fin, etiquetas, url, fuente, fecha_extraccion, activa, fecha_creacion)
SELECT 'Flash Sale Buenos Aires', 'Oferta relámpago a Buenos Aires', 450.00, 20.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 'argentina,flash,regional', null, 'SISTEMA', CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM oferta WHERE nombre = 'Flash Sale Buenos Aires');

-- ===== SERVICIOS ADICIONALES =====
-- Más proveedores
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT * FROM (VALUES
    ('Copa Airlines', 'AEROLINEA', 'contacto@copa.com', 'contacto@copa.com', '111222333', true),
    ('Viva Air', 'AEROLINEA', 'contacto@vivaair.com', 'contacto@vivaair.com', '444555666', true),
    ('Peruvian Airlines', 'AEROLINEA', 'contacto@peruvian.com', 'contacto@peruvian.com', '777888999', true)
) AS v(nombre, tipo_proveedor, contacto, email, telefono, activo)
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = v.nombre);

-- Servicios internacionales y nacionales adicionales
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT * FROM (VALUES
    ('VUELO', 'Lima a New York', 'Peru', 'Estados Unidos', 'internacional,negocios', 850.00, 12, 'Vuelo directo a la Gran Manzana', 1, true),
    ('VUELO', 'Lima a Londres', 'Peru', 'Reino Unido', 'internacional,europa', 1100.00, 8, 'Conexión directa a Londres', 2, true),
    ('VUELO', 'Lima a Tokyo', 'Peru', 'Japon', 'internacional,asia', 1350.00, 6, 'Experiencia asiática única', 3, true),
    ('VUELO', 'Lima a Dubai', 'Peru', 'Emiratos Arabes', 'internacional,lujo', 1200.00, 10, 'Lujo en el desierto', 2, true),
    ('VUELO', 'Lima a Mexico DF', 'Peru', 'Mexico', 'regional,cultura', 650.00, 20, 'Capital azteca te espera', 4, true),
    ('VUELO', 'Lima a Quito', 'Peru', 'Ecuador', 'regional,montaña', 280.00, 25, 'Ciudad mitad del mundo', 5, true),
    ('VUELO', 'Lima a La Paz', 'Peru', 'Bolivia', 'regional,altura', 320.00, 18, 'Ciudad más alta del mundo', 1, true),
    ('VUELO', 'Lima a Panama', 'Peru', 'Panama', 'regional,canal', 580.00, 22, 'Canal de Panamá', 3, true),
    ('VUELO', 'Lima a Trujillo', 'Peru', 'Peru', 'nacional,costa', 95.00, 30, 'Ciudad de la eterna primavera', 2, true),
    ('VUELO', 'Lima a Piura', 'Peru', 'Peru', 'nacional,playa', 110.00, 28, 'Playas del norte peruano', 1, true),
    ('VUELO', 'Lima a Chiclayo', 'Peru', 'Peru', 'nacional,gastronomia', 105.00, 25, 'Capital gastronómica del norte', 3, true),
    ('VUELO', 'Lima a Tarapoto', 'Peru', 'Peru', 'nacional,selva', 140.00, 20, 'Puerta de entrada a la selva', 2, true),
    ('VUELO', 'Lima a Pucallpa', 'Peru', 'Peru', 'nacional,amazonia', 160.00, 18, 'Corazón de la Amazonía', 1, true),
    ('VUELO', 'Lima a Cajamarca', 'Peru', 'Peru', 'nacional,historia', 125.00, 22, 'Cuna de la cultura Inca', 4, true),
    ('VUELO', 'Lima a Huancayo', 'Peru', 'Peru', 'nacional,sierra', 115.00, 24, 'Valle del Mantaro', 5, true)
) AS v(tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = v.nombre);

-- ===== RESERVAS DE EJEMPLO =====
-- Reservas para usuarios existentes
INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 150.00, CURRENT_TIMESTAMP + INTERVAL '15 days', CURRENT_TIMESTAMP + INTERVAL '18 days', 'PEN', 'Reserva de ejemplo - Vuelo a Cusco', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u 
WHERE u.email = 'cliente@travel4u.com' 
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones LIKE '%Vuelo a Cusco%');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 1250.50, CURRENT_TIMESTAMP + INTERVAL '45 days', CURRENT_TIMESTAMP + INTERVAL '52 days', 'PEN', 'Reserva de ejemplo - Vuelo a Madrid', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u 
WHERE u.email = 'maria.gonzalez@email.com' 
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones LIKE '%Vuelo a Madrid%');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 450.00, CURRENT_TIMESTAMP + INTERVAL '30 days', CURRENT_TIMESTAMP + INTERVAL '35 days', 'PEN', 'Reserva de ejemplo - Buenos Aires', u.id_usuario, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM usuarios u 
WHERE u.email = 'juan.perez@email.com' 
AND NOT EXISTS (SELECT 1 FROM reserva r WHERE r.id_usuario = u.id_usuario AND r.observaciones LIKE '%Buenos Aires%');

-- Crear usuario ID 17 si no existe
INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
SELECT 'Usuario', 'Especial', 'usuario17@travel4u.com', '1234', '999888777', 'CLIENTE', CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17);

-- Reservas adicionales para usuario ID 17
INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 850.00, CURRENT_TIMESTAMP + INTERVAL '20 days', CURRENT_TIMESTAMP + INTERVAL '27 days', 'PEN', 'Viaje de negocios a New York', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17)
AND NOT EXISTS (SELECT 1 FROM reserva WHERE id_usuario = 17 AND observaciones = 'Viaje de negocios a New York');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 1100.00, CURRENT_TIMESTAMP + INTERVAL '35 days', CURRENT_TIMESTAMP + INTERVAL '42 days', 'PEN', 'Vacaciones en Londres', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17)
AND NOT EXISTS (SELECT 1 FROM reserva WHERE id_usuario = 17 AND observaciones = 'Vacaciones en Londres');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 280.00, CURRENT_TIMESTAMP + INTERVAL '10 days', CURRENT_TIMESTAMP + INTERVAL '13 days', 'PEN', 'Fin de semana en Quito', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17)
AND NOT EXISTS (SELECT 1 FROM reserva WHERE id_usuario = 17 AND observaciones = 'Fin de semana en Quito');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 95.00, CURRENT_TIMESTAMP + INTERVAL '50 days', CURRENT_TIMESTAMP + INTERVAL '53 days', 'PEN', 'Visita familiar en Trujillo', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17)
AND NOT EXISTS (SELECT 1 FROM reserva WHERE id_usuario = 17 AND observaciones = 'Visita familiar en Trujillo');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Pendiente', 650.00, CURRENT_TIMESTAMP + INTERVAL '60 days', CURRENT_TIMESTAMP + INTERVAL '67 days', 'PEN', 'Exploración cultural en México', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17)
AND NOT EXISTS (SELECT 1 FROM reserva WHERE id_usuario = 17 AND observaciones = 'Exploración cultural en México');

INSERT INTO reserva (estado, total, fecha_inicio, fecha_fin, moneda, observaciones, id_usuario, created_at, updated_at)
SELECT 'Confirmada', 140.00, CURRENT_TIMESTAMP + INTERVAL '25 days', CURRENT_TIMESTAMP + INTERVAL '28 days', 'PEN', 'Aventura en Tarapoto', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE EXISTS (SELECT 1 FROM usuarios WHERE id_usuario = 17)
AND NOT EXISTS (SELECT 1 FROM reserva WHERE id_usuario = 17 AND observaciones = 'Aventura en Tarapoto');

-- ===== SERVICIOS ADICIONALES - MÁS TIPOS =====
-- Proveedores de Cruceros
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT * FROM (VALUES
    ('Royal Caribbean', 'CRUCERO', 'contacto@royalcaribbean.com', 'contacto@royalcaribbean.com', '200300400', true),
    ('Norwegian Cruise Line', 'CRUCERO', 'contacto@ncl.com', 'contacto@ncl.com', '200300401', true),
    ('Celebrity Cruises', 'CRUCERO', 'contacto@celebrity.com', 'contacto@celebrity.com', '200300402', true),
    ('MSC Cruceros', 'CRUCERO', 'contacto@msc.com', 'contacto@msc.com', '200300403', true),
    ('Princess Cruises', 'CRUCERO', 'contacto@princess.com', 'contacto@princess.com', '200300404', true)
) AS v(nombre, tipo_proveedor, contacto, email, telefono, activo)
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = v.nombre);

-- Proveedores de Buses
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT * FROM (VALUES
    ('Cruz del Sur', 'BUS', 'contacto@cruzdelsur.com', 'contacto@cruzdelsur.com', '300400500', true),
    ('Oltursa', 'BUS', 'contacto@oltursa.com', 'contacto@oltursa.com', '300400501', true),
    ('Movil Tours', 'BUS', 'contacto@moviltours.com', 'contacto@moviltours.com', '300400502', true),
    ('Civa', 'BUS', 'contacto@civa.com', 'contacto@civa.com', '300400503', true),
    ('Tepsa', 'BUS', 'contacto@tepsa.com', 'contacto@tepsa.com', '300400504', true)
) AS v(nombre, tipo_proveedor, contacto, email, telefono, activo)
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = v.nombre);

-- Proveedores de Aerolíneas adicionales
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT * FROM (VALUES
    ('American Airlines', 'AEROLINEA', 'contacto@aa.com', 'contacto@aa.com', '400500600', true),
    ('Air France', 'AEROLINEA', 'contacto@airfrance.com', 'contacto@airfrance.com', '400500601', true),
    ('Lufthansa', 'AEROLINEA', 'contacto@lufthansa.com', 'contacto@lufthansa.com', '400500602', true),
    ('Emirates', 'AEROLINEA', 'contacto@emirates.com', 'contacto@emirates.com', '400500603', true),
    ('Qatar Airways', 'AEROLINEA', 'contacto@qatarairways.com', 'contacto@qatarairways.com', '400500604', true)
) AS v(nombre, tipo_proveedor, contacto, email, telefono, activo)
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = v.nombre);

-- ===== 5 VUELOS ADICIONALES =====
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Roma', 'Peru', 'Italia', 'internacional,historia,cultura', 1450.00, 8, 'Descubre la Ciudad Eterna y su historia milenaria', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'American Airlines' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Roma');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Amsterdam', 'Peru', 'Holanda', 'internacional,europa,canales', 1320.00, 12, 'Explora los canales y museos de Amsterdam', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Air France' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Amsterdam');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Berlin', 'Peru', 'Alemania', 'internacional,historia,cultura', 1380.00, 10, 'Capital alemana llena de historia y modernidad', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Lufthansa' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Berlin');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Doha', 'Peru', 'Qatar', 'internacional,lujo,conexion', 1550.00, 6, 'Hub internacional de lujo en el Golfo Pérsico', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Qatar Airways' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Doha');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Lima a Los Angeles', 'Peru', 'Estados Unidos', 'internacional,entretenimiento,playa', 920.00, 15, 'Ciudad de los Ángeles y Hollywood', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Emirates' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Los Angeles');

-- ===== 5 CRUCEROS =====
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'CRUCERO', 'Crucero Caribe Occidental', 'Miami', 'Caribe', 'crucero,playa,tropical,familiar', 899.00, 20, 'Crucero de 7 días por las islas del Caribe Occidental', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Royal Caribbean' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Caribe Occidental');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'CRUCERO', 'Crucero Mediterráneo Clásico', 'Barcelona', 'Mediterráneo', 'crucero,cultura,historia,europa', 1299.00, 15, 'Crucero de 10 días por las costas del Mediterráneo', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Norwegian Cruise Line' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Mediterráneo Clásico');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'CRUCERO', 'Crucero Fiordos Noruegos', 'Bergen', 'Noruega', 'crucero,naturaleza,paisajes,aventura', 1899.00, 12, 'Crucero de 12 días por los espectaculares fiordos noruegos', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Celebrity Cruises' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Fiordos Noruegos');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'CRUCERO', 'Crucero Transatlántico', 'Southampton', 'New York', 'crucero,lujo,relajacion,oceano', 2199.00, 8, 'Crucero transatlántico de 14 días con máximo lujo', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'MSC Cruceros' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Transatlántico');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'CRUCERO', 'Crucero Alaska Glaciares', 'Seattle', 'Alaska', 'crucero,naturaleza,glaciares,vida salvaje', 1699.00, 10, 'Crucero de 8 días para ver glaciares y vida salvaje', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Princess Cruises' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Alaska Glaciares');

-- ===== 5 BUSES =====
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'BUS', 'Lima a Huacachina', 'Peru', 'Peru', 'bus,nacional,oasis,aventura', 45.00, 30, 'Viaje cómodo al oasis de Huacachina en Ica', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Cruz del Sur' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Huacachina');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'BUS', 'Lima a Paracas', 'Peru', 'Peru', 'bus,nacional,playa,naturaleza', 38.00, 35, 'Excursión a las Islas Ballestas y Reserva de Paracas', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Oltursa' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Paracas');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'BUS', 'Lima a Nazca', 'Peru', 'Peru', 'bus,nacional,misterio,lineas', 55.00, 25, 'Descubre las misteriosas Líneas de Nazca', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Movil Tours' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Nazca');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'BUS', 'Lima a Ayacucho', 'Peru', 'Peru', 'bus,nacional,historia,cultura', 85.00, 20, 'Ciudad histórica con arquitectura colonial', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Civa' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Ayacucho');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'BUS', 'Lima a Huaraz', 'Peru', 'Peru', 'bus,nacional,montaña,trekking', 75.00, 22, 'Puerta de entrada a la Cordillera Blanca', 
       (SELECT id_proveedor FROM proveedor WHERE nombre = 'Tepsa' LIMIT 1), true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Huaraz');

-- ===== MENSAJE DE CONFIRMACIÓN =====
-- Script completado: servicios adicionales agregados (5 vuelos, 5 cruceros, 5 buses)