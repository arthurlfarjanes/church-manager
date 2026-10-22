-- =============================================================
-- V2 - Modelagem Relacional do Church Manager
-- Módulos: Paróquia/Comunidade, Pessoas, Inventário, Eventos,
--          Horários de Missas/Intenções, Pastorais e Atividades.
-- =============================================================

-- 1. PARÓQUIA & COMUNIDADES (CAPELAS)
CREATE TABLE IF NOT EXISTS paroquia (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    diocese VARCHAR(150),
    paroco VARCHAR(150),
    telefone VARCHAR(30),
    email VARCHAR(100),
    endereco VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS comunidade (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    paroquia_id UUID NOT NULL REFERENCES paroquia(id) ON DELETE CASCADE,
    nome VARCHAR(150) NOT NULL,
    endereco VARCHAR(255),
    is_matriz BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_comunidade_paroquia ON comunidade(paroquia_id);

-- 2. CADASTRO DE PESSOAS (PAROQUIANOS / VOLUNTÁRIOS)
CREATE TABLE IF NOT EXISTS pessoa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(30),
    data_nascimento DATE,
    endereco VARCHAR(255),
    observacoes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_pessoa_nome ON pessoa(nome);
CREATE INDEX IF NOT EXISTS idx_pessoa_email ON pessoa(email);

-- 3. INVENTÁRIO (CATEGORIAS, ITENS E MOVIMENTAÇÕES)
CREATE TABLE IF NOT EXISTS categoria_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS item_patrimonio (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_tombamento VARCHAR(50) UNIQUE,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    categoria_id UUID NOT NULL REFERENCES categoria_item(id) ON DELETE RESTRICT,
    comunidade_id UUID REFERENCES comunidade(id) ON DELETE SET NULL,
    localizacao VARCHAR(100),
    quantidade INT NOT NULL DEFAULT 1,
    valor_estimado NUMERIC(12, 2),
    estado_conservacao VARCHAR(30) NOT NULL DEFAULT 'BOM',
    status VARCHAR(30) NOT NULL DEFAULT 'DISPONIVEL',
    data_aquisicao DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_item_categoria ON item_patrimonio(categoria_id);
CREATE INDEX IF NOT EXISTS idx_item_comunidade ON item_patrimonio(comunidade_id);
CREATE INDEX IF NOT EXISTS idx_item_status ON item_patrimonio(status);

CREATE TABLE IF NOT EXISTS movimentacao_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id UUID NOT NULL REFERENCES item_patrimonio(id) ON DELETE CASCADE,
    tipo VARCHAR(30) NOT NULL,
    data_movimentacao TIMESTAMPTZ NOT NULL DEFAULT now(),
    origem VARCHAR(150),
    destino VARCHAR(150),
    responsavel_id UUID REFERENCES pessoa(id) ON DELETE SET NULL,
    observacoes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_mov_item ON movimentacao_item(item_id);

-- 4. EVENTOS
CREATE TABLE IF NOT EXISTS evento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    comunidade_id UUID REFERENCES comunidade(id) ON DELETE SET NULL,
    local_espaco VARCHAR(150),
    data_inicio TIMESTAMPTZ NOT NULL,
    data_fim TIMESTAMPTZ,
    tipo VARCHAR(50) DEFAULT 'CELEBRACAO',
    status VARCHAR(30) NOT NULL DEFAULT 'AGENDADO',
    responsavel_id UUID REFERENCES pessoa(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_evento_data_inicio ON evento(data_inicio);
CREATE INDEX IF NOT EXISTS idx_evento_status ON evento(status);

-- 5. HORÁRIOS DE MISSAS E INTENÇÕES
CREATE TABLE IF NOT EXISTS horario_missa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    comunidade_id UUID NOT NULL REFERENCES comunidade(id) ON DELETE CASCADE,
    dia_semana VARCHAR(20) NOT NULL,
    horario TIME NOT NULL,
    celebrante VARCHAR(100),
    descricao VARCHAR(150),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_horario_comunidade ON horario_missa(comunidade_id);
CREATE INDEX IF NOT EXISTS idx_horario_dia ON horario_missa(dia_semana);

CREATE TABLE IF NOT EXISTS intencao_missa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    horario_missa_id UUID NOT NULL REFERENCES horario_missa(id) ON DELETE CASCADE,
    data_celebracao DATE NOT NULL,
    tipo VARCHAR(50) NOT NULL DEFAULT 'FALECIMENTO',
    para_quem VARCHAR(150) NOT NULL,
    solicitante VARCHAR(150),
    valor_estipendio NUMERIC(10, 2),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_intencao_horario ON intencao_missa(horario_missa_id);
CREATE INDEX IF NOT EXISTS idx_intencao_data ON intencao_missa(data_celebracao);

-- 6. PASTORAIS, MEMBROS E ATIVIDADES
CREATE TABLE IF NOT EXISTS pastoral (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    coordenador_id UUID REFERENCES pessoa(id) ON DELETE SET NULL,
    vice_coordenador_id UUID REFERENCES pessoa(id) ON DELETE SET NULL,
    comunidade_id UUID REFERENCES comunidade(id) ON DELETE SET NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_pastoral_comunidade ON pastoral(comunidade_id);

CREATE TABLE IF NOT EXISTS pastoral_membro (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pastoral_id UUID NOT NULL REFERENCES pastoral(id) ON DELETE CASCADE,
    pessoa_id UUID NOT NULL REFERENCES pessoa(id) ON DELETE CASCADE,
    funcao VARCHAR(50) DEFAULT 'AGENTE',
    data_ingresso DATE DEFAULT CURRENT_DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_pastoral_membro UNIQUE (pastoral_id, pessoa_id)
);
CREATE INDEX IF NOT EXISTS idx_membro_pastoral ON pastoral_membro(pastoral_id);
CREATE INDEX IF NOT EXISTS idx_membro_pessoa ON pastoral_membro(pessoa_id);

CREATE TABLE IF NOT EXISTS atividade_pastoral (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pastoral_id UUID NOT NULL REFERENCES pastoral(id) ON DELETE CASCADE,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    data_hora TIMESTAMPTZ NOT NULL,
    local VARCHAR(150),
    status VARCHAR(30) NOT NULL DEFAULT 'AGENDADA',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_atividade_pastoral ON atividade_pastoral(pastoral_id);
CREATE INDEX IF NOT EXISTS idx_atividade_data ON atividade_pastoral(data_hora);
