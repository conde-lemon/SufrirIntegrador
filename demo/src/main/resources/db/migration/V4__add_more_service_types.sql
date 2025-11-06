-- ===================================================================
-- MIGRACIÓN V4: AÑADIR MÁS TIPOS DE SERVICIOS
-- 5 Vuelos adicionales + 5 Cruceros + 5 Buses
-- ===================================================================

-- ===== PROVEEDORES ADICIONALES =====
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
SELECT * FROM (VALUES
    ('VUELO', 'Lima a Roma', 'Peru', 'Italia', 'internacional,historia,cultura', 1450.00, 8, 'Descubre la Ciudad Eterna y su historia milenaria', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Copa Airlines' LIMIT 1), true),
    ('VUELO', 'Lima a Amsterdam', 'Peru', 'Holanda', 'internacional,europa,canales', 1320.00, 12, 'Explora los canales y museos de Amsterdam', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Viva Air' LIMIT 1), true),
    ('VUELO', 'Lima a Berlin', 'Peru', 'Alemania', 'internacional,historia,cultura', 1380.00, 10, 'Capital alemana llena de historia y modernidad', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'American Airlines' LIMIT 1), true),
    ('VUELO', 'Lima a Doha', 'Peru', 'Qatar', 'internacional,lujo,conexion', 1550.00, 6, 'Hub internacional de lujo en el Golfo Pérsico', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Qatar Airways' LIMIT 1), true),
    ('VUELO', 'Lima a Los Angeles', 'Peru', 'Estados Unidos', 'internacional,entretenimiento,playa', 920.00, 15, 'Ciudad de los Ángeles y Hollywood', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Emirates' LIMIT 1), true)
) AS v(tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = v.nombre);

-- ===== 5 CRUCEROS =====
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT * FROM (VALUES
    ('CRUCERO', 'Crucero Caribe Occidental', 'Miami', 'Caribe', 'crucero,playa,tropical,familiar', 899.00, 20, 'Crucero de 7 días por las islas del Caribe Occidental', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Royal Caribbean' LIMIT 1), true),
    ('CRUCERO', 'Crucero Mediterráneo Clásico', 'Barcelona', 'Mediterráneo', 'crucero,cultura,historia,europa', 1299.00, 15, 'Crucero de 10 días por las costas del Mediterráneo', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Norwegian Cruise Line' LIMIT 1), true),
    ('CRUCERO', 'Crucero Fiordos Noruegos', 'Bergen', 'Noruega', 'crucero,naturaleza,paisajes,aventura', 1899.00, 12, 'Crucero de 12 días por los espectaculares fiordos noruegos', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Celebrity Cruises' LIMIT 1), true),
    ('CRUCERO', 'Crucero Transatlántico', 'Southampton', 'New York', 'crucero,lujo,relajacion,oceano', 2199.00, 8, 'Crucero transatlántico de 14 días con máximo lujo', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'MSC Cruceros' LIMIT 1), true),
    ('CRUCERO', 'Crucero Alaska Glaciares', 'Seattle', 'Alaska', 'crucero,naturaleza,glaciares,vida salvaje', 1699.00, 10, 'Crucero de 8 días para ver glaciares y vida salvaje', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Princess Cruises' LIMIT 1), true)
) AS v(tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = v.nombre);

-- ===== 5 BUSES =====
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT * FROM (VALUES
    ('BUS', 'Lima a Huacachina', 'Peru', 'Peru', 'bus,nacional,oasis,aventura', 45.00, 30, 'Viaje cómodo al oasis de Huacachina en Ica', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Cruz del Sur' LIMIT 1), true),
    ('BUS', 'Lima a Paracas', 'Peru', 'Peru', 'bus,nacional,playa,naturaleza', 38.00, 35, 'Excursión a las Islas Ballestas y Reserva de Paracas', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Oltursa' LIMIT 1), true),
    ('BUS', 'Lima a Nazca', 'Peru', 'Peru', 'bus,nacional,misterio,lineas', 55.00, 25, 'Descubre las misteriosas Líneas de Nazca', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Movil Tours' LIMIT 1), true),
    ('BUS', 'Lima a Ayacucho', 'Peru', 'Peru', 'bus,nacional,historia,cultura', 85.00, 20, 'Ciudad histórica con arquitectura colonial', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Civa' LIMIT 1), true),
    ('BUS', 'Lima a Huaraz', 'Peru', 'Peru', 'bus,nacional,montaña,trekking', 75.00, 22, 'Puerta de entrada a la Cordillera Blanca', 
     (SELECT id_proveedor FROM proveedor WHERE nombre = 'Tepsa' LIMIT 1), true)
) AS v(tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = v.nombre);

-- Mensaje de confirmación
SELECT 'Migración V4 completada: 15 servicios nuevos agregados (5 vuelos, 5 cruceros, 5 buses)' as mensaje;