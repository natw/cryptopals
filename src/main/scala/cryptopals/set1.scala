package cryptopals

import scala.collection.JavaConverters._
import scala.collection.GenSeq
import scala.language.implicitConversions
import javax.xml.bind.DatatypeConverter

import scala.reflect.ClassTag

object Set1 {

  implicit def arrayToList[A](a: Array[A]) = a.toList
  implicit def listToArray[A:ClassTag](l: List[A]) = l.toArray

  def bytes2hex(input: List[Byte]): String = {
    DatatypeConverter.printHexBinary(input)
  }

  def hex2bytes(input: String): List[Byte] = {
    DatatypeConverter.parseHexBinary(input)
  }

  def hex2base64(input: String): String = {
    DatatypeConverter.printBase64Binary(DatatypeConverter.parseHexBinary(input))
  }

  def hex2ASCII(input: String): String = {
    DatatypeConverter.parseHexBinary(input).toString
  }

  def hexXOR(l: String, r: String): String = bytes2hex(fixedXOR(hex2bytes(l), hex2bytes(r)))


  def fixedXOR[T <% Iterable[Byte]](left: T, right: T): List[Byte] = {
    left.zip(right).map { case (l, r) => (l^r).toByte }.toList
  }

  def singleByteXOR(ciphertext: List[Byte], key: Byte): List[Byte] = {
    fixedXOR(ciphertext, Iterator.continually(key).toIterable)
  }

  val EnglishLetterDistribution: Map[Char, Double] = Map(
    'E' -> .1249,
    'T' -> .0928,
    'A' -> .0804,
    'O' -> .0764,
    'I' -> .0757,
    'N' -> .0723,
    'S' -> .0651,
    'R' -> .0628,
    'H' -> .0505,
    'L' -> .0407,
    'D' -> .0382,
    'C' -> .0334,
    'U' -> .0273,
    'M' -> .0251,
    'F' -> .0240,
    'P' -> .0214,
    'G' -> .0187,
    'W' -> .0168,
    'Y' -> .0166,
    'B' -> .0148,
    'V' -> .0105,
    'K' -> .0054,
    'X' -> .0023,
    'J' -> .0016,
    'Q' -> .0012,
    'Z' -> .0009
  )

  val EnglishLetters = EnglishLetterDistribution.keySet


  def getDistributionScore(text: String): Double = {
    distanceFromEnglish(distributionForText(text))
  }

  // just average of difference of all distributions for now
  // lower is better
  def distanceFromEnglish(distribution: Map[Char, Double]): Double = {
    val dists = distribution.map { case (letter, dist) => 
      EnglishLetterDistribution.get(letter).map { (engDist: Double) => (dist - engDist).abs }.get
    }
    if (dists.isEmpty) { return 12.0 }
    dists.reduce(_ + _) / dists.size
  }

  def distributionForText(text: String): Map[Char, Double] = {
    val ntext: Set[Char] = normalizeInput(text).toSet
    ntext.intersect(EnglishLetters).map { letter =>
      (letter, ntext.count(_ == letter).toDouble / ntext.size)
    }.toMap
  }

  def normalizeInput(text: String): String = {
    text.toUpperCase().filter(EnglishLetters contains _)
  }

  def printableASCII(text: String): String = {
    text.filter { (c: Char) =>
      var i = c.toInt
      i > 31 && i < 127
    }
  }

  def containsOnlyPrintableASCII(text: String): Boolean = {
    text == printableASCII(text)
  }

  def onlyASCII(text: String): String = text.filter(_.toInt < 128)

  def containsOnlyASCII(text: String): Boolean = {
    text == onlyASCII(text)
  }

}
