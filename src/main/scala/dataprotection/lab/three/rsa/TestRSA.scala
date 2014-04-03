package dataprotection.lab.three.rsa

import dataprotection.lab.three.euclide.Euclide

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 13:21
 */

object TestRSA extends App {

  //testGeneratePQ()
  //testGeneratePublicKey()
  //testGeneratePrivateKey()
  testEncodeDecodeString()
  //testEncodeDecodeNumbers()

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

      val encodeMessage = RSA.encodeString(message, n, publicKey)
      val decodeMessage = RSA.decodeString(encodeMessage, n, privateKey)

      if (message != decodeMessage) {
        System.err.println(s"$message != $decodeMessage")
      }
    }
  }

  def testEncodeDecodeNumbers() {
    //TODO иногда глючит, пока не знаю в чем дело
    for (i <- 1 to 10) {
      val (n, publicKey, privateKey) = RSA.generateKeys()
      val message = Math.round(Math.random() * 10000000 + 1).toString

      val encodeMessage = RSA.encodeNumber(message, n, publicKey)
      val decodeMessage = RSA.decodeNumber(encodeMessage, n, privateKey)

      if (message != decodeMessage) {
        System.err.println(s"$message != $decodeMessage")
      }
    }
  }

}
