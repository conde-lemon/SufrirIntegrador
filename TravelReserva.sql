-- Eliminar tablas si existen (para limpieza)
DROP TABLE IF EXISTS public.servicio_proveedor CASCADE;
DROP TABLE IF EXISTS public.paquete_reserva CASCADE;
DROP TABLE IF EXISTS public.reserva_equipaje CASCADE;
DROP TABLE IF EXISTS public.detalle_reserva CASCADE;
DROP TABLE IF EXISTS public.pago CASCADE;
DROP TABLE IF EXISTS public.historial CASCADE;
DROP TABLE IF EXISTS public.reserva CASCADE;
DROP TABLE IF EXISTS public.servicio CASCADE;
DROP TABLE IF EXISTS public.paquete CASCADE;
DROP TABLE IF EXISTS public.equipaje CASCADE;
DROP TABLE IF EXISTS public.oferta CASCADE;
DROP TABLE IF EXISTS public.proveedor CASCADE;
DROP TABLE IF EXISTS public.usuarios CASCADE;

SELECT COUNT(*) FROM reserva;

-- Tablas principales
CREATE TABLE public.usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    rol VARCHAR(50) NOT NULL DEFAULT 'usuario',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT true
);

CREATE TABLE public.proveedor (
    id_proveedor SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    tipo_proveedor VARCHAR(100) NOT NULL,
    contacto VARCHAR(200),
    email VARCHAR(255),
    telefono VARCHAR(20),
    activo BOOLEAN DEFAULT true
);

CREATE TABLE public.equipaje (
    id_equipaje SERIAL PRIMARY KEY,
    tipo VARCHAR(100) NOT NULL,
    dimension_alto NUMERIC(8,2) NOT NULL,
    dimension_ancho NUMERIC(8,2) NOT NULL,
    dimension_largo NUMERIC(8,2) NOT NULL,
    peso_max NUMERIC(8,2) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT true,
    CONSTRAINT chk_dimensiones_positivas CHECK (dimension_alto > 0 AND dimension_ancho > 0 AND dimension_largo > 0),
    CONSTRAINT chk_precio_positivo CHECK (precio >= 0)
);

CREATE TABLE public.paquete (
    id_paquete SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio_total NUMERIC(10,2) NOT NULL,
    fecha_paquete TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT true,
    CONSTRAINT chk_precio_total_positivo CHECK (precio_total >= 0)
);

CREATE TABLE public.oferta (
    id_oferta BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL,
    etiquetas VARCHAR(500),
    url VARCHAR(500),
    fuente VARCHAR(100),
    fecha_extraccion TIMESTAMP,
    activa BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_precio_oferta_positivo CHECK (precio >= 0)
);

-- Tabla principal de reservas
CREATE TABLE public.reserva (
    id_reserva SERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    total NUMERIC(12,2) NOT NULL,
    moneda VARCHAR(3) DEFAULT 'USD',
    observaciones TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario),
    CONSTRAINT chk_total_positivo CHECK (total >= 0),
    CONSTRAINT chk_fechas_validas CHECK (fecha_fin IS NULL OR fecha_inicio IS NULL OR fecha_fin > fecha_inicio)
);

-- Tablas de servicios
CREATE TABLE public.servicio (
    id_servicio BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    tipo_servicio VARCHAR(100) NOT NULL,
    precio_base NUMERIC(10,2) NOT NULL,
    disponibilidad INTEGER NOT NULL DEFAULT 0,
    id_proveedor INTEGER,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_servicio_proveedor FOREIGN KEY (id_proveedor) REFERENCES public.proveedor(id_proveedor),
    CONSTRAINT chk_precio_base_positivo CHECK (precio_base >= 0),
    CONSTRAINT chk_disponibilidad_no_negativa CHECK (disponibilidad >= 0)
);

CREATE TABLE public.detalle_reserva (
    id_detalle_reserva SERIAL PRIMARY KEY,
    id_reserva INTEGER NOT NULL,
    id_servicio BIGINT NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_detalle_reserva FOREIGN KEY (id_reserva) REFERENCES public.reserva(id_reserva) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_servicio FOREIGN KEY (id_servicio) REFERENCES public.servicio(id_servicio),
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad > 0),
    CONSTRAINT chk_precio_unitario_positivo CHECK (precio_unitario >= 0),
    CONSTRAINT chk_subtotal_positivo CHECK (subtotal >= 0)
);

