docker run --rm=true \
           --name postgres-payment \
           -e POSTGRES_USER=app \
           -e POSTGRES_PASSWORD=app \
           -e POSTGRES_DB=db \
           -v ${PWD}/db-postgres/db:/var/lib/postgresql/data \
           -p 5432:5432 \
           postgres:13.3-alpine