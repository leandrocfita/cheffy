-- Rename users table to user
ALTER TABLE users RENAME TO "user";

-- Recreate indexes with new table name
DROP INDEX IF EXISTS idx_users_email;
DROP INDEX IF EXISTS idx_users_login;
CREATE INDEX idx_user_email ON "user"(email);
CREATE INDEX idx_user_login ON "user"(login);
