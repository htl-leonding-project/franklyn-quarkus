# Stage 0, "build-stage", based on Node.js, to build and compile the frontend
FROM node:18-alpine as build-stage
WORKDIR /app

COPY package*.json /app/
RUN npm install
COPY . /app/
RUN rm -rf target
ARG configuration=production
RUN npm run build -- --env BASE_HREF="/it190204/"
# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
FROM nginx:stable
#Copy ci-dashboard-dist
COPY --from=build-stage /app/target /usr/share/nginx/html
COPY --from=build-stage /app/styles/ /usr/share/nginx/html/styles
COPY --from=build-stage /app/img/  /usr/share/nginx/html/img
RUN ls /usr/share/nginx/html
#Copy default nginx configuration
COPY default.conf /etc/nginx/conf.d/default.conf

# COPY frontend/* /usr/share/nginx/html/
