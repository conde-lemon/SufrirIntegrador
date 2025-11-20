-- Inicialización de base de datos para Docker
-- Este archivo se ejecuta automáticamente al crear el contenedor de PostgreSQL

-- Crear extensiones si es necesario
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Insertar datos iniciales de equipaje
INSERT INTO equipaje (tipo, peso_max, precio, dimension_largo, dimension_ancho, dimension_alto, descripcion, activo) VALUES
('Equipaje de mano', 10.00, 0.00, 55.00, 40.00, 20.00, 'Equipaje de mano estándar incluido', true),
('Maleta 23kg', 23.00, 50.00, 75.00, 50.00, 30.00, 'Maleta facturada hasta 23kg', true),
('Maleta 32kg', 32.00, 80.00, 75.00, 50.00, 30.00, 'Maleta facturada hasta 32kg', true),
('Equipaje deportivo', 30.00, 100.00, 150.00, 50.00, 30.00, 'Equipaje para deportes especiales', true)
ON CONFLICT DO NOTHING;

-- Insertar servicios de ejemplo
INSERT INTO servicio (nombre, descripcion, precio_base, origen, destino, tipo_servicio, activo) VALUES
('Vuelo Lima - Madrid', 'Vuelo directo Lima a Madrid', 1200.00, 'Lima (LIM)', 'Madrid (MAD)', 'Vuelo', true),
('Bus Lima - Arequipa', 'Bus cama Lima a Arequipa', 80.00, 'Lima', 'Arequipa', 'Bus', true),
('Crucero Caribe', 'Crucero 7 días por el Caribe', 2500.00, 'Callao', 'Caribe', 'Crucero', true)
ON CONFLICT DO NOTHING;