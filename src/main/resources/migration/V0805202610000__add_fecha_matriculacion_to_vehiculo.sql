-- Añadir columna fecha_matriculacion (Hibernate la crea via ddl-auto,
-- pero este script migra los datos de anio y elimina la columna antigua)

ALTER TABLE vehiculo ADD COLUMN IF NOT EXISTS fecha_matriculacion DATE NULL;

UPDATE vehiculo
SET fecha_matriculacion = CONCAT(anio, '-01-01')
WHERE anio IS NOT NULL AND fecha_matriculacion IS NULL;

ALTER TABLE vehiculo DROP COLUMN IF EXISTS anio;