-- Tablas de relaciones muchos-a-muchos
CREATE TABLE public.reserva_equipaje (
    id_reserva_equipaje SERIAL PRIMARY KEY,
    id_reserva INTEGER NOT NULL,
    id_equipaje INTEGER NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reserva_equipaje_reserva FOREIGN KEY (id_reserva) REFERENCES public.reserva(id_reserva) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_equipaje_equipaje FOREIGN KEY (id_equipaje) REFERENCES public.equipaje(id_equipaje),
    CONSTRAINT chk_cantidad_equipaje_positiva CHECK (cantidad > 0),
    CONSTRAINT chk_precio_equipaje_positivo CHECK (precio_unitario >= 0),
    CONSTRAINT chk_subtotal_equipaje_positivo CHECK (subtotal >= 0),
    CONSTRAINT uq_reserva_equipaje UNIQUE (id_reserva, id_equipaje)
);

CREATE TABLE public.paquete_reserva (
    id_paquete_reserva SERIAL PRIMARY KEY,
    id_paquete INTEGER NOT NULL,
    id_reserva INTEGER NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_paquete_reserva_paquete FOREIGN KEY (id_paquete) REFERENCES public.paquete(id_paquete),
    CONSTRAINT fk_paquete_reserva_reserva FOREIGN KEY (id_reserva) REFERENCES public.reserva(id_reserva) ON DELETE CASCADE,
    CONSTRAINT chk_cantidad_paquete_positiva CHECK (cantidad > 0),
    CONSTRAINT chk_precio_paquete_positivo CHECK (precio_unitario >= 0),
    CONSTRAINT chk_subtotal_paquete_positivo CHECK (subtotal >= 0),
    CONSTRAINT uq_paquete_reserva UNIQUE (id_reserva, id_paquete)
);

-- Tablas transaccionales
CREATE TABLE public.pago (
    id_pago SERIAL PRIMARY KEY,
    id_reserva INTEGER NOT NULL,
    monto NUMERIC(12,2) NOT NULL,
    metodo_pago VARCHAR(100) NOT NULL,
    estado_pago VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    fecha_pago TIMESTAMP,
    referencia_pago VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pago_reserva FOREIGN KEY (id_reserva) REFERENCES public.reserva(id_reserva),
    CONSTRAINT chk_monto_positivo CHECK (monto >= 0),
    CONSTRAINT uq_pago_reserva UNIQUE (id_reserva)
);

CREATE TABLE public.historial (
    id_historial SERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    termino_busqueda VARCHAR(500) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resultados INTEGER DEFAULT 0,
    CONSTRAINT fk_historial_usuario FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario) ON DELETE CASCADE
);

-- Tabla de relación servicio-proveedor
CREATE TABLE public.servicio_proveedor (
    id_servicio_proveedor SERIAL PRIMARY KEY,
    id_servicio BIGINT NOT NULL,
    id_proveedor INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_servicio_proveedor_servicio FOREIGN KEY (id_servicio) REFERENCES public.servicio(id_servicio) ON DELETE CASCADE,
    CONSTRAINT fk_servicio_proveedor_proveedor FOREIGN KEY (id_proveedor) REFERENCES public.proveedor(id_proveedor) ON DELETE CASCADE,
    CONSTRAINT uq_servicio_proveedor UNIQUE (id_servicio, id_proveedor)
);

-- =============================================
-- INSERTAR DATOS DE PRUEBA (15+ REGISTROS)
-- =============================================

