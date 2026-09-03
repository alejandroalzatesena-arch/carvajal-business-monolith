# Carvajal E-commerce — Lista de Deseos (Wishlist)

Aplicación **monolítica full-stack** para la gestión de listas de deseos de un
e-commerce: catálogo de productos, wishlist con CRUD, aviso de productos sin
stock e histórico de actividad, con autenticación JWT.

| Capa | Tecnología |
|------|-----------|
| Backend | Spring Boot 3.3.5 · Java 17 · Maven |
| Seguridad | Spring Security + JWT (jjwt 0.12.6) |
| Base de datos | PostgreSQL 16 |
| Frontend | Angular 17 + Angular Material |
| Contenedores | Docker (multi-stage) + Docker Compose |
| Despliegue | Railway (PostgreSQL **gestionado** + 2 servicios Docker) |

---

## Integrantes y roles

| # | Integrante | Rol |
|---|-----------|-----|
| 1 | **Juan Esteban Cardona** | Backend Core + Base de datos (entidades, repositorios, servicios, scripts SQL) |
| 2 | **Alejandro Alzate** | API REST + Seguridad JWT (controllers, Spring Security, CORS) |
| 3 | **Juan Sebastián Ospina** | Frontend Angular 17 + integración full-stack |
| 4 | **Vanessa Triviño** | DevOps: Docker, Docker Compose, Railway y documentación |

---

## Estructura del monorepo

```
carvajal-business-monolith/
├── src/                              # Backend Spring Boot (com.carvajalecomers)
│   ├── main/java/com/carvajalecomers/
│   │   ├── controller/               # AuthController, ProductController, WishlistController
│   │   ├── dto/                      # Requests y responses de la API
│   │   ├── entity/                   # User, Product, Wishlist, WishlistItem, WishlistHistory
│   │   ├── repository/               # Repositorios JPA
│   │   ├── security/                 # SecurityConfig, filtro y provider JWT
│   │   └── service/                  # Lógica de negocio
│   └── main/resources/application.properties
│
├── db/
│   ├── schema.sql                    # DDL: tablas, FKs, índices
│   ├── data.sql                      # Datos de prueba (1 usuario + 15 productos)
│   └── MODELO_BASE_DATOS.md          # Diagrama ER y explicación del modelo
│
├── carvajal-ecommerce/               # Frontend Angular 17
│   ├── src/app/core/                 # Servicios, guards, interceptor JWT, modelos
│   ├── src/app/pages/                # login, register, catalog, wishlist, profile, admin
│   ├── src/environments/             # environment.ts / environment.prod.ts (apiUrl)
│   ├── nginx/default.conf.template   # Config de Nginx (SPA + proxy /api)  [DevOps]
│   ├── Dockerfile                    # Build Angular + Nginx                [DevOps]
│   └── .dockerignore                                                       # [DevOps]
│
├── Dockerfile                        # Backend multi-stage Java 17          [DevOps]
├── .dockerignore                                                           # [DevOps]
├── docker-compose.yml                # SOLO desarrollo local                [DevOps]
├── railway.toml                      # Config del servicio backend Railway  [DevOps]
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

---

## Requisitos

**Para ejecución con Docker (recomendado):**

- Docker Engine 24+ y Docker Compose v2
- Puertos libres: `4200` (frontend), `8080` (backend), `5432` (PostgreSQL)

**Para ejecución nativa (sin Docker):**

- Java 17 (JDK)
- Maven 3.9+ *(opcional: el repo incluye `./mvnw`)*
- Node.js 20+ y npm
- Angular CLI 17 — `npm i -g @angular/cli@17`
- PostgreSQL 14+

---

## Opción A — Ejecución local con Docker Compose

Es el camino más rápido: levanta PostgreSQL, backend y frontend con un comando.

```bash
docker compose up -d --build
```

| Servicio | URL |
|----------|-----|
| Frontend | http://localhost:4200 |
| Backend (API) | http://localhost:8080 |
| PostgreSQL | `localhost:5432` — db `carvajal_wishlist`, user `carvajal` / `carvajal123` |

Qué hace el compose:

1. Arranca `postgres:16-alpine` y ejecuta **automáticamente** `db/schema.sql` y
   `db/data.sql` montados en `/docker-entrypoint-initdb.d/`. Esto es obligatorio
   porque el backend arranca con `ddl-auto=validate` y **no crea tablas**.
2. Espera al `healthcheck` de PostgreSQL (`pg_isready`) antes de arrancar el backend.
3. Compila el backend (Maven → JAR) y el frontend (Angular → Nginx) en imágenes
   multi-stage.

Comandos útiles:

```bash
docker compose logs -f backend      # seguir logs del backend
docker compose ps                   # estado de los servicios
docker compose down                 # apagar conservando los datos
docker compose down -v              # apagar y BORRAR la BD (relanza schema.sql + data.sql)
docker compose up -d --build backend  # reconstruir solo el backend
```

> **Los scripts SQL solo se ejecutan cuando el volumen está vacío.** Si cambias
> `db/schema.sql` necesitas `docker compose down -v` para que se vuelvan a aplicar.

> **Este `docker-compose.yml` es solo para desarrollo local.** No es un modelo de
> producción: credenciales fijas, sin backups y con PostgreSQL en contenedor.
> En Railway se usa PostgreSQL **gestionado** (ver más abajo).

---

## Opción B — Ejecución local sin Docker

### 1. Base de datos

```sql
CREATE USER carvajal WITH PASSWORD 'carvajal123';
CREATE DATABASE carvajal_wishlist OWNER carvajal;
```

Cargar los scripts desde la raíz del repositorio:

```bash
psql -U carvajal -d carvajal_wishlist -f db/schema.sql
psql -U carvajal -d carvajal_wishlist -f db/data.sql
```

Conexión esperada por `src/main/resources/application.properties`:

- URL: `jdbc:postgresql://localhost:5432/carvajal_wishlist`
- Usuario: `carvajal` / Contraseña: `carvajal123`
- `spring.jpa.hibernate.ddl-auto=validate` → Hibernate **valida** el esquema, no lo crea.

