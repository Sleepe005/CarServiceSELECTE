

-- Миграция V1: Создание таблицы users

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

COMMENT ON TABLE users IS 'Пользователи системы подбора автомобилей';
COMMENT ON COLUMN users.id IS 'Уникальный идентификатор пользователя';
COMMENT ON COLUMN users.email IS 'Email пользователя (логин)';
COMMENT ON COLUMN users.password_hash IS 'Хеш пароля (BCrypt)';
COMMENT ON COLUMN users.full_name IS 'Полное имя пользователя';
COMMENT ON COLUMN users.created_at IS 'Дата и время регистрации';

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);