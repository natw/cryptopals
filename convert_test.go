package main

import (
	"encoding/hex"
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

// set 1 challenge 2
func TestFixedXOR(t *testing.T) {
	a := "1c0111001f010100061a024b53535009181c"
	abs, err := hex.DecodeString(a)
	require.NoError(t, err)
	b := "686974207468652062756c6c277320657965"
	bbs, err := hex.DecodeString(b)
	require.NoError(t, err)
	c := "746865206b696420646f6e277420706c6179"

	r, err := FixedXOR(abs, bbs)
	require.NoError(t, err)

	require.Equal(t, c, hex.EncodeToString(r))
}
