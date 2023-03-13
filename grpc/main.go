package main

import (
	"flag"
	"io"
	"log"
	"net/http"
)

var addr = flag.String("addr", ":8080", "the address to connect to")
var tls = flag.Bool("tls", true, "Connection uses TLS if true, else plain Http")

func main() {
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		log.Printf("Request: %s %s %s", r.Method, r.URL.Path, r.Proto)
		req, err := io.ReadAll(r.Body)
		if err != nil {
			log.Printf("Error reading request body: %v", err)
		}
		write, err := w.Write(req)
		if err != nil {
			log.Printf("Error writing response: %v", err)
		}
		log.Printf("Response: %d bytes written", write)
	})
	if *tls {
		log.Printf("Listening on %s with TLS", *addr)
		log.Fatal(http.ListenAndServeTLS(*addr, "./http.local.pem", "./http.local-key.pem", nil))
	} else {
		log.Printf("Listening on %s ", *addr)
		log.Fatal(http.ListenAndServe(*addr, nil))
	}
}
