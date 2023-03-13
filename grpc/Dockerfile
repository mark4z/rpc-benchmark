FROM golang:1.19-alpine AS builder
WORKDIR /app
COPY ./grpc .
RUN go mod tidy && go build -o /go/bin/grpc

FROM alpine
WORKDIR /app
COPY --from=builder /go/bin/grpc .
CMD ["./grpc"]
