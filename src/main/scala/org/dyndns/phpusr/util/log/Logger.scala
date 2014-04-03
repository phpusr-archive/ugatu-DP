package org.dyndns.phpusr.util.log

/**
 * @author phpusr
 *         Date: 03.04.14
 *         Time: 14:54
 */

/**
 * Логгер
 */
trait LoggerMixin {
  private val infoEnable = true
  private val debugEnable = true
  private val traceEnable = false

  def title(s: String) { if (infoEnable) println(s"\n----- $s -----") }
  def debug(s: String) { if (debugEnable) println(s"LOG:: $s") }
  def trace(s: String) { if (traceEnable) println(s"TRACE:: $s") }
}

class Logger extends LoggerMixin

object Logger {
  def apply() = new Logger
}
