#!/usr/bin/env bash

set -e

rm -rf target
mvn -B clean package
mkdir -p target/deploy
cp target/quarkus-app/quarkus-run.jar target/deploy/franklyn-application-server-image.jar
docker build --tag franklyn-application-server-image --file ./src/main/docker/Dockerfile ./target/deploy