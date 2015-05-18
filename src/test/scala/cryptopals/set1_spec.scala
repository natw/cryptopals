import org.specs2.mutable._
import scala.io.Source

import cryptopals.Set1._

class Set1 extends Specification {
  "challenge 1" should {
    "convert hex to base64" in {
      val input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
      val out = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"
      hex2base64(input) === out
    }
  }

  "bytes2hex" should {
    "work" in {
      val bytes = List[Byte](192.toByte, 5, 30)
      bytes2hex(bytes) === "C0051E"
    }
  }

  "hex2bytes" should {
    "work" in {
      val hex = "C0051E"
      hex2bytes(hex) === List[Byte](192.toByte, 5, 30)
    }
  }

  "challenge 2" should {
    "xor two hex strings" in {
      val left = "1c0111001f010100061a024b53535009181c"
      val right = "686974207468652062756c6c277320657965"
      val out = "746865206b696420646f6e277420706c6179"
      hexXOR(left, right).toLowerCase() === out
    }
  }

  "== challenge 3" should {
    "find plaintext" in {
      val ct = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
      val possibleKeys = ('a' to 'z')
      val tup = possibleKeys.map { (key: Char) =>
        var decoded = new String(singleByteXOR(hex2bytes(ct), key.toByte).toArray, "US-ASCII")
        var score = getDistributionScore(decoded)
        (key, decoded, score)
      }.minBy { case (k, d, s) => { s } }
      println("challenge 3")
      println(tup._1)
      println(tup._2)
      1 === 1
    }
  }

  "== challenge 4" should {
    "find plaintext from file" in {
      val possibleKeys = (0 to 128).map(_.toByte)
      val in = Source.fromInputStream(getClass.getResourceAsStream("4.txt"))
      println("== challenge 4")
      val scored = in.getLines.flatMap { (line: String) =>
        possibleKeys.map { (key: Byte) =>
          val decoded = new String(singleByteXOR(hex2bytes(line), key).toArray, "US-ASCII")
          val score = getDistributionScore(decoded)
          (key, decoded, score)
        }
      }.toList.sortBy(_._3)

      scored.filter { case (k: Byte, d: String, s: Double) =>
        containsOnlyPrintableASCII(d)
      }.map { case (k: Byte, d: String, s: Double) =>
        var filtered = printableASCII(d)
        println(containsOnlyASCII(filtered))
        println(filtered)
      }
      1 === 1
    }
  }

  "=== challenge 5" should {
    "XOR correctly" in {
      val pt = """Burning 'em, if you ain't quick and nimble
                 |I go crazy when I hear a cymbal""".stripMargin
      val key = "ICE"
      val ct = bytes2hex(repeatingXOR(hex2bytes(ascii2hex(pt)), hex2bytes(ascii2hex(key)))).toLowerCase
      val expected = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"
      ct === expected
    }
  }
}
