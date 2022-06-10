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

type LetterRatio struct{}

func (s *LetterRatio) Score(input string) int {
	letterCount := float32(0.1)
	otherCount := float32(1)
	for _, c := range input {
		if s.isLetter(c) {
			letterCount++
		} else {
			otherCount++
		}
	}
	return -int((letterCount / otherCount) * 100)
}

func (s *LetterRatio) isLetter(c rune) bool {
	return c == ' ' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
}

type EnglishFreqScore2 struct{}

func (s *EnglishFreqScore2) Score(input string) int {
	freqs := CalcLetterFrequencies(input)

	candidates := make(map[rune]bool)

	for k := range englishFrequencies {
		candidates[k] = true
	}
	for k := range freqs {
		candidates[k] = true
	}

	distanceTotal := 0.0
	for cand := range candidates {
		englishFreq := englishFrequencies[cand]
		ctFreq := freqs[cand]
		distance := math.Abs(englishFreq - ctFreq)
		distanceTotal += distance
	}
	distanceDistance := distanceTotal / float64(len(candidates))
	return int(distanceDistance * 100)
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

// func OrderByScore(lines []string, scorer Scorer) []string {
// }

type ScorableStrings struct {
	Scorer  Scorer
	Strings []string
}

func (s ScorableStrings) Len() int {
	return len(s.Strings)
}

func (s ScorableStrings) Less(i int, j int) bool {
	return s.Scorer.Score(s.Strings[i]) < s.Scorer.Score(s.Strings[j])
}

func (s ScorableStrings) Swap(i, j int) {
	s.Strings[i], s.Strings[j] = s.Strings[j], s.Strings[i]
}