-- Insertar 15 usuarios
INSERT INTO public.usuarios (nombres, apellidos, email, password, telefono, rol, fecha_registro) VALUES
('María', 'González', 'maria.gonzalez@email.com', 'password123', '+1234567890', 'usuario', '2025-01-15 10:30:00'),
('Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', 'password123', '+1234567891', 'usuario', '2025-02-20 14:15:00'),
('Ana', 'Martínez', 'ana.martinez@email.com', 'password123', '+1234567892', 'usuario', '2025-03-10 09:45:00'),
('Pedro', 'López', 'pedro.lopez@email.com', 'password123', '+1234567893', 'admin', '2025-01-05 16:20:00'),
('Laura', 'Hernández', 'laura.hernandez@email.com', 'password123', '+1234567894', 'usuario', '2025-04-18 11:10:00'),
('Javier', 'Díaz', 'javier.diaz@email.com', 'password123', '+1234567895', 'usuario', '2025-05-22 13:25:00'),
('Sofia', 'Ramírez', 'sofia.ramirez@email.com', 'password123', '+1234567896', 'usuario', '2025-01-08 08:20:00'),
('Diego', 'Castillo', 'diego.castillo@email.com', 'password123', '+1234567897', 'usuario', '2025-02-14 12:45:00'),
('Elena', 'Morales', 'elena.morales@email.com', 'password123', '+1234567898', 'usuario', '2025-03-22 15:30:00'),
('Ricardo', 'Silva', 'ricardo.silva@email.com', 'password123', '+1234567899', 'admin', '2025-04-05 09:15:00'),
('Carmen', 'Ortega', 'carmen.ortega@email.com', 'password123', '+1234567800', 'usuario', '2025-05-30 14:50:00'),
('Fernando', 'Reyes', 'fernando.reyes@email.com', 'password123', '+1234567801', 'usuario', '2025-06-12 11:25:00'),
('Gabriela', 'Vargas', 'gabriela.vargas@email.com', 'password123', '+1234567802', 'usuario', '2025-07-18 16:40:00'),
('Héctor', 'Mendoza', 'hector.mendoza@email.com', 'password123', '+1234567803', 'usuario', '2025-08-24 10:10:00'),
('Isabel', 'Cruz', 'isabel.cruz@email.com', 'password123', '+1234567804', 'usuario', '2025-09-03 13:35:00');

-- Insertar 15 proveedores
INSERT INTO public.proveedor (nombre, tipo_proveedor, contacto, email, telefono) VALUES
('Hotel Paradise', 'hospedaje', 'Juan Pérez', 'reservas@hotelparadise.com', '+1-800-123456'),
('AeroViajes', 'transporte', 'María Sánchez', 'contacto@aeroviajes.com', '+1-800-654321'),
('Tour Adventures', 'turismo', 'Carlos Ruiz', 'info@touradventures.com', '+1-800-987654'),
('RentCar Express', 'transporte', 'Ana Morales', 'alquiler@rentcarexpress.com', '+1-800-456789'),
('Gourmet Experiences', 'alimentacion', 'Laura Castro', 'eventos@gourmetexp.com', '+1-800-321654'),
('Beach Resort', 'hospedaje', 'Roberto Navarro', 'info@beachresort.com', '+1-800-111222'),
('Mountain Hostel', 'hospedaje', 'Patricia Jiménez', 'bookings@mountainhostel.com', '+1-800-333444'),
('City Bus Tours', 'transporte', 'Alberto Rios', 'tours@citybus.com', '+1-800-555666'),
('Adventure Guides', 'turismo', 'Sandra López', 'guides@adventure.com', '+1-800-777888'),
('Luxury Suites', 'hospedaje', 'Miguel Ángel Torres', 'reservas@luxurysuites.com', '+1-800-999000'),
('Fast Rentals', 'transporte', 'Daniela Moreno', 'rent@fastrentals.com', '+1-800-112233'),
('Cultural Experiences', 'turismo', 'Raúl Fernández', 'culture@experiences.com', '+1-800-445566'),
('Gastronomic Tours', 'alimentacion', 'Verónica Castro', 'food@gastrotours.com', '+1-800-778899'),
('Eco Adventures', 'turismo', 'Oscar Martínez', 'eco@adventures.com', '+1-800-001122'),
('Business Travel', 'transporte', 'Lucía Herrera', 'corporate@businesstravel.com', '+1-800-334455');

