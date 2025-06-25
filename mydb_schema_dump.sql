-- スキーマ作成
CREATE SCHEMA IF NOT EXISTS evaluation;

-- employees テーブル
CREATE TABLE evaluation.employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT false,
    permission VARCHAR(255),
    failed_count INTEGER DEFAULT 0,
    is_locked BOOLEAN DEFAULT false,
    locked_at TIMESTAMP,
    role VARCHAR(255)
);

-- evaluations テーブル
CREATE TABLE evaluation.evaluations (
    id SERIAL PRIMARY KEY,
    evaluator_id INTEGER NOT NULL,
    target_id INTEGER NOT NULL,
    skill_score NUMERIC(3, 1),
    business_score NUMERIC(3, 1),
    team_score NUMERIC(3, 1),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    phase_id INTEGER NOT NULL,
    CONSTRAINT evaluations_skill_score_check CHECK (skill_score BETWEEN 1.0 AND 5.0),
    CONSTRAINT evaluations_business_score_check CHECK (business_score BETWEEN 1.0 AND 5.0),
    CONSTRAINT evaluations_team_score_check CHECK (team_score BETWEEN 1.0 AND 5.0)
);

-- jwt_tokens テーブル
CREATE TABLE evaluation.jwt_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    issued_at TIMESTAMP(6) NOT NULL,
    expired_at TIMESTAMP(6) NOT NULL,
    is_revoked BOOLEAN NOT NULL,
    employee_id BIGINT NOT NULL
);

-- password_reset_tokens テーブル
CREATE TABLE evaluation.password_reset_tokens (
    token VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    employee_id BIGINT NOT NULL
);

-- phases テーブル
CREATE TABLE evaluation.phases (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date VARCHAR(255),
    end_date VARCHAR(255),
    self_eval_due TIMESTAMP,
    peer_eval_due TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    phase_number INTEGER
);

-- 外部キー制約（必要なら後から追加可能）
ALTER TABLE evaluation.evaluations
  ADD CONSTRAINT fk_evaluator FOREIGN KEY (evaluator_id) REFERENCES evaluation.employees(id),
  ADD CONSTRAINT fk_target FOREIGN KEY (target_id) REFERENCES evaluation.employees(id),
  ADD CONSTRAINT fk_phase FOREIGN KEY (phase_id) REFERENCES evaluation.phases(id);

ALTER TABLE evaluation.jwt_tokens
  ADD CONSTRAINT fk_token_employee FOREIGN KEY (employee_id) REFERENCES evaluation.employees(id);

ALTER TABLE evaluation.password_reset_tokens
  ADD CONSTRAINT fk_reset_employee FOREIGN KEY (employee_id) REFERENCES evaluation.employees(id);

-- =====================
-- ダミーデータの挿入
-- =====================

-- employees
INSERT INTO evaluation.employees (id, name, email, password, is_admin, permission, failed_count, is_locked, locked_at, role) VALUES
(1, '管理 太郎', 'admin@example.com', 'hashed_password', true, 'admin', 0, false, NULL, '部長'),
(2, '一般 花子', 'user@example.com', 'hashed_password', false, NULL, 0, false, NULL, '社員');

-- phases
INSERT INTO evaluation.phases (id, name, start_date, end_date, self_eval_due, peer_eval_due, created_at, updated_at, phase_number) VALUES
(1, '2025年度上期', '2025-04-01', '2025-09-30', '2025-06-30 23:59:59', '2025-07-31 23:59:59', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- evaluations
INSERT INTO evaluation.evaluations (id, evaluator_id, target_id, skill_score, business_score, team_score, comment, created_at, updated_at, phase_id) VALUES
(1, 2, 1, 4.0, 3.5, 4.5, 'がんばっていました', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
