package main

import (
	"math/bits"
)

func hamming(a []byte, b []byte) int {
	total := 0
	for i := range a {
		x := a[i] ^ b[i]
		total += bits.OnesCount(uint(x))
	}
	return total
}
