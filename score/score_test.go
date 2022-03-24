package score

import (
	"testing"

	"github.com/stretchr/testify/require"
)

func TestCalcLetterFrequencies(t *testing.T) {
	ts := "abaebccd"
	freqs := CalcLetterFrequencies(ts)

	require.Equal(t, map[rune]float64{
		'a': .25,
		'b': .25,
		'c': .25,
		'd': .125,
		'e': .125,
	}, freqs)
}

func TestEnglishFreqScore(t *testing.T) {
	sc := &EnglishFreqScore{}

	score1 := sc.Score("hi there, what is up?")
	score2 := sc.Score("asdfouiayspofpasodufiasidufapoisdufopiasudfopiuasdofpuasopdfuiasiopdfu")
	require.Greater(t, score2, score1)
}
