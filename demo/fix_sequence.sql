-- Script para arreglar el problema de secuencia en la tabla usuarios
-- Ejecutar este script en PostgreSQL para sincronizar la secuencia con los datos existentes

-- 1. Verificar el valor máximo actual en la tabla usuarios
SELECT MAX(id_usuario) FROM usuarios;

-- 2. Obtener el valor actual de la secuencia
SELECT currval('usuarios_id_usuario_seq');

-- 3. Actualizar la secuencia al siguiente valor disponible
-- Reemplaza 'X' con el valor máximo encontrado en el paso 1 + 1
SELECT setval('usuarios_id_usuario_seq', (SELECT COALESCE(MAX(id_usuario), 0) + 1 FROM usuarios), false);

-- 4. Verificar que la secuencia se actualizó correctamente
SELECT currval('usuarios_id_usuario_seq');

-- Comando alternativo si el nombre de la secuencia es diferente:
-- SELECT setval(pg_get_serial_sequence('usuarios', 'id_usuario'), (SELECT COALESCE(MAX(id_usuario), 0) + 1 FROM usuarios), false);