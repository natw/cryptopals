package main

import (
	"encoding/base64"
	"encoding/hex"

	"github.com/pkg/errors"
)

func Hex2Base64(input string) (string, error) {
	var out string

	bs, err := hex.DecodeString(input)
	if err != nil {
		return out, err
	}
	out = base64.StdEncoding.EncodeToString(bs)
	return out, nil
}

func FixedXOR(a []byte, b []byte) ([]byte, error) {
	result := make([]byte, len(a))
	var err error

	if len(a) != len(b) {
		return result, errors.New("arguments must be same length")
	}

	for i := range a {
		result[i] = a[i] ^ b[i]
	}

	return result, err
}

func SingleXOR(a []byte, b byte) []byte {
	result := make([]byte, len(a))
	for i := range a {
		result[i] = a[i] ^ b
	}
	return result
}
