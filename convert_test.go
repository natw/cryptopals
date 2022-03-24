package main

import (
	"testing"

	"github.com/stretchr/testify/require"
)

// set 1 challenge 1
func TestHex2Base64(t *testing.T) {
	b, err := Hex2Base64("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
	h := "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"
	require.NoError(t, err)
	require.Equal(t, h, b)
}
