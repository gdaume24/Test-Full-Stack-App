FROM mysql:8.0.4

# Copier les fichiers de configuration
COPY ./ressources/sql/script.sql /docker-entrypoint-initdb.d/

EXPOSE 3306

