package dataprotection.lab.three.rsa

import dataprotection.lab.three.euclide.Euclide
import scala.util.Random
import org.scalatest.FlatSpec
import dataprotection.lab.three.types.RsaType

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 13:21
 */

/**
 * Тестирование класса RSA
 */
class RSA_Spec extends FlatSpec {

  "RSA" should "generate p & q without exception" in {
    for (i <- 1 to 10)
      println(RSA.generatePQ(100))
  }

  it should "generate normal p, q and Public Key" in {
    for (i <- 1 to 10) {
      val (p, q) = RSA.generatePQ(100)
      val publicKey = RSA.generatePublicKey(p, q, 100)
      println(s"publicKey: $publicKey")

      assert(Euclide.gcd(publicKey, (p-1)*(q-1)) == RsaType.One)
    }
  }

  it should "generate Private Key without exception" in {
    val (p, q, e) = (7, 11, 23)
    val privateKey = RSA.generatePrivateKey(p, q, e)
    println(s"privateKey: $privateKey")
  }

  it should "encode and decode message as string" in {
    for (i <- 1 to 10) {
      val (p, q, n, publicKey, privateKey) = RSA.generateKeys()
      val message = Random.nextString(10)

      val encodeMessage = RSA.encryptString(message, n, publicKey)
      val decodeMessage = RSA.decryptString(encodeMessage, n, privateKey)

      assert(message == decodeMessage)
    }
  }

  it should "encode and decode message as number" in {
    for (i <- 1 to 10) {
      val (p, q, n, publicKey, privateKey) = RSA.generateKeys()
      val message = Random.nextInt(10000000).toString

      val encodeMessage = RSA.encryptNumber(message, n, publicKey)
      val decodeMessage = RSA.decryptNumber(encodeMessage, n, privateKey)

      assert(message == decodeMessage)
    }
  }

}
