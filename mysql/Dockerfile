#!/bin/bash
FROM mysql:5.7
ENV IMAGE_NAME db:latest
COPY ./key.sql /docker-entrypoint-initdb.d/

EXPOSE 3306
