package main

import (
	"flag"
	"io"
	"log"
	"net/http"
	"strconv"
	"sync/atomic"
	"time"
)

var addr = flag.String("addr", ":8080", "the address to connect to")
var tls = flag.Bool("tls", true, "Connection uses TLS if true, else plain Http")

func main() {
	flag.Parse()
	// q: why addr is not changed?
	// a: because flag.Parse() will parse the flag and change the value of addr
	handle("/", nil)
	handle("/delay", func(body []byte) []byte {
		if delay, err := strconv.Atoi(string(body)); err == nil {
			time.Sleep(time.Duration(delay) * time.Millisecond)
		}
		return []byte{}
	})

	var count int64
	handle("/count", func(body []byte) []byte {
		c := atomic.AddInt64(&count, 1)
		log.Printf("Count: %d", c)
		return nil
	})

	if *tls {
		log.Printf("Listening on %s with TLS", *addr)
		log.Fatal(http.ListenAndServeTLS(*addr, "./localhost.pem", "./localhost-key.pem", nil))
	} else {
		log.Printf("Listening on %s ", *addr)
		log.Fatal(http.ListenAndServe(*addr, nil))
	}
}

func handle(path string, fn func(body []byte) []byte) {
	http.HandleFunc(path, func(w http.ResponseWriter, r *http.Request) {
		log.Printf("Request: %s %s %s", r.Method, r.URL.Path, r.Proto)
		req, err := io.ReadAll(r.Body)
		if err != nil {
			log.Printf("Error reading request body: %v", err)
			return
		}
		defer r.Body.Close()
		res := req
		if fn != nil {
			res = fn(req)
		}
		write, err := w.Write(res)
		if err != nil {
			log.Printf("Error writing response: %v", err)
		}
		log.Printf("Response: %d bytes written", write)
	})
}
