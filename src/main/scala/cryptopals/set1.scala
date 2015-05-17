package cryptopals

import scala.collection.JavaConverters._
import scala.language.implicitConversions
import javax.xml.bind.DatatypeConverter

import scala.reflect.ClassTag

object Set1 {

  implicit def arrayToList[A](a: Array[A]) = a.toList
  implicit def listToArray[A:ClassTag](l: List[A]) = l.toArray

  def hex2base64(input: String): String = {
    DatatypeConverter.printBase64Binary(DatatypeConverter.parseHexBinary(input))
  }

  def bytes2hex(input: List[Byte]): String = {
    DatatypeConverter.printHexBinary(input)
  }

  def hex2bytes(input: String): List[Byte] = {
    DatatypeConverter.parseHexBinary(input)
  }

  def hexXOR(left: String, right: String): String = {
    bytes2hex(fixedXOR(hex2bytes(left), hex2bytes(right)))
  }

  def fixedXOR(left: List[Byte], right: List[Byte]): List[Byte] = {
    left.zip(right).map { case (l, r) => (l^r).toByte }
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
    dists.reduce(_ + _) / dists.size
  }

  def distributionForText(text: String): Map[Char, Double] = {
    val ntext = normalize(text).toSet
    ntext.intersect(EnglishLetters).map { letter =>
      (letter, ntext.count(_ == letter).toDouble / ntext.size)
    }.toMap
  }

  def normalize(text: String): String = {
    text.toUpperCase().filter(EnglishLetters contains _)
  }
  // def singleByteXOR(ciphertext: List[Byte], key: Byte): List[Byte] = {
  // }
}
