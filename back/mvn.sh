#!/bin/bash

export MYSQL_URL=jdbc:mysql://localhost:3308/test?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8
export MYSQL_USERNAME=user
export MYSQL_ROOT_PASSWORD=root
export MYSQL_PASSWORD=123456

export SECURITY_JWT_SECRET_KEY=openclassrooms
export SECURITY_JWT_EXPIRATION_TIME=864000000



mvn clean install -e
# java -version