-- Insertar 15 equipajes
INSERT INTO public.equipaje (tipo, dimension_alto, dimension_ancho, dimension_largo, peso_max, precio, descripcion) VALUES
('Mochila', 40.00, 30.00, 15.00, 8.00, 15.00, 'Mochila de mano para viajes cortos'),
('Maleta Pequeña', 55.00, 40.00, 20.00, 10.00, 25.00, 'Maleta cabina estándar'),
('Maleta Mediana', 65.00, 45.00, 25.00, 20.00, 35.00, 'Maleta para estancia media'),
('Maleta Grande', 75.00, 50.00, 30.00, 30.00, 50.00, 'Maleta grande para viajes largos'),
('Equipaje Especial', 100.00, 60.00, 40.00, 50.00, 80.00, 'Para equipo deportivo o instrumentos'),
('Mochila Pequeña', 35.00, 25.00, 12.00, 5.00, 12.00, 'Mochila pequeña para documentos'),
('Maleta Ejecutiva', 50.00, 35.00, 18.00, 12.00, 40.00, 'Maleta para viajes de negocios'),
('Bolso de Viaje', 45.00, 30.00, 20.00, 15.00, 28.00, 'Bolso versátil para viajes cortos'),
('Mochila Técnica', 55.00, 32.00, 22.00, 18.00, 45.00, 'Mochila para equipo técnico'),
('Maleta Rígida', 70.00, 48.00, 28.00, 25.00, 65.00, 'Maleta con carcasa rígida'),
('Equipaje de Bebé', 60.00, 40.00, 25.00, 20.00, 35.00, 'Equipaje especial para viajar con bebés'),
('Porta Instrumentos', 120.00, 45.00, 35.00, 40.00, 95.00, 'Para instrumentos musicales'),
('Bolso de Playa', 40.00, 35.00, 15.00, 8.00, 18.00, 'Bolso ideal para día de playa'),
('Mochila de Senderismo', 60.00, 35.00, 25.00, 22.00, 55.00, 'Mochila especial para trekking'),
('Maleta con Ruedas', 68.00, 46.00, 26.00, 28.00, 58.00, 'Maleta práctica con ruedas');

-- Insertar 15 paquetes
INSERT INTO public.paquete (nombre, descripcion, precio_total, fecha_paquete) VALUES
('Paquete Playa', 'Incluye hotel + vuelos + traslados a playa paradisíaca', 1200.00, '2025-06-01 00:00:00'),
('Aventura Montaña', 'Tour de senderismo, alojamiento en cabañas y equipo incluido', 850.00, '2025-07-15 00:00:00'),
('Ciudad Cultural', 'Visita a museos, city tour y hotel céntrico', 650.00, '2025-08-20 00:00:00'),
('Luna de Miel', 'Suite premium, cena romántica y experiencias exclusivas', 1800.00, '2025-09-10 00:00:00'),
('Paquete Aventura Extrema', 'Rappel, rafting y canopy en la selva', 950.00, '2025-06-10 00:00:00'),
('Tour Gastronómico', 'Recorrido por los mejores restaurantes', 780.00, '2025-07-05 00:00:00'),
('Escape Romántico', 'Fin de semana en suite con jacuzzi', 620.00, '2025-08-12 00:00:00'),
('Viaje Familiar', 'Actividades para toda la familia', 1100.00, '2025-09-08 00:00:00'),
('Retiro Espiritual', 'Yoga, meditación y bienestar', 540.00, '2025-10-15 00:00:00'),
('Tour Fotográfico', 'Lugares ideales para fotografía', 890.00, '2025-11-20 00:00:00'),
('Aventura Acuática', 'Buceo, snorkel y paseos en barco', 1250.00, '2025-12-05 00:00:00'),
('Cultural Heritage', 'Visita a sitios patrimonio mundial', 720.00, '2025-06-25 00:00:00'),
('Winter Sports', 'Esquí y snowboard en la montaña', 1500.00, '2025-12-15 00:00:00'),
('City Break', 'Escapada de 3 días a ciudad europea', 480.00, '2025-10-08 00:00:00'),
('Business Trip', 'Paquete ejecutivo con servicios premium', 1350.00, '2025-11-10 00:00:00');

