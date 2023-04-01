FROM nginx:1.23

COPY ./nginx/nginx.conf /etc/nginx/nginx.conf
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
COPY ./ca/localhost.pem /etc/nginx/localhost.pem
COPY ./ca/localhost-key.pem /etc/nginx/localhost-key.pem
COPY ./nginx/index.html /etc/nginx/html/index.html

