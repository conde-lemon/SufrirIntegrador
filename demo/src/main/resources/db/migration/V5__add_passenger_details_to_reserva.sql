-- Migration to add passenger details to reserva table
-- V5__add_passenger_details_to_reserva.sql

ALTER TABLE reserva 
ADD COLUMN IF NOT EXISTS nombre VARCHAR(100),
ADD COLUMN IF NOT EXISTS apellido VARCHAR(100),
ADD COLUMN IF NOT EXISTS documento VARCHAR(50),
ADD COLUMN IF NOT EXISTS fecha_nacimiento DATE,
ADD COLUMN IF NOT EXISTS sexo VARCHAR(20),
ADD COLUMN IF NOT EXISTS correo VARCHAR(150),
ADD COLUMN IF NOT EXISTS telefono VARCHAR(20),
ADD COLUMN IF NOT EXISTS asiento_seleccionado VARCHAR(10);

-- Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_reserva_documento ON reserva(documento);
CREATE INDEX IF NOT EXISTS idx_reserva_correo ON reserva(correo);
CREATE INDEX IF NOT EXISTS idx_reserva_asiento ON reserva(asiento_seleccionado);