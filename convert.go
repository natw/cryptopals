package main

import (
	"encoding/base64"
	"encoding/hex"
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