-- Insertar 15 ofertas
INSERT INTO public.oferta (nombre, descripcion, precio, etiquetas, url, fuente, fecha_extraccion) VALUES
('Oferta Verano 2025', 'Descuento especial en destinos de playa', 899.00, 'playa,verano,descuento', 'https://ejemplo.com/oferta-verano', 'Sistema', '2025-05-20 09:00:00'),
('Early Booking', 'Reserva anticipada con 30% descuento', 700.00, 'early,booking,promo', 'https://ejemplo.com/early-booking', 'Sistema', '2025-04-15 10:30:00'),
('Black Friday', 'Ofertas exclusivas Black Friday 2025', 550.00, 'blackfriday,ofertas,especial', 'https://ejemplo.com/black-friday', 'Sistema', '2025-11-20 08:00:00'),
('Last Minute', 'Ofertas de última hora con 40% descuento', 450.00, 'lastminute,urgente,descuento', 'https://ejemplo.com/last-minute', 'Sistema', '2025-07-10 11:00:00'),
('Summer Sale', 'Promoción especial de verano', 680.00, 'summer,sale,hot', 'https://ejemplo.com/summer-sale', 'Sistema', '2025-06-15 14:20:00'),
('Winter Getaway', 'Escapadas de invierno con descuento', 520.00, 'winter,getaway,snow', 'https://ejemplo.com/winter-getaway', 'Sistema', '2025-11-25 09:45:00'),
('Family Package', 'Descuento especial para familias', 890.00, 'family,package,kids', 'https://ejemplo.com/family-package', 'Sistema', '2025-05-30 16:30:00'),
('Honeymoon Special', 'Oferta exclusiva para lunas de miel', 1550.00, 'honeymoon,romantic,luxury', 'https://ejemplo.com/honeymoon-special', 'Sistema', '2025-08-18 10:15:00'),
('Adventure Week', 'Semana de aventura con actividades incluidas', 720.00, 'adventure,week,active', 'https://ejemplo.com/adventure-week', 'Sistema', '2025-09-22 13:40:00'),
('Cultural Pass', 'Acceso a múltiples museos y sitios', 380.00, 'cultural,pass,museums', 'https://ejemplo.com/cultural-pass', 'Sistema', '2025-10-05 15:25:00'),
('Business Class', 'Oferta para viajeros frecuentes', 1100.00, 'business,class,executive', 'https://ejemplo.com/business-class', 'Sistema', '2025-04-12 12:10:00'),
('Student Discount', '50% descuento para estudiantes', 290.00, 'student,discount,youth', 'https://ejemplo.com/student-discount', 'Sistema', '2025-03-18 08:50:00'),
('Senior Special', 'Oferta para adultos mayores', 420.00, 'senior,special,mature', 'https://ejemplo.com/senior-special', 'Sistema', '2025-02-14 17:35:00'),
('Weekend Escape', 'Escapada de fin de semana económico', 320.00, 'weekend,escape,economy', 'https://ejemplo.com/weekend-escape', 'Sistema', '2025-07-25 14:15:00'),
('Luxury Experience', 'Experiencias de lujo con descuento', 1850.00, 'luxury,experience,vip', 'https://ejemplo.com/luxury-experience', 'Sistema', '2025-10-30 11:50:00');

-- Insertar 15 servicios
INSERT INTO public.servicio (nombre, descripcion, tipo_servicio, precio_base, disponibilidad, id_proveedor) VALUES
('Habitación Doble', 'Habitación doble con desayuno incluido', 'hospedaje', 120.00, 15, 1),
('Vuelo Nacional', 'Vuelo económico clase turista', 'transporte', 200.00, 50, 2),
('Tour Ciudad', 'Recorrido guiado por la ciudad', 'turismo', 45.00, 25, 3),
('Alquiler Auto', 'Auto económico por día', 'transporte', 35.00, 12, 4),
('Cena Gourmet', 'Cena de 3 tiempos en restaurante premium', 'alimentacion', 80.00, 30, 5),
('Traslado Aeropuerto', 'Servicio de traslado al aeropuerto', 'transporte', 25.00, 40, 4),
('Seguro de Viaje', 'Seguro médico y de equipaje', 'seguros', 50.00, 100, NULL),
('Suite Presidencial', 'Suite de lujo con vista al mar', 'hospedaje', 350.00, 5, 10),
('Vuelo Primera Clase', 'Vuelo internacional primera clase', 'transporte', 1200.00, 10, 2),
('Tour Privado', 'Tour exclusivo con guía personal', 'turismo', 150.00, 8, 9),
('Limusina', 'Servicio de limusina por hora', 'transporte', 80.00, 6, 11),
('Degustación Vinos', 'Tour y degustación en viñedos', 'alimentacion', 95.00, 15, 13),
('Spa Relajante', 'Masajes y tratamientos spa', 'bienestar', 120.00, 12, 1),
('Curso de Cocina', 'Clase de cocina con chef profesional', 'alimentacion', 75.00, 20, 13),
('Alquiler Bicicletas', 'Bicicletas para recorrido urbano', 'transporte', 15.00, 30, 8);

