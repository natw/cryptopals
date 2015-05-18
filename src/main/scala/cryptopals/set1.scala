package cryptopals

import scala.collection.JavaConverters._
import scala.collection.GenIterable
import scala.language.implicitConversions
import javax.xml.bind.DatatypeConverter

import scala.reflect.ClassTag

object Set1 {

  abstract class EncodedString(str: String)
  case class HexString(str: String) extends EncodedString(str)
  case class Base64String(str: String) extends EncodedString(str)

  implicit def arrayToList[A](a: Array[A]) = a.toList
  implicit def listToArray[A:ClassTag](l: List[A]) = l.toArray

  implicit def bytes2ascii(input: Array[Byte]): String = { new String(input, "utf-8") }
  implicit def ascii2bytes(input: String): Array[Byte] = { input.getBytes }

  implicit def hex2bytes(input: HexString): Array[Byte] = { DatatypeConverter.parseHexBinary(input.str) }
  implicit def bytes2hex(input: Array[Byte]): HexString = { new HexString(DatatypeConverter.printHexBinary(input)) }
  implicit def hex2ascii(input: HexString): String = { bytes2ascii(hex2bytes(input)) }

  implicit def bytes2base64(input: Array[Byte]): Base64String = { new Base64String(DatatypeConverter.printBase64Binary(input)) }
  implicit def base642bytes(input: Base64String): Array[Byte] = { DatatypeConverter.parseBase64Binary(input.str) }

  // def fixedXOR[T <: GenIterable[Byte]](left: T, right: T): Array[Byte] = {
  def fixedXOR[T <% Iterable[Byte]](left: Array[Byte], right: T): Array[Byte] = {
    left.zip(right).map { case (l:Byte, r:Byte) => (l^r).toByte }.toArray
  }

  // def repeatingXOR[T <% GenIterable[Byte]](ct: T, key: T): Array[Byte] = {
  def repeatingXOR(ct: Array[Byte], key: Array[Byte]): Array[Byte] = {
    def cycle: Stream[Byte] = key.toStream append cycle
    fixedXOR(ct, cycle)
  }

  // def singleByteXOR[T <% GenIterable[Byte]](ciphertext: T, key: Byte): Array[Byte] = {
  def singleByteXOR(ciphertext: Array[Byte], key: Byte): Array[Byte] = {
    repeatingXOR(ciphertext, List(key))
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
