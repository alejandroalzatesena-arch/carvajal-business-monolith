DROP TABLE IF EXISTS wishlist_history CASCADE;
DROP TABLE IF EXISTS wishlist_items   CASCADE;
DROP TABLE IF EXISTS wishlists        CASCADE;
DROP TABLE IF EXISTS products         CASCADE;
DROP TABLE IF EXISTS users            CASCADE;

CREATE TABLE users (
    id         BIGSERIAL     PRIMARY KEY,
    full_name  VARCHAR(120)  NOT NULL,
    email      VARCHAR(150)  NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id          BIGSERIAL      PRIMARY KEY,
    name        VARCHAR(150)   NOT NULL,
    description VARCHAR(500),
    price       NUMERIC(12,2)  NOT NULL CHECK (price >= 0),
    stock       INTEGER        NOT NULL CHECK (stock >= 0),
    image_url   VARCHAR(300),
    category    VARCHAR(80),
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wishlists (
    id         BIGSERIAL  PRIMARY KEY,
    user_id    BIGINT     NOT NULL UNIQUE,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE wishlist_items (
    id               BIGSERIAL  PRIMARY KEY,
    wishlist_id      BIGINT     NOT NULL,
    product_id       BIGINT     NOT NULL,
    desired_quantity INTEGER    NOT NULL DEFAULT 1 CHECK (desired_quantity > 0),
    added_at         TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlists (id),
    CONSTRAINT fk_item_product  FOREIGN KEY (product_id)  REFERENCES products (id),
    CONSTRAINT uq_wishlist_product UNIQUE (wishlist_id, product_id)
);

CREATE TABLE wishlist_history (
    id                     BIGSERIAL      PRIMARY KEY,
    wishlist_id            BIGINT         NOT NULL,
    product_id             BIGINT         NOT NULL,
    action                 VARCHAR(20)    NOT NULL,
    product_name_snapshot  VARCHAR(150)   NOT NULL,
    product_price_snapshot NUMERIC(12,2)  NOT NULL,
    action_at              TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlists (id),
    CONSTRAINT fk_history_product  FOREIGN KEY (product_id)  REFERENCES products (id),
    CONSTRAINT chk_history_action CHECK (action IN ('ADDED', 'UPDATED', 'REMOVED'))
);

CREATE INDEX idx_items_wishlist   ON wishlist_items (wishlist_id);
CREATE INDEX idx_history_wishlist ON wishlist_history (wishlist_id);
CREATE INDEX idx_products_active  ON products (active);
