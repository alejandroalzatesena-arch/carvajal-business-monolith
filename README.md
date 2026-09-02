# Carvajal Business Monolith - Full Stack

Proyecto full-stack integrado: Backend **Spring Boot 3.3.5** (Java 17) + Frontend **Angular 17** + **PostgreSQL**.

## Estructura

```
.
├── src/                        # Backend Spring Boot (com.carvajalecomers)
├── db/
│   ├── schema.sql              # Script de creación de tablas
│   └── data.sql                # Datos de prueba (usuarios y productos)
├── carvajal-ecommerce/         # Frontend Angular 17
└── pom.xml                     # Configuración Maven del backend
```

## Requisitos

- Java 17
- Maven (o usar `./mvnw`)
- Node.js + npm
- Angular CLI 17 (`npm i -g @angular/cli@17`)
- PostgreSQL

## 1. Base de datos (PostgreSQL)

Crear la base de datos y el usuario:

```sql
CREATE USER carvajal WITH PASSWORD 'carvajal123';
CREATE DATABASE carvajal_wishlist OWNER carvajal;
```

Cargar los scripts (desde la raíz del proyecto):

```bash
psql -U carvajal -d carvajal_wishlist -f db/schema.sql
psql -U carvajal -d carvajal_wishlist -f db/data.sql
```

Conexión esperada por el backend (application.properties):
- URL: `jdbc:postgresql://localhost:5432/carvajal_wishlist`
- Usuario: `carvajal` / Contraseña: `carvajal123`

## 2. Backend (puerto 8080)

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

Usuarios de prueba (tabla `users`):
- Email: `cliente.demo@carvajal.com` / Password: `carvajal123`

### Endpoints

- `POST /api/auth/login` — autenticación (público)
- `POST /api/auth/register` — registro (público)
- `GET /api/products` — catálogo (público)
- `GET /api/products/{id}` — detalle de producto (público)
- `GET /api/wishlist` — lista de deseos (JWT)
- `POST /api/wishlist/items` — agregar item (JWT)
- `PUT /api/wishlist/items/{productId}` — actualizar cantidad (JWT)
- `DELETE /api/wishlist/items/{productId}` — eliminar item (JWT)
- `GET /api/wishlist/history` — histórico (JWT)

CORS habilitado para `http://localhost:4200`.

## 3. Frontend (puerto 4200)

```bash
cd carvajal-ecommerce
npm install
ng serve
```

La app queda disponible en `http://localhost:4200`.

`apiUrl` configurado en `src/environments/environment.ts` apuntando a `http://localhost:8080`.

### Funcionalidades

- Login / Registro de usuarios
- Catálogo de productos con stock
- Lista de deseos CRUD (agregar, actualizar cantidad, eliminar)
- Notificación visual de productos sin stock / stock insuficiente
- Historial de actividad (en `/admin/history`)
