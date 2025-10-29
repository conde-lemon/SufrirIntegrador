-- Este script se ejecuta al iniciar la aplicación para poblar la base de datos.
-- Se insertan datos solo si las tablas están vacías para evitar duplicados.

-- Insertar Proveedores si no existen
INSERT INTO proveedor (id_proveedor, nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 1, 'Latam Airlines', 'AEROLINEA', 'contacto@latam.com', 'contacto@latam.com', '123456789', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE id_proveedor = 1);

INSERT INTO proveedor (id_proveedor, nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 2, 'Iberia', 'AEROLINEA', 'contacto@iberia.com', 'contacto@iberia.com', '987654321', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE id_proveedor = 2);

INSERT INTO proveedor (id_proveedor, nombre, tipo_proveedor, contacto, email, telefono, activo)
SELECT 3, 'Avianca', 'AEROLINEA', 'contacto@avianca.com', 'contacto@avianca.com', '555555555', true
    WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE id_proveedor = 3);


-- Insertar Servicios (Vuelos) si no existen
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo Económico a Cusco', 'Peru', 'Peru', 'top ventas,aventura', 150.00, 20, 'Descubre la magia de Cusco.', 1, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo Económico a Cusco');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo a Madrid', 'Peru', 'Espana', 'vacaciones,familiar,top ventas', 1250.50, 15, 'Explora la capital de España.', 2, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo a Madrid');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Escapada a Buenos Aires', 'Peru', 'Argentina', 'invierno,gastronomia', 450.00, 10, 'Disfruta del tango y la buena comida.', 1, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Escapada a Buenos Aires');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo Directo a Cancún', 'Mexico', 'Mexico', 'playa,verano,familiar', 380.00, 25, 'Relájate en las playas del Caribe.', 1, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo Directo a Cancún');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Conexión a París', 'Colombia', 'Francia', 'romantico,vacaciones', 1100.00, 8, 'La ciudad del amor te espera.', 2, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Conexión a París');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo a Lima', 'Brasil', 'Peru', 'negocios,puente aereo', 350.00, 18, 'Conexión directa entre Sao Paulo y Lima.', 3, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo a Lima');

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
SELECT 'VUELO', 'Vuelo a Bogotá', 'Mexico', 'Colombia', 'negocios,cultural', 420.00, 12, 'Descubre la capital colombiana.', 3, true
    WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Vuelo a Bogotá');