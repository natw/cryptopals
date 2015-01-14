import org.specs2.mutable._

import cryptopals._

class Set1 extends Specification {
  "challenge 1" should {
    "convert hex to base64" in {
      val input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
      val out = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"
      Set1.hex2base64(input) === out
    }
  }

  "bytes2hex" should {
    "work" in {
      val bytes = List[Byte](192.toByte, 5, 30)
      Set1.bytes2hex(bytes) === "C0051E"
    }
  }

  "hex2bytes" should {
    "work" in {
      val hex = "C0051E"
      Set1.hex2bytes(hex) === List[Byte](192.toByte, 5, 30)
    }
  }

  "challenge 2" should {
    "xor two hex strings" in {
      val left = "1c0111001f010100061a024b53535009181c"
      val right = "686974207468652062756c6c277320657965"
      val out = "746865206b696420646f6e277420706c6179"
      Set1.hexXOR(left, right).toLowerCase() === out
    }
  }
}