-- Insertar 15 reservas
INSERT INTO public.reserva (id_usuario, estado, fecha_reserva, fecha_inicio, fecha_fin, total, observaciones) VALUES
(1, 'confirmada', '2025-06-10 14:20:00', '2025-07-15 00:00:00', '2025-07-20 00:00:00', 850.00, 'Viaje familiar a la playa'),
(2, 'pendiente', '2025-07-05 09:15:00', '2025-08-10 00:00:00', '2025-08-15 00:00:00', 1200.00, 'Viaje de negocios'),
(3, 'confirmada', '2025-08-20 16:45:00', '2025-09-25 00:00:00', '2025-09-30 00:00:00', 650.00, 'Tour cultural'),
(4, 'cancelada', '2025-05-15 11:30:00', '2025-06-20 00:00:00', '2025-06-25 00:00:00', 450.00, 'Cancelado por emergencia'),
(5, 'confirmada', '2025-09-01 13:10:00', '2025-10-05 00:00:00', '2025-10-12 00:00:00', 1800.00, 'Luna de miel'),
(6, 'pendiente', '2025-10-08 10:25:00', '2025-11-15 00:00:00', '2025-11-20 00:00:00', 720.00, 'Segunda reserva del usuario'),
(7, 'confirmada', '2025-07-12 08:30:00', '2025-08-18 00:00:00', '2025-08-22 00:00:00', 950.00, 'Viaje de fin de semana largo'),
(8, 'pendiente', '2025-08-03 14:45:00', '2025-09-10 00:00:00', '2025-09-17 00:00:00', 1560.00, 'Viaje de aniversario'),
(9, 'confirmada', '2025-09-15 11:20:00', '2025-10-20 00:00:00', '2025-10-25 00:00:00', 890.00, 'Tour fotográfico'),
(10, 'cancelada', '2025-10-08 16:10:00', '2025-11-12 00:00:00', '2025-11-15 00:00:00', 450.00, 'Cambio de planes'),
(11, 'confirmada', '2025-11-05 09:35:00', '2025-12-10 00:00:00', '2025-12-20 00:00:00', 2100.00, 'Viaje largo de invierno'),
(12, 'pendiente', '2025-12-01 13:50:00', '2026-01-05 00:00:00', '2026-01-12 00:00:00', 1350.00, 'Viaje de año nuevo'),
(13, 'confirmada', '2025-06-25 10:15:00', '2025-07-30 00:00:00', '2025-08-05 00:00:00', 980.00, 'Vacaciones de verano'),
(14, 'confirmada', '2025-07-30 15:25:00', '2025-09-02 00:00:00', '2025-09-09 00:00:00', 1120.00, 'Viaje familiar'),
(15, 'pendiente', '2025-08-28 12:40:00', '2025-10-03 00:00:00', '2025-10-08 00:00:00', 670.00, 'Escapada de otoño');

-- Insertar detalles de reserva
INSERT INTO public.detalle_reserva (id_reserva, id_servicio, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 5, 120.00, 600.00), (1, 2, 2, 200.00, 400.00), (1, 6, 2, 25.00, 50.00),
(2, 1, 7, 120.00, 840.00), (2, 3, 1, 45.00, 45.00), (2, 7, 2, 50.00, 100.00),
(3, 1, 4, 120.00, 480.00), (3, 3, 2, 45.00, 90.00), (3, 6, 2, 25.00, 50.00),
(4, 2, 2, 200.00, 400.00), (4, 6, 2, 25.00, 50.00),
(5, 8, 7, 350.00, 2450.00), (5, 9, 2, 1200.00, 2400.00), (5, 11, 2, 80.00, 160.00),
(6, 1, 5, 120.00, 600.00), (6, 4, 3, 35.00, 105.00), (6, 7, 1, 50.00, 50.00),
(7, 1, 4, 120.00, 480.00), (7, 10, 1, 150.00, 150.00), (7, 12, 2, 95.00, 190.00),
(8, 8, 7, 350.00, 2450.00), (8, 9, 1, 1200.00, 1200.00), (8, 13, 2, 120.00, 240.00),
(9, 1, 5, 120.00, 600.00), (9, 12, 2, 95.00, 190.00), (9, 14, 1, 75.00, 75.00),
(10, 2, 2, 200.00, 400.00), (10, 6, 2, 25.00, 50.00),
(11, 8, 10, 350.00, 3500.00), (11, 3, 2, 45.00, 90.00), (11, 14, 2, 75.00, 150.00),
(12, 1, 7, 120.00, 840.00), (12, 4, 1, 35.00, 35.00), (12, 15, 2, 15.00, 30.00),
(13, 1, 6, 120.00, 720.00), (13, 6, 2, 25.00, 50.00), (13, 7, 2, 50.00, 100.00),
(14, 1, 7, 120.00, 840.00), (14, 3, 3, 45.00, 135.00), (14, 15, 1, 15.00, 15.00),
(15, 1, 4, 120.00, 480.00), (15, 3, 2, 45.00, 90.00), (15, 11, 1, 80.00, 80.00);

