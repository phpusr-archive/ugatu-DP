package rsa

import euclide.Euclide

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 13:21
 */

object TestRSA extends App {

  //testGeneratePQ()
  //testGeneratePublicKey()
  //testGeneratePrivateKey()
  //testEncodeDecodeString()
  testEncodeDecodeNumbers()

  def testGeneratePQ() {
    println(RSA.generatePQ(100))
    println(RSA.generatePQ(100))
    println(RSA.generatePQ(100))
  }

  def testGeneratePublicKey() {
    for (i <- 1 to 10) {
      val (p, q) = RSA.generatePQ(100)
      val publicKey = RSA.generatePublicKey(p, q, 100)
      println(s"publicKey: $publicKey")
      assert(Euclide.gcd(publicKey, (p-1)*(q-1)) == 1)
    }
  }

  def testGeneratePrivateKey() {
    val (p, q, e) = (7, 11, 23)
    val privateKey = RSA.generatePrivateKey(p, q, e)
    println("privateKey = " + privateKey)
  }

  def testEncodeDecodeString() {
    //TODO иногда глючит, пока не знаю в чем дело
    for (i <- 1 to 10) {
      val (n, publicKey, privateKey) = RSA.generateKeys()
      val message = "Hello"

      val encodeString = RSA.encode(message, n, publicKey)
      println(s"encodeString: $encodeString")
      val decodeString = RSA.decode(encodeString, n, privateKey)
      println(s"decodeString: $decodeString")
      println()

      assert(message == decodeString)
    }
  }

  def testEncodeDecodeNumbers() {
    val (n, publicKey, privateKey) = RSA.generateKeys()
    val message = 123456

    val encode = RSA.encode(message, n, publicKey)
    println(s"encode: $encode")
  }

}
