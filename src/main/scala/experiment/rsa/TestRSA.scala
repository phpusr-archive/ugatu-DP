package experiment.rsa

/**
 * @author phpusr
 *         Date: 01.04.14
 *         Time: 14:11
 */
object TestRSA extends App {
  val res = RSA_App.modulPow(2, 10, 1023)
  println(s"res: $res")
}
