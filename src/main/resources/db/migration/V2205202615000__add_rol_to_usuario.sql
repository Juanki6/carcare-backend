-- Añade la columna rol a la tabla usuario.
-- Los usuarios existentes quedan como ROLE_USER por defecto.
ALTER TABLE usuario
    ADD COLUMN IF NOT EXISTS rol VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER';
