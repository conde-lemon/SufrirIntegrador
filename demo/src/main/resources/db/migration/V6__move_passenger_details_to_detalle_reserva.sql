-- Migration to move passenger details from reserva to detalle_reserva
-- V6__move_passenger_details_to_detalle_reserva.sql

-- Remove passenger fields from reserva table if they exist
ALTER TABLE reserva 
DROP COLUMN IF EXISTS nombre,
DROP COLUMN IF EXISTS apellido,
DROP COLUMN IF EXISTS documento,
DROP COLUMN IF EXISTS fecha_nacimiento,
DROP COLUMN IF EXISTS sexo,
DROP COLUMN IF EXISTS correo,
DROP COLUMN IF EXISTS telefono,
DROP COLUMN IF EXISTS asiento_seleccionado;

-- Add passenger fields to detalle_reserva table
ALTER TABLE detalle_reserva 
ADD COLUMN IF NOT EXISTS nombre VARCHAR(100),
ADD COLUMN IF NOT EXISTS apellido VARCHAR(100),
ADD COLUMN IF NOT EXISTS documento VARCHAR(50),
ADD COLUMN IF NOT EXISTS fecha_nacimiento DATE,
ADD COLUMN IF NOT EXISTS sexo VARCHAR(20),
ADD COLUMN IF NOT EXISTS correo VARCHAR(150),
ADD COLUMN IF NOT EXISTS telefono VARCHAR(20),
ADD COLUMN IF NOT EXISTS asiento_seleccionado VARCHAR(10);

-- Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_detalle_reserva_documento ON detalle_reserva(documento);
CREATE INDEX IF NOT EXISTS idx_detalle_reserva_correo ON detalle_reserva(correo);
CREATE INDEX IF NOT EXISTS idx_detalle_reserva_asiento ON detalle_reserva(asiento_seleccionado);