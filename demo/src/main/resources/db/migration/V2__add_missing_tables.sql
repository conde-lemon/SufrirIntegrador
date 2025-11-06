-- =============================================
-- MIGRACIÓN SEGURA - NO ELIMINA DATOS EXISTENTES
-- =============================================

-- Crear tablas faltantes solo si no existen
CREATE TABLE IF NOT EXISTS public.equipaje (
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

CREATE TABLE IF NOT EXISTS public.paquete (
    id_paquete SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio_total NUMERIC(10,2) NOT NULL,
    fecha_paquete TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT true,
    CONSTRAINT chk_precio_total_positivo CHECK (precio_total >= 0)
);

CREATE TABLE IF NOT EXISTS public.reserva_equipaje (
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

CREATE TABLE IF NOT EXISTS public.paquete_reserva (
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

CREATE TABLE IF NOT EXISTS public.pago (
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

CREATE TABLE IF NOT EXISTS public.historial (
    id_historial SERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    termino_busqueda VARCHAR(500) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resultados INTEGER DEFAULT 0,
    CONSTRAINT fk_historial_usuario FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario) ON DELETE CASCADE
);

-- Agregar columnas faltantes a tablas existentes
DO $$ 
BEGIN
    -- Agregar columnas a servicio si no existen
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='servicio' AND column_name='origen') THEN
        ALTER TABLE public.servicio ADD COLUMN origen VARCHAR(100);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='servicio' AND column_name='destino') THEN
        ALTER TABLE public.servicio ADD COLUMN destino VARCHAR(100);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='servicio' AND column_name='tags') THEN
        ALTER TABLE public.servicio ADD COLUMN tags VARCHAR(500);
    END IF;

    -- Agregar columnas a oferta si no existen
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='oferta' AND column_name='descuento') THEN
        ALTER TABLE public.oferta ADD COLUMN descuento NUMERIC(5,2) DEFAULT 0;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='oferta' AND column_name='fecha_inicio') THEN
        ALTER TABLE public.oferta ADD COLUMN fecha_inicio TIMESTAMP;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='oferta' AND column_name='fecha_fin') THEN
        ALTER TABLE public.oferta ADD COLUMN fecha_fin TIMESTAMP;
    END IF;
END $$;

-- Insertar datos de ejemplo solo si las tablas están vacías
INSERT INTO public.equipaje (tipo, dimension_alto, dimension_ancho, dimension_largo, peso_max, precio, descripcion)
SELECT * FROM (VALUES
    ('Mochila', 40.00, 30.00, 15.00, 8.00, 15.00, 'Mochila de mano para viajes cortos'),
    ('Maleta Pequeña', 55.00, 40.00, 20.00, 10.00, 25.00, 'Maleta cabina estándar'),
    ('Maleta Mediana', 65.00, 45.00, 25.00, 20.00, 35.00, 'Maleta para estancia media'),
    ('Maleta Grande', 75.00, 50.00, 30.00, 30.00, 50.00, 'Maleta grande para viajes largos'),
    ('Equipaje Especial', 100.00, 60.00, 40.00, 50.00, 80.00, 'Para equipo deportivo o instrumentos')
) AS v(tipo, dimension_alto, dimension_ancho, dimension_largo, peso_max, precio, descripcion)
WHERE NOT EXISTS (SELECT 1 FROM public.equipaje LIMIT 1);

INSERT INTO public.paquete (nombre, descripcion, precio_total, fecha_paquete)
SELECT * FROM (VALUES
    ('Paquete Playa', 'Incluye hotel + vuelos + traslados a playa paradisíaca', 1200.00, '2025-06-01 00:00:00'::timestamp),
    ('Aventura Montaña', 'Tour de senderismo, alojamiento en cabañas y equipo incluido', 850.00, '2025-07-15 00:00:00'::timestamp),
    ('Ciudad Cultural', 'Visita a museos, city tour y hotel céntrico', 650.00, '2025-08-20 00:00:00'::timestamp),
    ('Luna de Miel', 'Suite premium, cena romántica y experiencias exclusivas', 1800.00, '2025-09-10 00:00:00'::timestamp),
    ('Paquete Aventura Extrema', 'Rappel, rafting y canopy en la selva', 950.00, '2025-06-10 00:00:00'::timestamp)
) AS v(nombre, descripcion, precio_total, fecha_paquete)
WHERE NOT EXISTS (SELECT 1 FROM public.paquete LIMIT 1);

-- Crear índices si no existen
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_reserva_usuario') THEN
        CREATE INDEX idx_reserva_usuario ON public.reserva(id_usuario);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_reserva_estado') THEN
        CREATE INDEX idx_reserva_estado ON public.reserva(estado);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_servicio_tipo') THEN
        CREATE INDEX idx_servicio_tipo ON public.servicio(tipo_servicio);
    END IF;
END $$;

-- Normalizar roles de usuario
UPDATE usuarios SET rol = 'USER' WHERE rol IN ('usuario', 'USUARIO', 'cliente', 'CLIENTE');
UPDATE usuarios SET rol = 'ADMIN' WHERE rol IN ('admin', 'ADMIN');

-- Mensaje de confirmación
SELECT 'Migración completada exitosamente - Datos existentes preservados' as mensaje;