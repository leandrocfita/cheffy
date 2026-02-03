-- Insert default profiles
INSERT INTO profile (id, type, date_created, last_updated)
VALUES (1, 'CLIENT', NOW(), NOW())
ON CONFLICT (type) DO NOTHING;

INSERT INTO profile (id, type, date_created, last_updated)
VALUES (2, 'OWNER', NOW(), NOW())
ON CONFLICT (type) DO NOTHING;
