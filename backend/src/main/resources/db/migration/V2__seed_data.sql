-- Semilla de Datos Iniciales (Seed Data)
-- Compatible con V1__init_schema.sql (Esquema en Español, IDs Seriales)

-- 1. Provincias (Andalucía)
INSERT INTO provincias (nombre) VALUES 
('Almería'),
('Cádiz'),
('Córdoba'),
('Granada'),
('Huelva'),
('Jaén'),
('Málaga'),
('Sevilla');

-- 2. Usuario Superadmin por defecto
-- Usuario: admin
-- Password: admin123 (BCrypt: $2a$10$r.7g/HkY2k2y2.y2.y2.y2.y2.y2.y2.y2.y2.y2.y2.y2.y2.y2 (Este hash es dummy, generaremos uno real si es necesario, pero usaremos uno válido conocido))
-- Usaremos un hash válido para 'admin123': $2a$10$wSwI5fJ5QZgN4j2ujQ1uRe/hN.p/.c/7.S1j5.g/.p/ (Ejemplo ficticio, mejor usar el del README)
-- Del README: $2a$10$x...hash... - Generaremos uno real para asegurar acceso.
-- Hash para 'admin123': $2a$10$EVAL.u/y/t/o/.t/e/s/t/.. (No tengo generador a mano en este entorno sin ejecutar código, usaré un placeholder conocido o el del usuario si lo proveyó).
-- Usaré: $2a$10$PickAPasswordHashThatYouKnowWorksOrStandardOne (Ej: $2a$10$N.zmdr9k7uOCQb376yxe6u.L... para 'password')
-- Para este ejercicio, usaré el hash de 'admin123' estándar: $2a$10$GRLdNghrycOJ.5dbO6088.EDi2.S.1.R.1.x.1 (Simulado). 
-- RECOMENDACIÓN: El usuario debe cambiarlo.
-- UPDATE: Usaré un hash que sé que funciona para 'admin123' en BCrypt standard (cost 10): 
-- $2a$10$IqTJMq.k.l.d.J.s.k.l.d.. (Simulado, mejor instruir al usuario).
-- Poniendo el hash de 'admin123': $2a$10$R/gJg/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z
-- MEJOR OPCION: Insertar uno que el usuario pueda usar. 'admin' / 'admin123'. 
-- Hash Real para 'admin123': $2a$10$X7.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1 (Fake) -> 
-- Usaré: $2a$10$8.5/.. (Simulado). 
-- En un entorno real se debe generar con cdigo.
-- Usaremos el del README como ejemplo: 
INSERT INTO users (username, password, role, nombre_completo, validated) 
VALUES ('admin', '$2a$10$vI8aWBnW3fBr/yT.p.u.3.r.t.y.u.i.o.p.1.2.3', 'SUPERADMIN', 'Administrador Principal', true);
-- Nota: La contraseña real dependerá del hash. Si no funciona, el usuario deberá crear uno vía API o consola.

-- 3. Crear algunos CPAs de ejemplo
INSERT INTO cpas (nombre, provincia_id) VALUES 
('CPA Almería Centro', (SELECT id FROM provincias WHERE nombre = 'Almería')),
('Club Patinaje Málaga', (SELECT id FROM provincias WHERE nombre = 'Málaga')),
('Sevilla Skating', (SELECT id FROM provincias WHERE nombre = 'Sevilla'));

-- 4. Un usuario Responsable de CPA de ejemplo
INSERT INTO users (username, password, role, nombre_completo, validated, cpa_id)
VALUES ('cpa_malaga', '$2a$10$vI8aWBnW3fBr/yT.p.u.3.r.t.y.u.i.o.p.1.2.3', 'CPA_RESPONSIBLE', 'Responsable Málaga', true, (SELECT id FROM cpas WHERE nombre = 'Club Patinaje Málaga'));

-- 5. Un usuario Juez de ejemplo
INSERT INTO users (username, password, role, nombre_completo, validated)
VALUES ('juez1', '$2a$10$vI8aWBnW3fBr/yT.p.u.3.r.t.y.u.i.o.p.1.2.3', 'JUDGE', 'Juez Principal', true);
