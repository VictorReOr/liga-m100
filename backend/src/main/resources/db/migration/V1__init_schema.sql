-- V1__init_schema.sql

-- TABLE: PROVINCIAS
CREATE TABLE provincias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- TABLE: CPAS (Clubes / Centros)
CREATE TABLE cpas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    provincia_id INTEGER NOT NULL REFERENCES provincias(id),
    CONSTRAINT uk_cpa_nombre_provincia UNIQUE (nombre, provincia_id)
);

-- TABLE: USERS (Usuarios con Roles)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- SUPERADMIN, CPA_RESPONSIBLE, JUDGE, PUBLIC
    validated BOOLEAN DEFAULT TRUE,
    cpa_id INTEGER REFERENCES cpas(id), -- Solo para responsables de CPA
    nombre_completo VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABLE: DEPORTISTAS
CREATE TABLE deportistas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    dni VARCHAR(20) UNIQUE, -- Puede ser nulo para menores sin DNI, pero si existe unico
    anio_nacimiento INTEGER NOT NULL,
    genero VARCHAR(20) NOT NULL, -- MASCULINO, FEMENINO
    cpa_id INTEGER NOT NULL REFERENCES cpas(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABLE: EVENTOS
-- Representa Clasi 1, Clasi 2, Final, etc.
CREATE TABLE eventos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- CLASIFICATORIO, FINAL
    fecha DATE NOT NULL,
    max_pruebas_por_deportista INTEGER DEFAULT 3,
    provincias_participantes_json JSONB, -- Array de IDs de provincias participantes
    activo BOOLEAN DEFAULT TRUE
);

-- TABLE: DISCIPLINAS
CREATE TABLE disciplinas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    horario_inicio TIMESTAMP, -- Fecha y hora completa para facilitar ordenamiento
    horario_fin TIMESTAMP,
    requiere_juez BOOLEAN DEFAULT TRUE,
    tipo_marca VARCHAR(50) NOT NULL, -- TIEMPO, DISTANCIA, PUNTUACION
    reglas_config_json JSONB, -- JSON con reglas de puntuación (ej: { "type": "POSITION", "points": [10,8,6...] })
    evento_id INTEGER REFERENCES eventos(id) -- Opcional, si las disciplinas son específicas de evento o globales (asumimos globales vinculadas por horario, o específicas. Haremos específicas por simplicidad o relación N:M. Por ahora vinculamos a evento para horarios específicos)
);

-- TABLE: INSCRIPCIONES
CREATE TABLE inscripciones (
    id SERIAL PRIMARY KEY,
    evento_id INTEGER NOT NULL REFERENCES eventos(id),
    deportista_id INTEGER NOT NULL REFERENCES deportistas(id),
    disciplina_id INTEGER NOT NULL REFERENCES disciplinas(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_inscripcion_evento_deportista_disciplina UNIQUE (evento_id, deportista_id, disciplina_id)
);

-- TABLE: PUNTUACIONES / RESULTADOS
CREATE TABLE puntuaciones (
    id SERIAL PRIMARY KEY,
    inscripcion_id INTEGER NOT NULL REFERENCES inscripciones(id),
    juez_id INTEGER REFERENCES users(id), -- Juez que puntuó
    marca DECIMAL(10,3), -- Valor numérico de la marca (segundos, metros, puntos)
    marca_texto VARCHAR(50), -- Representación textual si aplica
    intentos JSONB, -- Para guardar múltiples intentos si la disciplina lo requiere
    es_valida BOOLEAN DEFAULT TRUE, -- False = Descalificado / Nulo
    notas TEXT, -- Observaciones del juez
    calculo_puntos INTEGER, -- Puntos calculados por el sistema según reglas
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- INDICES PARA OPTIMIZACION
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_deportistas_cpa ON deportistas(cpa_id);
CREATE INDEX idx_inscripciones_evento ON inscripciones(evento_id);
CREATE INDEX idx_inscripciones_deportista ON inscripciones(deportista_id);
CREATE INDEX idx_puntuaciones_inscripcion ON puntuaciones(inscripcion_id);
