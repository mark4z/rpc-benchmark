package main

import (
	httptls "crypto/tls"
	"fmt"
	"golang.org/x/net/http2"
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
	http2.ConfigureTransport(ts)
	//enable http2
	request, err := http.NewRequest("POST", "https://openapi-ft2.amwaynet.com.cn/amway-mock-center-core/v1/api/free/applyForDeletion/mockApi", strings.NewReader("Hello World"))
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
