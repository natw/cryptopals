package main

import (
	"encoding/hex"
	"fmt"

	"github.com/natw/cryptopals/score"
)

func main() {
	// s1c3
	hexCT := "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
	bs, err := hex.DecodeString(hexCT)
	if err != nil {
		panic(err)
	}

	key, pt, err := FindSingleByteXOR(bs)
	if err != nil {
		panic(err)
	}
	fmt.Printf("key: '%c'\n", key)
	fmt.Printf("plaintext: %q\n", pt)
}

// FindSingleByteXOR takes a hex encoded ciphertext and finds the single byte
// that the plaintext was xor'd with
func FindSingleByteXOR(ct []byte) (byte, string, error) {
	scorer := &score.EnglishFreqScore{}
	topScore := 9999
	bestKey := byte('1')
	pt := ""

	for code := 32; code < 128; code++ {
		key := byte(code)
		maybe := SingleXOR(ct, key)
		score := scorer.Score(string(maybe))

		// fmt.Printf("key of '%c' got score of %d\n", key, score)

		// remember, high score bad
		if score < topScore {
			topScore = score
			bestKey = key
			pt = string(maybe)
		}

	}

	return bestKey, pt, nil
}
