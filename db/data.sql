INSERT INTO users (id, full_name, email, password) VALUES
    (1, 'Cliente Demo Carvajal', 'cliente.demo@carvajal.com', 'carvajal123');

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
    (15, 'Disco Duro Externo WD 2TB',      'Disco externo portatil USB 3.0, 2TB de capacidad.',                349000.00,  55, 'https://picsum.photos/seed/wd-hdd/300/300',       'Accesorios',   TRUE);

SELECT setval(pg_get_serial_sequence('users',    'id'), (SELECT MAX(id) FROM users));
SELECT setval(pg_get_serial_sequence('products', 'id'), (SELECT MAX(id) FROM products));