-- Insertar reserva_equipaje
INSERT INTO public.reserva_equipaje (id_reserva, id_equipaje, cantidad, precio_unitario, subtotal) VALUES
(1, 2, 2, 25.00, 50.00), (1, 3, 1, 35.00, 35.00),
(2, 3, 2, 35.00, 70.00), (2, 4, 1, 50.00, 50.00),
(3, 1, 1, 15.00, 15.00), (3, 2, 1, 25.00, 25.00),
(5, 4, 2, 50.00, 100.00), (5, 7, 1, 40.00, 40.00),
(6, 2, 1, 25.00, 25.00), (6, 6, 1, 12.00, 12.00),
(7, 8, 2, 28.00, 56.00), (7, 10, 1, 65.00, 65.00),
(8, 9, 2, 45.00, 90.00), (8, 11, 1, 65.00, 65.00),
(9, 7, 1, 95.00, 95.00), (9, 13, 1, 18.00, 18.00),
(11, 4, 3, 50.00, 150.00), (11, 12, 1, 55.00, 55.00),
(13, 5, 1, 80.00, 80.00), (13, 14, 2, 18.00, 36.00),
(14, 6, 1, 40.00, 40.00), (14, 15, 1, 58.00, 58.00),
(15, 2, 2, 25.00, 50.00), (15, 8, 1, 28.00, 28.00);

-- Insertar paquete_reserva
INSERT INTO public.paquete_reserva (id_paquete, id_reserva, cantidad, precio_unitario, subtotal) VALUES
(1, 2, 1, 1200.00, 1200.00),
(3, 3, 1, 650.00, 650.00),
(4, 5, 1, 1800.00, 1800.00),
(5, 7, 1, 950.00, 950.00),
(6, 8, 1, 780.00, 780.00),
(7, 9, 1, 620.00, 620.00),
(8, 11, 1, 1100.00, 1100.00),
(9, 13, 1, 540.00, 540.00),
(10, 14, 1, 890.00, 890.00);

-- Insertar pagos
INSERT INTO public.pago (id_reserva, monto, metodo_pago, estado_pago, fecha_pago, referencia_pago) VALUES
(1, 850.00, 'tarjeta_credito', 'completado', '2025-06-11 10:30:00', 'PAY-001234'),
(2, 600.00, 'paypal', 'pendiente', NULL, 'PAY-001235'),
(3, 650.00, 'transferencia', 'completado', '2025-08-21 14:20:00', 'PAY-001236'),
(5, 1800.00, 'tarjeta_credito', 'completado', '2025-09-02 16:45:00', 'PAY-001237'),
(7, 950.00, 'tarjeta_debito', 'completado', '2025-07-13 11:20:00', 'PAY-001238'),
(8, 800.00, 'paypal', 'pendiente', NULL, 'PAY-001239'),
(9, 890.00, 'transferencia', 'completado', '2025-09-16 14:35:00', 'PAY-001240'),
(11, 2100.00, 'tarjeta_credito', 'completado', '2025-11-06 16:50:00', 'PAY-001241'),
(13, 980.00, 'tarjeta_credito', 'completado', '2025-06-26 09:15:00', 'PAY-001242'),
(14, 1120.00, 'paypal', 'completado', '2025-07-31 13:40:00', 'PAY-001243');

-- Insertar historial
INSERT INTO public.historial (id_usuario, termino_busqueda, fecha, resultados) VALUES
(1, 'playa cancun', '2025-06-08 11:20:00', 15),
(1, 'hotel all inclusive', '2025-06-08 11:25:00', 8),
(2, 'vuelos nueva york', '2025-07-03 09:15:00', 22),
(3, 'tour museos europeos', '2025-08-18 14:30:00', 12),
(5, 'luna de miel maldivas', '2025-08-28 16:40:00', 7),
(6, 'vuelos baratos', '2025-09-19 10:15:00', 34),
(7, 'hotel playa luxury', '2025-07-10 14:25:00', 12),
(8, 'paquetes aniversario', '2025-08-01 16:30:00', 8),
(9, 'tours fotografia paisajes', '2025-09-13 11:45:00', 15),
(10, 'ofertas last minute', '2025-10-05 09:20:00', 22),
(11, 'viajes invierno nieve', '2025-11-02 13:55:00', 18),
(12, 'año nuevo 2026', '2025-11-28 17:10:00', 25),
(13, 'vacaciones julio', '2025-06-22 08:40:00', 30),
(14, 'viaje familiar agosto', '2025-07-27 12:15:00', 14),
(15, 'escapada octubre', '2025-08-25 15:50:00', 19);

