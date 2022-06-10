package main

import (
	"bufio"
	"encoding/hex"
	"fmt"
	"os"

	"github.com/natw/cryptopals/score"
)

func main() {
	// s1c3()
	// s1c4()
	s1c5()
}

func s1c5() {
	pt := "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal"

	ct := repeatingKeyXOR([]byte(pt), []byte("ICE"))
	fmt.Println(hex.EncodeToString(ct))
}

func s1c4() {
	file, err := os.Open("4.txt")
	if err != nil {
		panic(err)
	}
	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)
	scorer := &score.LetterRatio{}
	bestScore := 9999
	bestPt := ""
	var bestKey byte
	for scanner.Scan() {
		line := scanner.Text()
		ctBytes, err := hex.DecodeString(line)
		if err != nil {
			panic(err)
		}

		key, pt, err := FindSingleByteXOR(scorer, ctBytes)
		score := scorer.Score(string(ctBytes))

		if score < bestScore {
			bestScore = score
			bestPt = pt
			bestKey = key
		}

		if err != nil {
			panic(err)
		}
	}
	fmt.Printf("best\nkey: %c\npt: %q", bestKey, bestPt)
}

func s1c3() {
	hexCT := "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
	bs, err := hex.DecodeString(hexCT)
	if err != nil {
		panic(err)
	}

	// scorer := &score.EnglishFreqScore{}
	scorer := &score.LetterRatio{}
	key, pt, err := FindSingleByteXOR(scorer, bs)
	if err != nil {
		panic(err)
	}
	fmt.Println("")
	fmt.Printf("key: '%c'\n", key)
	fmt.Printf(" pt: %q\n\n", pt)
}

// FindSingleByteXOR takes a hex encoded ciphertext and finds the single byte
// that the plaintext was xor'd with
func FindSingleByteXOR(scorer score.Scorer, ct []byte) (byte, string, error) {
	topScore := 9999
	bestKey := byte('1')
	pt := ""

	for code := 0; code < 256; code++ {
		key := byte(code)
		maybe := SingleXOR(ct, key)
		score := scorer.Score(string(maybe))

		// fmt.Printf(" ct: %q\nkey: '%c'\n pt: %q\n sc: %d\n\n", hex.EncodeToString(ct), key, maybe, score)

		// remember, high score bad
		if score < topScore {
			topScore = score
			bestKey = key
			pt = string(maybe)
		}

	}

	return bestKey, pt, nil
}
