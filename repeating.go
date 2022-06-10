package main

func repeatingKeyXOR(plaintext []byte, key []byte) []byte {
	ct := make([]byte, len(plaintext))
	ki := 0
	for i := range plaintext {
		keyByte := key[ki%len(key)]
		ki++
		ct[i] = plaintext[i] ^ keyByte
	}
	return ct
}
