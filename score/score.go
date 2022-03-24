package score

import (
	"math"
	"unicode"
)

var englishFrequencies = map[rune]float64{
	'a': .082,
	'b': .015,
	'c': .027,
	'd': .047,
	'e': .13,
	'f': .022,
	'g': .02,
	'h': .062,
	'i': .069,
	'j': .0016,
	'k': .0081,
	'l': .04,
	'm': .027,
	'n': .067,
	'o': .078,
	'p': .019,
	'q': .0011,
	'r': .059,
	's': .062,
	't': .096,
	'u': .027,
	'v': .0097,
	'w': .024,
	'x': .0015,
	'y': .02,
	'z': .00078,
}

// Scorers take a piece of potential plaintext and return a number
// representing likelyhood that it's "real"
type Scorer interface {
	Score(string) int
}

type EnglishFreqScore struct{}

func (s *EnglishFreqScore) Score(input string) int {
	if len(input) == 0 {
		return 0
	}

	freqs := CalcLetterFrequencies(input)

	var subtotal float64
	for letter, englishFreq := range englishFrequencies {
		subtotal += math.Abs(englishFreq - freqs[letter])
	}

	// the input having characters way outside ascii should probably count against it
	for _, char := range input {
		if char < 48 || char > 122 {
			subtotal += (float64(char) / 100)
		}
	}

	return int(subtotal * 100)
}

func CalcLetterFrequencies(pt string) map[rune]float64 {
	counts := make(map[rune]int)
	freqs := make(map[rune]float64)
	letterCount := 0

	for _, letter := range pt {
		if unicode.IsLetter(letter) {
			counts[unicode.ToLower(letter)]++
			letterCount++
		}
	}

	totalLetterCount := float64(letterCount)
	for char, count := range counts {
		freqs[char] = float64(count) / totalLetterCount
	}

	return freqs
}
