# syntax=docker/dockerfile:1
# =============================================================================
#  Backend - Lista de Deseos Carvajal
#  Spring Boot 3.3.5 / Java 17 / Maven
#
#  Build multi-stage: la etapa "build" compila el JAR con Maven y la etapa
#  "runtime" publica unicamente un JRE + el JAR (imagen final ~200 MB en vez
#  de ~600 MB si se arrastrara el JDK y el repositorio Maven).
#
#  Se usa el binario `mvn` de la imagen base y NO `./mvnw`, porque
#  .mvn/wrapper/maven-wrapper.jar esta en .gitignore y no llega al contexto.
# =============================================================================

# ---------------------------- Stage 1: build ---------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Capa de dependencias aislada: mientras el pom.xml no cambie, Docker reutiliza
# la cache y no vuelve a descargar el arbol de dependencias.
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

# Codigo fuente y empaquetado. Los tests se omiten en la imagen: se ejecutan en
# el pipeline / en local con `./mvnw test`.
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests

# --------------------------- Stage 2: runtime --------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Usuario sin privilegios: el contenedor no corre como root.
RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /build/target/*.jar /app/app.jar
# El entrypoint traduce DATABASE_URL (formato libpq de Render/Heroku/Fly) a la
# URL JDBC y las credenciales que espera Spring. Ver docker-entrypoint.sh.
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
# Inicializacion idempotente para la nube. Solo se ejecuta si la plataforma
# define SPRING_SQL_INIT_MODE=always (ver render.yaml); en local no se usa.
COPY db/cloud-init.sql /app/db/cloud-init.sql
RUN chmod +x /app/docker-entrypoint.sh && chown -R spring:spring /app
USER spring

# PORT: en local vale 8080; Railway lo inyecta dinamicamente en el arranque.
ENV PORT=8080
# MaxRAMPercentage respeta el limite de memoria del contenedor (Railway).
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

EXPOSE 8080

# El script termina con `exec java`, de modo que la JVM queda como PID 1 y
# recibe SIGTERM para apagarse limpiamente.
ENTRYPOINT ["/app/docker-entrypoint.sh"]
