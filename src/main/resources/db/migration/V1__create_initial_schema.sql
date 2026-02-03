-- Create profile table
CREATE TABLE profile (
    id INTEGER PRIMARY KEY,
    type VARCHAR(255) NOT NULL UNIQUE,
    date_created TIMESTAMPTZ NOT NULL,
    last_updated TIMESTAMPTZ NOT NULL
);

-- Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_created TIMESTAMPTZ NOT NULL,
    last_updated TIMESTAMPTZ NOT NULL
);

-- Create address table
CREATE TABLE address (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    address_line VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    main BOOLEAN NOT NULL DEFAULT false,
    neighborhood VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    state_province VARCHAR(255) NOT NULL,
    street_name VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL,
    date_created TIMESTAMPTZ NOT NULL,
    last_updated TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create user_profile junction table
CREATE TABLE user_profile (
    user_id UUID NOT NULL,
    profile_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, profile_id),
    CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_profile_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

-- Create restaurant table
CREATE TABLE restaurant (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    address_id UUID,
    culinary VARCHAR(255),
    opening_time TIMESTAMPTZ,
    closing_time TIMESTAMPTZ,
    user_id UUID,
    cnpj VARCHAR(255) UNIQUE,
    active BOOLEAN DEFAULT true,
    date_created TIMESTAMPTZ,
    last_updated TIMESTAMPTZ,
    CONSTRAINT fk_restaurant_address FOREIGN KEY (address_id) REFERENCES address(id),
    CONSTRAINT fk_restaurant_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create food_item table
CREATE TABLE food_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    description VARCHAR(255),
    price DECIMAL(10,2),
    photo_key VARCHAR(255),
    restaurant_id UUID,
    delivery_available BOOLEAN DEFAULT false,
    available BOOLEAN DEFAULT true,
    active BOOLEAN DEFAULT true,
    date_created TIMESTAMPTZ,
    last_updated TIMESTAMPTZ,
    CONSTRAINT fk_food_item_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_login ON users(login);
CREATE INDEX idx_address_user_id ON address(user_id);
CREATE INDEX idx_profile_type ON profile(type);
CREATE INDEX idx_user_profile_user_id ON user_profile(user_id);
CREATE INDEX idx_user_profile_profile_id ON user_profile(profile_id);
CREATE INDEX idx_restaurant_user_id ON restaurant(user_id);
CREATE INDEX idx_restaurant_cnpj ON restaurant(cnpj);
CREATE INDEX idx_food_item_restaurant_id ON food_item(restaurant_id);
