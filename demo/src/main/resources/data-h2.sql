-- Script SQL para H2 Database
-- Insertar Usuario Administrador
INSERT INTO usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro, activo)
VALUES ('Admin', 'Travel4U', 'admin@travel4u.com', '1234', '999999999', 'ADMIN', CURRENT_TIMESTAMP, true);

-- Insertar Proveedores
INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
VALUES ('Latam Airlines', 'AEROLINEA', 'contacto@latam.com', 'contacto@latam.com', '123456789', true);

INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo)
VALUES ('Iberia', 'AEROLINEA', 'contacto@iberia.com', 'contacto@iberia.com', '987654321', true);

-- Insertar Servicios
INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
VALUES ('VUELO', 'Vuelo Económico a Cusco', 'Peru', 'Peru', 'top ventas,aventura', 150.00, 20, 'Descubre la magia de Cusco.', 1, true);

INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo)
VALUES ('VUELO', 'Vuelo a Madrid', 'Peru', 'Espana', 'vacaciones,familiar,top ventas', 1250.50, 15, 'Explora la capital de España.', 2, true);