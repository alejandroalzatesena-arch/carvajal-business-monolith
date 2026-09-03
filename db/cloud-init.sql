-- =============================================================================
--  Inicializacion idempotente para despliegues en la nube (Render, Railway...)
--
--  A diferencia de db/schema.sql, este script NO borra nada: usa
--  CREATE TABLE IF NOT EXISTS e INSERT ... ON CONFLICT DO NOTHING, de modo que
--  puede ejecutarse en cada arranque sin destruir datos ni fallar.
--
--  Lo lanza Spring (spring.sql.init) ANTES de que Hibernate valide el esquema,
--  asi que el backend puede arrancar contra una base de datos vacia sin
--  necesidad de sembrarla a mano.
--
--  En local NO se ejecuta: docker-compose carga db/schema.sql y db/data.sql
--  mediante docker-entrypoint-initdb.d y no define SPRING_SQL_INIT_MODE.
-- =============================================================================

CREATE TABLE IF NOT EXISTS users (
    id         BIGSERIAL     PRIMARY KEY,
    full_name  VARCHAR(120)  NOT NULL,
    email      VARCHAR(150)  NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
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

CREATE TABLE IF NOT EXISTS wishlists (
    id         BIGSERIAL  PRIMARY KEY,
    user_id    BIGINT     NOT NULL UNIQUE,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS wishlist_items (
    id               BIGSERIAL  PRIMARY KEY,
    wishlist_id      BIGINT     NOT NULL,
    product_id       BIGINT     NOT NULL,
    desired_quantity INTEGER    NOT NULL DEFAULT 1 CHECK (desired_quantity > 0),
    added_at         TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlists (id),
    CONSTRAINT fk_item_product  FOREIGN KEY (product_id)  REFERENCES products (id),
    CONSTRAINT uq_wishlist_product UNIQUE (wishlist_id, product_id)
);

CREATE TABLE IF NOT EXISTS wishlist_history (
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

CREATE INDEX IF NOT EXISTS idx_items_wishlist   ON wishlist_items (wishlist_id);
CREATE INDEX IF NOT EXISTS idx_history_wishlist ON wishlist_history (wishlist_id);
CREATE INDEX IF NOT EXISTS idx_products_active  ON products (active);

-- ----------------------------------------------------------------------------
--  Datos de demostracion. ON CONFLICT DO NOTHING los hace repetibles.
-- ----------------------------------------------------------------------------
INSERT INTO users (id, full_name, email, password) VALUES
    (1, 'Cliente Demo Carvajal', 'cliente.demo@carvajal.com',
     '$2a$10$2911UFLpYjT6ne7eW5soT.HVGTlLX6Sqd1s0WK3zoHppp56Ks7C/O')
ON CONFLICT (id) DO NOTHING;


INSERT INTO products (id, name, description, price, stock, image_url, category, active) VALUES
    (1,  'Portatil HP Pavilion 15',        'Core i5-1335U, 16GB RAM, 512GB SSD, 15.6" FHD, Windows 11.',       3199000.00, 25, 'https://picsum.photos/seed/hp-pavilion/300/300',  'Portatiles',   TRUE),
    (2,  'Portatil Lenovo IdeaPad 3',      'Ryzen 5 7520U, 8GB RAM, 512GB SSD, 15.6" FHD.',                    2499000.00, 40, 'https://picsum.photos/seed/lenovo-ideapad/300/300','Portatiles',   TRUE),
    (3,  'Portatil ASUS TUF Gaming F15',   'Core i7-12700H, 16GB RAM, RTX 4060, 512GB SSD, 144Hz.',            5899000.00, 12, 'https://picsum.photos/seed/asus-tuf/300/300',     'Portatiles',   TRUE),
    (4,  'Portatil Dell Inspiron 15',      'Core i5-1334U, 8GB RAM, 256GB SSD, 15.6" FHD.',                    2799000.00, 18, 'https://picsum.photos/seed/dell-inspiron/300/300','Portatiles',   TRUE),
    (5,  'PC Escritorio HP All-in-One 24', 'Core i5, 16GB RAM, 1TB SSD, pantalla 23.8" integrada.',            3499000.00, 0,  'https://picsum.photos/seed/hp-aio/300/300',       'Escritorio',   TRUE),
    (6,  'Monitor LG UltraGear 27"',       'Monitor gamer 27" QHD 165Hz, 1ms, panel IPS.',                     1249000.00, 30, 'https://picsum.photos/seed/lg-monitor/300/300',   'Monitores',    TRUE),
    (7,  'Monitor Samsung 24" FHD',        'Monitor 24" Full HD 75Hz, panel IPS, HDMI + VGA.',                 629000.00,  50, 'https://picsum.photos/seed/samsung-mon/300/300',  'Monitores',    TRUE),
    (8,  'Memoria RAM Kingston Fury 16GB', 'DDR4 3200MHz, modulo unico de 16GB para portatil/PC.',             229000.00,  80, 'https://picsum.photos/seed/kingston-ram/300/300', 'Componentes',  TRUE),
    (9,  'SSD Samsung 970 EVO 1TB',        'Unidad de estado solido NVMe M.2, lectura 3500 MB/s.',             469000.00,  60, 'https://picsum.photos/seed/samsung-ssd/300/300',  'Componentes',  TRUE),
    (10, 'Tarjeta Grafica ASUS RTX 4060',  'GPU NVIDIA GeForce RTX 4060 8GB GDDR6.',                           1699000.00, 15, 'https://picsum.photos/seed/asus-rtx/300/300',     'Componentes',  TRUE),
    (11, 'Procesador AMD Ryzen 7 5700X',   'CPU 8 nucleos / 16 hilos, socket AM4, 4.6GHz turbo.',              899000.00,  0,  'https://picsum.photos/seed/ryzen-cpu/300/300',    'Componentes',  TRUE),
    (12, 'Teclado Mecanico Logitech G Pro','Teclado mecanico gamer, switches GX, RGB, formato TKL.',           419000.00,  45, 'https://picsum.photos/seed/logitech-kb/300/300',  'Accesorios',   TRUE),
    (13, 'Mouse Logitech G502 Hero',       'Mouse gamer 25.600 DPI, 11 botones programables.',                 219000.00,  70, 'https://picsum.photos/seed/logitech-mouse/300/300','Accesorios',   TRUE),
    (14, 'Audifonos HyperX Cloud II',      'Diadema gamer con sonido envolvente 7.1 y microfono.',             329000.00,  38, 'https://picsum.photos/seed/hyperx/300/300',       'Accesorios',   TRUE),
    (15, 'Disco Duro Externo WD 2TB',      'Disco externo portatil USB 3.0, 2TB de capacidad.',                349000.00,  55, 'https://picsum.photos/seed/wd-hdd/300/300',       'Accesorios',   TRUE)
ON CONFLICT (id) DO NOTHING;


-- Las secuencias deben quedar por encima de los ids insertados a mano.
SELECT setval(pg_get_serial_sequence('users',    'id'), COALESCE((SELECT MAX(id) FROM users),    1));
SELECT setval(pg_get_serial_sequence('products', 'id'), COALESCE((SELECT MAX(id) FROM products), 1));
