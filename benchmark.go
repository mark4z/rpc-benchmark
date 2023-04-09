package main

import (
	"context"
	"crypto/tls"
	"fmt"
	"io"
	"net/http"
	"net/http/httptrace"
	"strings"
	"sync"
	"sync/atomic"
	"time"
)

var count int64
var req int64

func main() {
	client := &http.Client{
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{
				InsecureSkipVerify: true,
			},
			MaxConnsPerHost:     100,
			MaxIdleConnsPerHost: 100,
		},
	}

	start := time.Now()
	wg := sync.WaitGroup{}
	for i := 0; i < 100; i++ {
		wg.Add(1)
		go func() {
			for j := 0; j < 2000; j++ {
				process(client)
			}
			wg.Done()
		}()
	}
	go func() {
		time.Sleep(1 * time.Second)
		for {
			select {
			case <-time.After(200 * time.Millisecond):
				fmt.Printf("\r conn: %d, req: %d, qps: %v",
					atomic.LoadInt64(&count), atomic.LoadInt64(&req), float64(atomic.LoadInt64(&req))/time.Since(start).Seconds())
			}
		}
	}()

	wg.Wait()
	fmt.Println()
	fmt.Printf("\r conn: %d, req: %d, qps: %v",
		atomic.LoadInt64(&count), atomic.LoadInt64(&req), float64(atomic.LoadInt64(&req))/time.Since(start).Seconds())
}

func process(client *http.Client) {
	atomic.AddInt64(&req, 1)
	trace := &httptrace.ClientTrace{
		ConnectStart: func(network, addr string) {
			atomic.AddInt64(&count, 1)
		},
	}
	ctx := httptrace.WithClientTrace(context.Background(), trace)
	//ctx, cancel := context.WithCancel(ctx)
	//defer cancel()

	request, _ := http.NewRequestWithContext(ctx, "POST", "https://127.0.0.1/delay", strings.NewReader("1"))
	do, err := client.Do(request)
	if err != nil {
		fmt.Println(err)
		return
	}
	io.ReadAll(do.Body)
	do.Body.Close()
}
