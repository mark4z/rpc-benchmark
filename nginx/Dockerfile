FROM nginx:1.23

ENV MAX_REQUESTS_PER_CONNECTION 1000

COPY ./nginx/nginx.conf /etc/nginx/nginx.conf
COPY ./nginx/default.conf /tmp/default-temp.conf
COPY ./ca/localhost.pem /etc/nginx/localhost.pem
COPY ./ca/localhost-key.pem /etc/nginx/localhost-key.pem
COPY ./nginx/index.html /etc/nginx/html/index.html

RUN cat /tmp/default-temp.conf | sed "s/keepalive_requests 1000/keepalive_requests $MAX_REQUESTS_PER_CONNECTION/g" > /etc/nginx/conf.d/default.conf

