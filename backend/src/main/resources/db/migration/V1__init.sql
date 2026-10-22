-- =============================================================
-- V1 - Migração inicial
-- Habilita extensões utilizadas pelo projeto.
-- As tabelas de cada módulo serão criadas em migrações seguintes
-- (V2__..., V3__...), nunca editando migrações já aplicadas.
-- =============================================================

-- Geração de UUIDs (gen_random_uuid)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
