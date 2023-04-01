package main

import (
	httptls "crypto/tls"
	"fmt"
	"io"
	"net/http"
	"strings"
	"testing"
)

func TestReq(t *testing.T) {
	ts := &http.Transport{
		TLSClientConfig: &httptls.Config{
			InsecureSkipVerify: true,
		},
	}
	client := http.Client{
		Transport: ts,
	}
	//if err := http2.ConfigureTransport(ts); err != nil {
	//	t.Fatal(err)
	//}
	//enable http2
	request, err := http.NewRequest("POST", "https://localhost:8080", strings.NewReader("Hello World"))
	if err != nil {
		t.Fatal(err)
	}
	response, err := client.Do(request)
	if err != nil {
		t.Fatal(err)
	}
	if response.StatusCode != http.StatusOK {
		t.Fatalf("Expected status code %d, got %d", http.StatusOK, response.StatusCode)
	}
	bytes, err := io.ReadAll(response.Body)
	if err != nil {
		t.Fatal(err)
	}
	defer response.Body.Close()
	fmt.Println(string(bytes))
}