### 2. Backend (puerto 8080)

```bash
./mvnw spring-boot:run
```

En Windows (PowerShell / CMD):

```bash
mvnw.cmd spring-boot:run
```

### 3. Frontend (puerto 4200)

```bash
cd carvajal-ecommerce
npm install
ng serve
```

---

## Credenciales de prueba

| Campo | Valor |
|-------|-------|
| Email | `cliente.demo@carvajal.com` |
| Password | `carvajal123` |

*(cargado por `db/data.sql`)*

> La columna `users.password` almacena el **hash BCrypt**, nunca la contraseña en
> claro: el backend autentica con `BCryptPasswordEncoder` y rechaza cualquier
> otro formato. Si añades usuarios a mano al seed, inserta el hash (60
> caracteres, prefijo `$2a$`), no el texto plano.

---

## Endpoints principales

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| `POST` | `/api/auth/register` | Público | Registro de usuario |
| `POST` | `/api/auth/login` | Público | Login → devuelve el JWT |
| `GET` | `/api/products` | Público | Catálogo de productos |
| `GET` | `/api/products/{id}` | Público | Detalle de un producto |
| `GET` | `/api/wishlist` | JWT | Items de la lista de deseos |
| `POST` | `/api/wishlist/items` | JWT | Agregar producto |
| `PUT` | `/api/wishlist/items/{productId}` | JWT | Actualizar cantidad deseada |
| `DELETE` | `/api/wishlist/items/{productId}` | JWT | Eliminar producto |
| `GET` | `/api/wishlist/history` | JWT | Histórico de la wishlist |

Las rutas con JWT requieren la cabecera `Authorization: Bearer <token>`
(el interceptor de Angular la añade automáticamente).

Prueba rápida:

```bash
curl -s http://localhost:8080/api/products | head -c 300
```

---
## Variables de entorno

`application.properties` trae valores por defecto para desarrollo local. En
Docker y en Railway **no se edita ese fichero**: se sobreescriben las
propiedades con variables de entorno gracias al *relaxed binding* de Spring Boot
(`jwt.expiration-ms` se resuelve desde `JWT_EXPIRATION_MS`).

### Backend

