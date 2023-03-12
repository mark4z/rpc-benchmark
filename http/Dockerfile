FROM golang:1.19-alpine AS builder
WORKDIR /app
COPY ./http .
RUN go mod tidy && go build -o /go/bin/http

FROM alpine
WORKDIR /app
COPY --from=builder /go/bin/http .
COPY ./ca/http.local.pem .
COPY ./ca/http.local-key.pem .
RUN chmod +x http && chmod 600 http.local-key.pem && chmod 600 http.local.pem
CMD ["./http"]
