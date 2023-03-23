FROM golang:1.19-alpine AS builder
WORKDIR /app
COPY ./http .
ENV GOPROXY=https://proxy.golang.com.cn,direct
RUN go mod tidy && go build -o /go/bin/http

FROM alpine
WORKDIR /app
COPY --from=builder /go/bin/http .
COPY ./ca/localhost.pem .
COPY ./ca/localhost-key.pem .
RUN chmod +x http && chmod 600 localhost-key.pem && chmod 600 localhost.pem
CMD ["./http"]