-- Asignar un proveedor válido al servicio 7 (Seguro de Viaje)
INSERT INTO public.servicio_proveedor (id_servicio, id_proveedor) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 4), (7, 15),  
(8, 10), (9, 2), (10, 9), (11, 11), (12, 13), (13, 1), (14, 13), (15, 8);

-- =============================================
-- ÍNDICES Y OPTIMIZACIONES
-- =============================================

-- Índices para mejorar el rendimiento
CREATE INDEX idx_reserva_usuario ON public.reserva(id_usuario);
CREATE INDEX idx_reserva_estado ON public.reserva(estado);
CREATE INDEX idx_reserva_fecha ON public.reserva(fecha_reserva);
CREATE INDEX idx_detalle_reserva_reserva ON public.detalle_reserva(id_reserva);
CREATE INDEX idx_detalle_reserva_servicio ON public.detalle_reserva(id_servicio);
CREATE INDEX idx_pago_reserva ON public.pago(id_reserva);
CREATE INDEX idx_pago_estado ON public.pago(estado_pago);
CREATE INDEX idx_historial_usuario ON public.historial(id_usuario);
CREATE INDEX idx_historial_fecha ON public.historial(fecha);
CREATE INDEX idx_servicio_proveedor ON public.servicio(id_proveedor);
CREATE INDEX idx_servicio_tipo ON public.servicio(tipo_servicio);
CREATE INDEX idx_servicio_activo ON public.servicio(activo) WHERE activo = true;

-- Función para actualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para actualizar timestamps
CREATE TRIGGER update_reserva_updated_at BEFORE UPDATE ON public.reserva FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_pago_updated_at BEFORE UPDATE ON public.pago FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Vista para reportes de reservas
CREATE OR REPLACE VIEW public.vw_reservas_detalladas AS
SELECT 
    r.id_reserva,
    r.estado,
    r.fecha_reserva,
    r.total,
    u.nombres || ' ' || u.apellidos as usuario,
    u.email,
    p.estado_pago,
    p.monto as monto_pago,
    COUNT(dr.id_detalle_reserva) as cantidad_servicios,
    COUNT(re.id_reserva_equipaje) as cantidad_equipajes
FROM public.reserva r
LEFT JOIN public.usuarios u ON r.id_usuario = u.id_usuario
LEFT JOIN public.pago p ON r.id_reserva = p.id_reserva
LEFT JOIN public.detalle_reserva dr ON r.id_reserva = dr.id_reserva
LEFT JOIN public.reserva_equipaje re ON r.id_reserva = re.id_reserva
GROUP BY r.id_reserva, u.nombres, u.apellidos, u.email, p.estado_pago, p.monto;

-- =============================================
-- CONSULTA DE VERIFICACIÓN
-- =============================================

SELECT 'Base de datos creada exitosamente con 15+ registros en cada tabla' as mensaje;

-- Mostrar resumen de datos insertados
SELECT 
    (SELECT COUNT(*) FROM usuarios) as total_usuarios,
    (SELECT COUNT(*) FROM proveedor) as total_proveedores,
    (SELECT COUNT(*) FROM equipaje) as total_equipajes,
    (SELECT COUNT(*) FROM paquete) as total_paquetes,
    (SELECT COUNT(*) FROM oferta) as total_ofertas,
    (SELECT COUNT(*) FROM servicio) as total_servicios,
    (SELECT COUNT(*) FROM reserva) as total_reservas,
    (SELECT COUNT(*) FROM detalle_reserva) as total_detalles,
    (SELECT COUNT(*) FROM pago) as total_pagos;

-- Para usuarios normales
UPDATE usuarios SET rol = 'USER' WHERE rol IN ('usuario', 'USUARIO');

-- Para administradores  
UPDATE usuarios SET rol = 'ADMIN' WHERE rol IN ('admin', 'ADMIN');

select *from usuarios