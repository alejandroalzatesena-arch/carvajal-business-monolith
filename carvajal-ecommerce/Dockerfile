# syntax=docker/dockerfile:1
# =============================================================================
#  Frontend - Carvajal E-commerce (Angular 17) servido con Nginx
#
#  Build multi-stage: la etapa "build" compila con Node y la etapa "runtime"
#  publica solo los estaticos sobre nginx:alpine (~50 MB). Node no viaja a
#  produccion.
# =============================================================================

# ---------------------------- Stage 1: build ---------------------------------
FROM node:20-alpine AS build
WORKDIR /app

# Capa de dependencias aislada: se reutiliza mientras no cambie el lockfile.
COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# La configuracion "production" aplica el fileReplacements de angular.json:
# environment.ts -> environment.prod.ts
RUN npm run build -- --configuration production

# --------------------------- Stage 2: runtime --------------------------------
FROM nginx:1.27-alpine AS runtime

# Angular 17 usa el builder "application": el bundle sale en
# dist/<proyecto>/browser, no directamente en dist/<proyecto>.
COPY --from=build /app/dist/carvajal-ecommerce/browser /usr/share/nginx/html

# El entrypoint de la imagen procesa esta plantilla con envsubst al arrancar.
COPY nginx/default.conf.template /etc/nginx/templates/default.conf.template

# Antepone el esquema a BACKEND_URL cuando la plataforma solo entrega el host
# (caso de Render). El prefijo 15 lo situa antes del paso de envsubst (20).
COPY nginx/15-normalize-backend-url.envsh /docker-entrypoint.d/15-normalize-backend-url.envsh
RUN chmod +x /docker-entrypoint.d/15-normalize-backend-url.envsh

# Opt-in para que el entrypoint exporte NGINX_LOCAL_RESOLVERS leyendo los
# nameservers de /etc/resolv.conf (sin esto, la variable no se define).
ENV NGINX_ENTRYPOINT_LOCAL_RESOLVERS=1

# Solo se sustituyen estas tres: asi $uri, $host, $upstream, etc. sobreviven.
ENV NGINX_ENVSUBST_FILTER="^(PORT|BACKEND_URL|NGINX_LOCAL_RESOLVERS)"

# PORT: 80 en local; Railway lo sobreescribe en el arranque.
ENV PORT=80
# Destino del proxy /api. En docker-compose apunta al servicio "backend";
# en Railway se define como variable del servicio (ver README).
ENV BACKEND_URL="http://backend:8080"

EXPOSE 80
