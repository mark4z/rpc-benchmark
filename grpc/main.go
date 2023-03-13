package main

import (
	context "context"
	"flag"
	"fmt"
	"google.golang.org/grpc"
	"log"
	"net"
)

var addr = flag.String("addr", ":8080", "the address to connect to")

type server struct {
	UnimplementedSimpleServiceServer
}

func (s *server) SimpleRpc(ctx context.Context, in *SimpleRequest) (*SimpleReply, error) {
	log.Println("Request ", "SimpleRpc", "Grpc")
	message := in.Message
	log.Printf("Response: %d bytes written", len(message))
	return &SimpleReply{Message: message}, nil
}

func main() {
	flag.Parse()
	lis, err := net.Listen("tcp", fmt.Sprintf(*addr))
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	RegisterSimpleServiceServer(s, &server{})
	log.Printf("server listening at %v", lis.Addr())
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
