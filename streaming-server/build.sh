#!/usr/bin/env bash

set -e

rm -rf target
mvn -B clean package -DskipTests=true
mkdir -p target/deploy
cp ./target/*-runner.jar target/deploy/
docker build --tag franklyn-streaming-server-image --file ./src/main/docker/Dockerfile ./target/deploy