| Variable | Propiedad Spring | Ejemplo | Obligatoria |
|----------|-----------------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `spring.datasource.url` | `jdbc:postgresql://host:5432/railway` | Sí |
| `SPRING_DATASOURCE_USERNAME` | `spring.datasource.username` | `postgres` | Sí |
| `SPRING_DATASOURCE_PASSWORD` | `spring.datasource.password` | (secreto) | Sí |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `spring.jpa.hibernate.ddl-auto` | `validate` | No (default `validate`) |
| `JWT_SECRET` | `jwt.secret` | clave Base64 de 32 bytes o más (HS256) | Sí |
| `JWT_EXPIRATION_MS` | `jwt.expiration-ms` | `86400000` (24 h) | Sí |
| `PORT` | puerto del servidor HTTP | `8080` | Lo inyecta Railway |
| `JAVA_OPTS` | flags de la JVM | `-XX:MaxRAMPercentage=75.0` | No |

Generar un `JWT_SECRET` nuevo para producción:

```bash
openssl rand -base64 48
```

### Frontend (imagen Nginx)

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `PORT` | Puerto en el que escucha Nginx | `80` (Railway lo inyecta) |
| `BACKEND_URL` | Destino del proxy `/api`, **sin barra final** | `http://backend:8080` |

---

## Despliegue en Railway

Arquitectura objetivo: **3 componentes** dentro de un mismo proyecto de Railway.

```
Proyecto Railway
├── Postgres    <- base de datos GESTIONADA por Railway (no un contenedor propio)
├── backend     <- este repo, Root Directory = /                  , Dockerfile
└── frontend    <- este repo, Root Directory = carvajal-ecommerce , Dockerfile
```

> **Nunca despliegues el servicio `postgres` de `docker-compose.yml` en Railway.**
> Ese contenedor pierde los datos en cada redeploy y no tiene backups. Railway
> ofrece PostgreSQL gestionado con volumen persistente, backups y credenciales
> rotables: eso es lo que se debe usar.

### Paso 0 — Coste (léelo antes de empezar)

**Railway no tiene plan gratuito permanente.** Ofrece un crédito de prueba de un
solo uso y, agotado, hay que pasar a un plan de pago (Hobby, ~5 USD/mes más
consumo) para mantener los servicios vivos. Los tres componentes de este
proyecto —PostgreSQL gestionado, backend y frontend— consumen de ese saldo.

No crees cuentas adicionales para renovar el crédito de prueba: incumple los
términos de servicio de Railway y expone a que se suspendan todas las cuentas
implicadas, incluida la que ya tenga el proyecto.

