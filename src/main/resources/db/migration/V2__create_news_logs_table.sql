CREATE TABLE IF NOT EXISTS news_log (
    id UUID PRIMARY KEY,
    execution_time TIMESTAMP NOT NULL,
    summary TEXT,
    status VARCHAR(50) NOT NULL,
    error_message TEXT
);

CREATE TABLE IF NOT EXISTS job_preferences (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    preferred_time TIME,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_user_preferences FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
