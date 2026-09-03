#!/bin/sh
# =============================================================================
#  Arranque del backend en contenedor.
#
#  Render, Heroku, Fly y el propio Railway publican la conexion a PostgreSQL
#  como DATABASE_URL en formato libpq:
#
#      postgresql://usuario:clave@host:puerto/basedatos?sslmode=require
#
#  Spring no entiende ese formato: necesita una URL JDBC y las credenciales
#  por separado. Este script hace la traduccion cuando hace falta, de modo que
#  la misma imagen sirve en cualquiera de esas plataformas sin tocar
#  application.properties.
#
#  Una configuracion explicita siempre gana: si SPRING_DATASOURCE_URL ya viene
#  definida (como en docker-compose.yml), no se toca nada.
# =============================================================================
set -e

if [ -z "${SPRING_DATASOURCE_URL:-}" ] && [ -n "${DATABASE_URL:-}" ]; then
    # Descarta el esquema (postgres:// o postgresql://)
    rest=${DATABASE_URL#*://}

    case "$rest" in
        *@*)
            # Hay credenciales embebidas: usuario[:clave]@resto
            creds=${rest%%@*}
            hostpart=${rest#*@}

            export SPRING_DATASOURCE_USERNAME="${creds%%:*}"
            case "$creds" in
                *:*) export SPRING_DATASOURCE_PASSWORD="${creds#*:}" ;;
            esac
            ;;
        *)
            hostpart=$rest
            ;;
    esac

    # Se conservan los parametros de la query (p. ej. sslmode=require, que
    # Render exige en las conexiones externas): el driver JDBC los acepta.
    export SPRING_DATASOURCE_URL="jdbc:postgresql://${hostpart}"

    echo "[entrypoint] SPRING_DATASOURCE_URL derivada de DATABASE_URL"

    # Estamos en una plataforma con base de datos gestionada, que se entrega
    # vacia. Como el backend arranca con ddl-auto=validate y no crea tablas,
    # se activa aqui la inicializacion por script: asi no depende de que la
    # plataforma haya aplicado estas variables de entorno.
    #
    # cloud-init.sql es idempotente (IF NOT EXISTS / ON CONFLICT DO NOTHING),
    # de modo que repetirlo en cada arranque no destruye ni duplica datos.
    # Una configuracion explicita del operador siempre tiene prioridad.
    if [ -z "${SPRING_SQL_INIT_MODE:-}" ] && [ -f /app/db/cloud-init.sql ]; then
        export SPRING_SQL_INIT_MODE="always"
        : "${SPRING_SQL_INIT_SCHEMA_LOCATIONS:=file:/app/db/cloud-init.sql}"
        export SPRING_SQL_INIT_SCHEMA_LOCATIONS
        echo "[entrypoint] autosiembra activada con /app/db/cloud-init.sql"
    fi
fi

# PORT lo inyecta la plataforma (Render, Railway); 8080 en local.
exec java $JAVA_OPTS -Dserver.port="${PORT:-8080}" -jar /app/app.jar
