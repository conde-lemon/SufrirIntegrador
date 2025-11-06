-- ===================================================================
-- SCRIPT DE INICIALIZACIÓN H2 PARA TRAVELRESERVA
-- Versión simplificada para H2 Database
-- ===================================================================

-- ===== USUARIOS =====
INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
VALUES ('Admin', 'Travel4U', 'admin@travel4u.com', '1234', '999999999', 'ADMIN', CURRENT_TIMESTAMP, true);

INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
VALUES ('Carlos', 'Cliente', 'cliente@travel4u.com', '1234', '987654321', 'CLIENTE', CURRENT_TIMESTAMP, true);

INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
VALUES ('María', 'González', 'maria.gonzalez@email.com', '1234', '912345678', 'CLIENTE', CURRENT_TIMESTAMP, true);

-- ===== PROVEEDORES =====
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
VALUES ('Latam Airlines', 'AEROLINEA', 'contacto@latam.com', 'contacto@latam.com', '123456789', true);

INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
VALUES ('Iberia', 'AEROLINEA', 'contacto@iberia.com', 'contacto@iberia.com', '987654321', true);

INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
VALUES ('Avianca', 'AEROLINEA', 'contacto@avianca.com', 'contacto@avianca.com', '555555555', true);

-- ===== SERVICIOS =====
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
VALUES ('VUELO', 'Vuelo Económico a Cusco', 'Peru', 'Peru', 'top ventas,aventura', 150.00, 20, 'Descubre la magia de Cusco.', 1, true);

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
VALUES ('VUELO', 'Vuelo a Madrid', 'Peru', 'Espana', 'vacaciones,familiar,top ventas', 1250.50, 15, 'Explora la capital de España.', 2, true);

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
VALUES ('VUELO', 'Escapada a Buenos Aires', 'Peru', 'Argentina', 'invierno,gastronomia', 450.00, 10, 'Disfruta del tango y la buena comida.', 1, true);

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
VALUES ('VUELO', 'Lima a Miami', 'Peru', 'Estados Unidos', 'playa,compras', 650.00, 18, 'Gateway a Estados Unidos.', 1, true);

-- ===== EQUIPAJES =====
INSERT INTO equipaje (tipo, peso_max, precio, dimension_largo, dimension_ancho, dimension_alto, descripcion, activo)
VALUES ('Equipaje de Mano', 8.00, 0.00, 55.00, 40.00, 20.00, 'Equipaje de mano estándar', true);

INSERT INTO equipaje (tipo, peso_max, precio, dimension_largo, dimension_ancho, dimension_alto, descripcion, activo)
VALUES ('Equipaje Facturado 23kg', 23.00, 50.00, 75.00, 50.00, 30.00, 'Equipaje facturado estándar', true);

-- ===== OFERTAS =====
INSERT INTO oferta (nombre, descripcion, precio, descuento, fecha_inicio, fecha_fin, etiquetas, fuente, fecha_extraccion, activa, fecha_creacion)
VALUES ('Super Oferta Cusco', 'Vuelo a Cusco con 30% de descuento', 150.00, 30.00, CURRENT_TIMESTAMP, DATEADD('DAY', 30, CURRENT_TIMESTAMP), 'cusco,descuento', 'SISTEMA', CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP);

INSERT INTO oferta (nombre, descripcion, precio, descuento, fecha_inicio, fecha_fin, etiquetas, fuente, fecha_extraccion, activa, fecha_creacion)
VALUES ('Promo Europa Verano', 'Vuelos a Europa con 25% de descuento', 1250.50, 25.00, CURRENT_TIMESTAMP, DATEADD('DAY', 60, CURRENT_TIMESTAMP), 'europa,verano', 'SISTEMA', CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP);