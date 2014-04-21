package dataprotection.lab.one.block

import org.scalatest.FlatSpec

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 11:02
 */

/**
 * Тестирование класса Block
 */
class BlockSpec extends FlatSpec {

  /** Должен корректно объединить части блока */
  it should "correctly return all parts" in {
    val block = Block(-1, -2)

    val resultBinary = "11111111111111111111111111111111 11111111111111111111111111111110".replace(" ", "")

    assertResult(resultBinary) {
      block.allParts.toBinaryString
    }
  }

  /** Должен корректно поменять части блока местами */
  it should "correctly swap block parts" in {
    val block = Block(-1, -2)
    val swapBlock = block.swap

    assert(swapBlock.leftPart == block.rightPart)
    assert(swapBlock.rightPart == block.leftPart)

    val resultBinary = "11111111111111111111111111111110 11111111111111111111111111111111".replace(" ", "")

    assertResult(resultBinary) {
      swapBlock.allParts.toBinaryString
    }
  }

}
