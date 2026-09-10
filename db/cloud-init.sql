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


-- =============================================================================
--  Catalogo de 48 productos en 9 categorias.
--
--  image_url usa placeholders temporales (picsum.photos) que se reemplazan
--  despues subiendo la imagen real a Cloudinary con
--  POST /api/products/{id}/image (mirar README, seccion Cloudinary).
--
--  Varios productos (ids 5, 11, 18, 23, 27, 33, 38, 40 y 47) tienen stock = 0
--  a proposito, para probar la notificacion de "sin stock" en la wishlist.
-- =============================================================================
INSERT INTO products (id, name, description, price, stock, image_url, category, active) VALUES
    -- Portatiles (8)
    (1,  'Portatil HP Pavilion 15',           'Core i5-1335U, 16GB RAM, 512GB SSD, 15.6" FHD, Windows 11.',       3199000.00, 25, 'https://picsum.photos/seed/hp-pavilion/300/300',  'Portatiles',           TRUE),
    (2,  'Portatil Lenovo IdeaPad 3',         'Ryzen 5 7520U, 8GB RAM, 512GB SSD, 15.6" FHD.',                    2499000.00, 40, 'https://picsum.photos/seed/lenovo-ideapad/300/300','Portatiles',           TRUE),
    (3,  'Portatil ASUS TUF Gaming F15',      'Core i7-12700H, 16GB RAM, RTX 4060, 512GB SSD, 144Hz.',            5899000.00, 12, 'https://picsum.photos/seed/asus-tuf/300/300',     'Portatiles',           TRUE),
    (4,  'Portatil Dell Inspiron 15',         'Core i5-1334U, 8GB RAM, 256GB SSD, 15.6" FHD.',                    2799000.00, 18, 'https://picsum.photos/seed/dell-inspiron/300/300','Portatiles',           TRUE),
    (16, 'Portatil Apple MacBook Air M2',     'Chip M2, 8GB RAM unificada, 256GB SSD, pantalla Retina 13.6".',     7499000.00, 8,  'https://picsum.photos/seed/macbook-air/300/300',  'Portatiles',           TRUE),
    (17, 'Portatil Acer Aspire 5',            'Ryzen 5 5500U, 8GB RAM, 512GB SSD, 15.6" FHD.',                    2199000.00, 30, 'https://picsum.photos/seed/acer-aspire/300/300',  'Portatiles',           TRUE),
    (18, 'Portatil MSI Katana GF66',          'Core i7-11800H, RTX 3050 Ti, 16GB RAM, 512GB SSD, 144Hz.',         5249000.00, 0,  'https://picsum.photos/seed/msi-katana/300/300',   'Portatiles',           TRUE),
    (19, 'Portatil HP ProBook 450 G10',       'Core i7-1355U, 16GB RAM, 512GB SSD, 15.6" FHD, ideal empresa.',    4499000.00, 6,  'https://picsum.photos/seed/hp-probook/300/300',  'Portatiles',           TRUE),

    -- Escritorio (2)
    (5,  'PC Escritorio HP All-in-One 24',    'Core i5, 16GB RAM, 1TB SSD, pantalla 23.8" integrada.',            3499000.00, 0,  'https://picsum.photos/seed/hp-aio/300/300',       'Escritorio',           TRUE),
    (20, 'PC Gamer Intel Core i5-13400F',     'RTX 4060, 16GB RAM, 1TB SSD NVMe, gabinete RGB, fuente 650W.',     4299000.00, 10, 'https://picsum.photos/seed/pc-gamer/300/300',     'Escritorio',           TRUE),

    -- Monitores (6)
    (6,  'Monitor LG UltraGear 27"',          'Monitor gamer 27" QHD 165Hz, 1ms, panel IPS.',                     1249000.00, 30, 'https://picsum.photos/seed/lg-monitor/300/300',   'Monitores',            TRUE),
    (7,  'Monitor Samsung 24" FHD',           'Monitor 24" Full HD 75Hz, panel IPS, HDMI + VGA.',                 629000.00,  50, 'https://picsum.photos/seed/samsung-mon/300/300',  'Monitores',            TRUE),
    (21, 'Monitor Dell UltraSharp 27" 4K',    'IPS 4K UHD 60Hz, 100% sRGB, USB-C 90W, para creadores.',          2299000.00, 14, 'https://picsum.photos/seed/dell-ultrasharp/300/300','Monitores',         TRUE),
    (22, 'Monitor Philips 27" Curvo',         'Full HD VA curvo, 75Hz, HDMI + DisplayPort.',                      849000.00,  42, 'https://picsum.photos/seed/philips-curvo/300/300','Monitores',            TRUE),
    (23, 'Monitor ASUS TUF Gaming 24"',       'Full HD 165Hz, 1ms, FreeSync Premium, compatible G-Sync.',         1099000.00, 0,  'https://picsum.photos/seed/asus-tuf-mon/300/300', 'Monitores',            TRUE),
    (24, 'Monitor Samsung Odyssey G5 27"',    'QHD curvo 144Hz, 1ms, HDR10, panel VA.',                           1599000.00, 22, 'https://picsum.photos/seed/odyssey-g5/300/300',   'Monitores',            TRUE),

    -- Teclados y mouse (7)
    (12, 'Teclado Mecanico Logitech G Pro',   'Teclado mecanico gamer, switches GX, RGB, formato TKL.',           419000.00,  45, 'https://picsum.photos/seed/logitech-kb/300/300',  'Teclados y mouse',     TRUE),
    (13, 'Mouse Logitech G502 Hero',          'Mouse gamer 25.600 DPI, 11 botones programables.',                 219000.00,  70, 'https://picsum.photos/seed/logitech-mouse/300/300','Teclados y mouse',    TRUE),
    (25, 'Teclado Mecanico Redragon Kumara',  'Switches Outemu Blue, RGB per-tecla, layout espanol.',              189000.00,  60, 'https://picsum.photos/seed/redragon-kumara/300/300','Teclados y mouse',  TRUE),
    (26, 'Teclado Corsair K70 RGB MK.2',      'Switches Cherry MX Red, chasis de aluminio, RGB.',                 449000.00,  25, 'https://picsum.photos/seed/corsair-k70/300/300',  'Teclados y mouse',     TRUE),
    (27, 'Teclado Inalambrico Logitech MX Keys','Teclas de membrana silenciosas, retroiluminacion inteligente, Bluetooth + USB.', 399000.00, 0, 'https://picsum.photos/seed/logitech-mx-keys/300/300','Teclados y mouse', TRUE),
    (28, 'Mouse Inalambrico Logitech M185',   '1000 DPI, mini receptor USB, 12 meses de bateria.',                49000.00,   90, 'https://picsum.photos/seed/logitech-m185/300/300','Teclados y mouse',     TRUE),
    (29, 'Mouse Gamer Razer Viper Mini',      '8500 DPI, 61 gramos, switches opticos, cable SpeedFlex.',          149000.00,  55, 'https://picsum.photos/seed/razer-viper/300/300',  'Teclados y mouse',     TRUE),

    -- Componentes (9)
    (8,  'Memoria RAM Kingston Fury 16GB',    'DDR4 3200MHz, modulo unico de 16GB para portatil/PC.',             229000.00,  80, 'https://picsum.photos/seed/kingston-ram/300/300', 'Componentes',          TRUE),
    (9,  'SSD Samsung 970 EVO 1TB',           'Unidad de estado solido NVMe M.2, lectura 3500 MB/s.',             469000.00,  60, 'https://picsum.photos/seed/samsung-ssd/300/300',  'Componentes',          TRUE),
    (10, 'Tarjeta Grafica ASUS RTX 4060',     'GPU NVIDIA GeForce RTX 4060 8GB GDDR6.',                           1699000.00, 15, 'https://picsum.photos/seed/asus-rtx/300/300',     'Componentes',          TRUE),
    (11, 'Procesador AMD Ryzen 7 5700X',      'CPU 8 nucleos / 16 hilos, socket AM4, 4.6GHz turbo.',              899000.00,  0,  'https://picsum.photos/seed/ryzen-cpu/300/300',    'Componentes',          TRUE),
    (30, 'Tarjeta Grafica AMD RX 6750 XT',    'GPU AMD Radeon RX 6750 XT 12GB GDDR6, ideal para 1440p.',          1999000.00, 9,  'https://picsum.photos/seed/amd-rx6750/300/300',  'Componentes',          TRUE),
    (31, 'Procesador Intel Core i5-13400F',   '10 nucleos (6P+4E), socket LGA1700, sin graficos integrados.',     999000.00,  18, 'https://picsum.photos/seed/intel-i5-13400f/300/300','Componentes',       TRUE),
    (32, 'Memoria RAM Corsair Vengeance 32GB','2 modulos DDR5 5600MHz, disipador de aluminio.',                   649000.00,  33, 'https://picsum.photos/seed/corsair-vengeance/300/300','Componentes',     TRUE),
    (33, 'SSD Crucial P3 Plus 2TB',           'NVMe PCIe Gen4, lectura hasta 5000 MB/s, M.2 2280.',               759000.00,  0,  'https://picsum.photos/seed/crucial-p3/300/300',   'Componentes',          TRUE),
    (34, 'Disco Duro Interno Seagate 2TB',    '7200RPM, SATA III, 256MB cache, 3.5".',                            289000.00,  47, 'https://picsum.photos/seed/seagate-hdd/300/300',  'Componentes',          TRUE),

    -- Accesorios (7)
    (14, 'Audifonos HyperX Cloud II',         'Diadema gamer con sonido envolvente 7.1 y microfono.',             329000.00,  38, 'https://picsum.photos/seed/hyperx/300/300',       'Accesorios',           TRUE),
    (35, 'Audifonos Sony WH-1000XM4',         'Cancelacion activa de ruido, Bluetooth, hasta 30h de bateria.',    1349000.00, 12, 'https://picsum.photos/seed/sony-xm4/300/300',     'Accesorios',           TRUE),
    (36, 'Audifonos Gamer Redragon Zeus X',   'Sonido 7.1, RGB, microfono removible, diadema acolchada.',         229000.00,  38, 'https://picsum.photos/seed/redragon-zeus/300/300','Accesorios',           TRUE),
    (37, 'Webcam Logitech C920 HD Pro',       '1080p 30fps, autoenfoque, doble microfono, correccion de luz.',    329000.00,  26, 'https://picsum.photos/seed/logitech-c920/300/300','Accesorios',           TRUE),
    (38, 'Hub USB 4 en 1 Baseus',             '2x USB 3.0, HDMI 4K, carga PD 100W, aluminio.',                    149000.00,  0,  'https://picsum.photos/seed/baseus-hub/300/300',   'Accesorios',           TRUE),
    (39, 'Soporte para Laptop Ajustable',     'Base de aluminio, 7 niveles de altura, plegable, portatil.',       79000.00,   64, 'https://picsum.photos/seed/laptop-stand/300/300', 'Accesorios',          TRUE),
    (40, 'Enfriador Base USB 4 Ventiladores', 'Base refrigerante con 4 ventiladores, LED azul, USB.',             99000.00,   0,  'https://picsum.photos/seed/cooler-laptop/300/300', 'Accesorios',         TRUE),

    -- Software / licencias (4)
    (41, 'Licencia Windows 11 Pro',           'Clave digital oficial, envio por correo, activacion de por vida.',  549000.00,  200, 'https://picsum.photos/seed/win11pro/300/300',     'Software',             TRUE),
    (42, 'Licencia Microsoft Office 2021',    'Home & Business, instalacion en 1 PC, licencia permanente.',       799000.00,  150, 'https://picsum.photos/seed/office2021/300/300',   'Software',             TRUE),
    (43, 'Curso Python para Desarrollo Web',  'Mas de 40 horas de video, proyectos reales, acceso de por vida.',  199000.00,  300, 'https://picsum.photos/seed/curso-python/300/300', 'Software',            TRUE),
    (44, 'Libro Clean Code (Espanol)',        'Robert C. Martin, edicion en espanol, PDF + ePub, descarga inmediata.', 129000.00, 80, 'https://picsum.photos/seed/clean-code/300/300', 'Software',           TRUE),

    -- Redes (3)
    (45, 'Router Wi-Fi 6 TP-Link Archer AX55','Doble banda AX3000, 4 puertos Gigabit, USB 3.0.',                  449000.00,  20, 'https://picsum.photos/seed/tp-link-ax55/300/300', 'Redes',                TRUE),
    (46, 'Adaptador Wi-Fi USB TP-Link ART3U', 'Dual band AC1300, mini receptor, Windows/Mac/Linux.',              89000.00,   58, 'https://picsum.photos/seed/tp-link-t3u/300/300',  'Redes',                TRUE),
    (47, 'Switch Gigabit TP-Link TL-SG108',   '8 puertos 10/100/1000, carcasa metalica, plug and play.',          139000.00,  0,  'https://picsum.photos/seed/tp-link-sg108/300/300','Redes',                TRUE),

    -- Almacenamiento externo (2)
    (15, 'Disco Duro Externo WD 2TB',         'Disco externo portatil USB 3.0, 2TB de capacidad.',                349000.00,  55, 'https://picsum.photos/seed/wd-hdd/300/300',       'Almacenamiento externo',TRUE),
    (48, 'SSD Portatil Samsung T7 1TB',       'USB 3.2 Gen2, hasta 1050 MB/s, resistente a caidas.',              689000.00,  28, 'https://picsum.photos/seed/samsung-t7/300/300',   'Almacenamiento externo',TRUE)
ON CONFLICT (id) DO NOTHING;


-- Las secuencias deben quedar por encima de los ids insertados a mano.
SELECT setval(pg_get_serial_sequence('users',    'id'), COALESCE((SELECT MAX(id) FROM users),    1));
SELECT setval(pg_get_serial_sequence('products', 'id'), COALESCE((SELECT MAX(id) FROM products), 1));