Si solo necesitas demostrar la aplicación funcionando, el
[despliegue local con Docker Compose](#opción-a--ejecución-local-con-docker-compose)
levanta el sistema completo sin coste alguno.

### Paso 1 — Crear el proyecto

1. Entra en <https://railway.app> y pulsa **New Project**.
2. Elige **Deploy from GitHub repo** y selecciona
   `alejandroalzatesena-arch/carvajal-business-monolith`.
3. Selecciona la rama a desplegar (`main` o `develop`).

### Paso 2 — Añadir PostgreSQL gestionado

1. Dentro del proyecto: **+ New → Database → Add PostgreSQL**.
2. Railway crea el servicio `Postgres` y expone estas variables:
   `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`,
   `DATABASE_URL` y `DATABASE_PUBLIC_URL`.

> **Cuidado con `DATABASE_URL`.** Railway la entrega en formato libpq
> (`postgresql://usuario:clave@host:puerto/bd`), que **no es una URL JDBC válida**.
> Spring necesita `jdbc:postgresql://host:puerto/bd` con usuario y contraseña por
> separado. Por eso en el paso 4 se construye `SPRING_DATASOURCE_URL` a partir de
> las variables `PG*` en lugar de reutilizar `DATABASE_URL`.

### Paso 3 — Cargar el esquema en la base de datos gestionada

El backend arranca con `ddl-auto=validate`, así que **las tablas deben existir
antes del primer despliegue**. Con la CLI de Railway:

```bash
railway login
railway link
railway connect Postgres
```

Y dentro de la sesión `psql` que se abre:

```sql
\i db/schema.sql
\i db/data.sql
```

Alternativa sin CLI — copia `DATABASE_PUBLIC_URL` desde la pestaña *Variables*
del servicio Postgres y ejecuta:

```bash
psql "$DATABASE_PUBLIC_URL" -f db/schema.sql
psql "$DATABASE_PUBLIC_URL" -f db/data.sql
```

### Paso 4 — Desplegar el backend con Dockerfile

1. En el servicio creado en el paso 1, abre **Settings**:
   - **Root Directory**: `/`
   - **Builder**: `Dockerfile` (Railway detecta el `Dockerfile` de la raíz; la
     configuración también queda fijada en `railway.toml`).
   - Renombra el servicio a `backend`.
2. En **Variables**, añade lo siguiente. La sintaxis `${{Postgres.X}}` son
   referencias entre servicios de Railway: se resuelven solas y viajan por la
   red privada del proyecto.

   ```
   SPRING_DATASOURCE_URL         = jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
   SPRING_DATASOURCE_USERNAME    = ${{Postgres.PGUSER}}
   SPRING_DATASOURCE_PASSWORD    = ${{Postgres.PGPASSWORD}}
   SPRING_JPA_HIBERNATE_DDL_AUTO = validate
   JWT_SECRET                    = (resultado de: openssl rand -base64 48)
   JWT_EXPIRATION_MS             = 86400000
   ```

   `PORT` lo inyecta Railway automáticamente y el `ENTRYPOINT` del `Dockerfile`
   lo lee con `-Dserver.port=$PORT`, así que no hay que declararlo.
3. **Settings → Networking → Generate Domain** para obtener la URL pública,
   por ejemplo `https://backend-production-xxxx.up.railway.app`.
4. Verifica el despliegue:

   ```bash
   curl -s https://TU-BACKEND.up.railway.app/api/products
   ```

### Paso 5 — Desplegar el frontend (build + Nginx)

1. **+ New → GitHub Repo** y elige el **mismo** repositorio.
2. En **Settings** del nuevo servicio:
   - **Root Directory**: `carvajal-ecommerce`
   - **Builder**: `Dockerfile` (usa `carvajal-ecommerce/Dockerfile`)
   - Renombra el servicio a `frontend`.
3. En **Variables** (necesario para el proxy `/api`, que es la configuración
   activa por defecto — ver **opción A** en la sección siguiente):

   ```
   BACKEND_URL = http://${{backend.RAILWAY_PRIVATE_DOMAIN}}:8080
   ```

   `PORT` lo inyecta Railway.
4. **Settings → Networking → Generate Domain**.

---

## CORS y `apiUrl` del frontend

En local todo encaja: Angular sirve en `http://localhost:4200` y `SecurityConfig`
permite exactamente ese origen. **En Railway los dominios son otros**, así que hay
que elegir una de estas dos opciones.

### Opción A — Proxy inverso en Nginx (activa por defecto, sin tocar Java)

**Es la que está configurada en el repositorio; no hay que hacer nada más.**

El `Dockerfile` del frontend incluye `nginx/default.conf.template` con un bloque
`location /api/` que reenvía las peticiones al backend, y
`carvajal-ecommerce/src/environments/environment.prod.ts` ya usa rutas relativas:

```ts
export const environment = {
  production: true,
  apiUrl: ''            // rutas relativas: /api/... las resuelve Nginx
};
```

Los servicios construyen las URLs como `${environment.apiUrl}/api/...`, así que
con `apiUrl: ''` piden `/api/products`, `/api/auth/login`, etc. al **mismo
origen** desde el que se sirvió la app. El navegador no dispara preflight,
**CORS deja de aplicar** y no hay que tocar `SecurityConfig`.

`ng serve` no se ve afectado: el build de desarrollo usa `environment.ts`, que
mantiene `apiUrl: 'http://localhost:8080'`.

> El `proxy_pass` usa una variable y un `resolver` local, así que Nginx resuelve
> el DNS de `BACKEND_URL` **en cada petición**, no al cargar la configuración.
> Gracias a eso el frontend arranca aunque el backend todavía no esté disponible
> (devuelve `502` hasta que lo esté) y sigue las reasignaciones de IP privada de
> Railway sin necesidad de reiniciarlo.

### Opción B — Llamada directa al dominio público + CORS en el backend

Alternativa a la opción A, si prefieres que el navegador llame directamente al
dominio público del backend en vez de pasar por el proxy de Nginx. **Requiere
revertir `apiUrl: ''` y además un cambio en Java**, por eso no es la opción por
defecto:

1. `carvajal-ecommerce/src/environments/environment.prod.ts`:

   ```ts
   export const environment = {
     production: true,
     apiUrl: 'https://TU-BACKEND.up.railway.app'
   };
   ```

2. **`SecurityConfig.java` tiene que aceptar el dominio del frontend.** Hoy el
   origen está fijado a `http://localhost:4200`, por lo que en Railway **todas
   las peticiones fallarían con error de CORS**. Este cambio **no está aplicado**
   en el repositorio, porque la configuración activa es la opción A y con ella
   no hace falta:

   ```java
   // antes
   config.setAllowedOrigins(List.of("http://localhost:4200"));

   // después: configurable por entorno
   @Value("${app.cors.allowed-origins:http://localhost:4200}")
   private String allowedOrigins;
   // ...
   config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
   ```

   y en las variables del servicio `backend` de Railway:

   ```
   APP_CORS_ALLOWED_ORIGINS = http://localhost:4200,https://TU-FRONTEND.up.railway.app
   ```

---

## Ramas (GitFlow)

| Rama | Propósito |
|------|-----------|
| `main` | Código estable / releases |
| `develop` | Rama de integración; base de todas las features |
| `feature/backend-core` | Integrante 1 — entidades, repositorios, servicios, SQL |
| `feature/api-rest-jwt` | Integrante 2 — controllers REST + seguridad JWT |
| `desarrollo-frontend` | Integrante 3 — Angular 17 |
| `feature/integracion-fullstack` | Integrante 3 — integración backend + frontend |
| `feature/devops-railway` | Integrante 4 — Docker, Compose, Railway y documentación |

Flujo de trabajo de una feature:

```bash
git fetch origin
git checkout develop
git pull origin develop
git checkout -b feature/NOMBRE
git push -u origin feature/NOMBRE
```

Después se abre un Pull Request contra `develop`.

Convención de mensajes: [Conventional Commits](https://www.conventionalcommits.org/)
(`feat`, `fix`, `docs`, `chore`, `style`, `refactor`, `test`).

---

## Resolución de problemas

| Síntoma | Causa probable | Solución |
|---------|----------------|----------|
| `Schema-validation: missing table [users]` | La BD está vacía y `ddl-auto=validate` no crea tablas | Cargar `db/schema.sql`. En local: `docker compose down -v && docker compose up -d` |
| Backend en bucle de reinicio en Railway | `SPRING_DATASOURCE_URL` mal formada (se usó `DATABASE_URL`) | Construirla desde las variables `PG*` como se indica en el paso 4 |
| `Blocked by CORS policy` en el navegador | El dominio del frontend no está en `allowedOrigins` | Aplicar la opción A (proxy Nginx) o la opción B |
| `403` en `/api/auth/login` y `Empty encoded password` en los logs | La contraseña del usuario está en texto plano en la BD, no como hash BCrypt | Reinsertar el usuario con el hash. Si es el seed: `docker compose down -v && docker compose up -d` para recargar `db/data.sql` |
| `401` en `/api/wishlist` | Falta o expiró el JWT | Volver a iniciar sesión; revisar `JWT_EXPIRATION_MS` |
| Healthcheck de Railway agotado | El arranque tarda más que `healthcheckTimeout` | Subir `healthcheckTimeout` en `railway.toml` |
| `404` al recargar una ruta de Angular | Falta el fallback de SPA | Ya resuelto por `try_files $uri $uri/ /index.html` en `nginx/default.conf.template` |
| Puerto `5432` ocupado al levantar el compose | Hay un PostgreSQL nativo corriendo | Detenerlo, o cambiar el mapeo a `"5433:5432"` |
| `npm ci` falla en el build del frontend | `package-lock.json` desincronizado con `package.json` | Ejecutar `npm install` en local y commitear el lockfile |
