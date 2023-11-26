#!/usr/bin/env bash

set -e

rm -rf target
mvn -B clean package
mkdir -p target/deploy
cp target/*-runner.jar target/deploy/
docker build --tag franklyn-application-server-image --file ./src/main/docker/Dockerfile ./target/deploy