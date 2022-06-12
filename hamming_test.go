package main

import (
	"testing"

	"github.com/stretchr/testify/require"
)

func TestHamming(t *testing.T) {
	t.Run("empty arrays", func(t *testing.T) {
		a := []byte{}
		b := []byte{}
		dist := hamming(a, b)
		require.Equal(t, 0, dist)
	})

	t.Run("strings", func(t *testing.T) {
		a := []byte("this is a test")
		b := []byte("wokka wokka!!!")
		require.Equal(t, 37, hamming(a, b))
	})
}
