#!/usr/bin/env bash


kubectl apply -f ./database/volume.yaml
kubectl apply -f ./database/pvc.yaml
kubectl apply -f ./database/secret.yaml
kubectl apply -f ./database/deployment.yaml
kubectl apply -f ./database/service.yaml

kubectl apply -f ./application-server/deployment.yaml
kubectl apply -f ./application-server/service.yaml
kubectl apply -f ./application-server/ingress.yaml

kubectl apply -f ./streaming-server/deployment.yaml
kubectl apply -f ./streaming-server/service.yaml

kubectl apply -f ./nginx/nginx.yaml

kubectl rollout restart deployment/franklyn-streaming-server
kubectl rollout restart deployment/franklyn-application-server
kubectl rollout restart deployment/nginx
