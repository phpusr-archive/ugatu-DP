package org.dyndns.phpusr.util.log

/**
 * @author phpusr
 *         Date: 03.04.14
 *         Time: 14:54
 */

/**
 * Логгер
 */
case class Logger(infoEnable: Boolean, debugEnable: Boolean, traceEnable: Boolean) {
  // Methods logging
  def title(s: String) { if (infoEnable) println(s"\n----- $s -----") }
  def debug(s: String) { if (debugEnable) println(s"LOG:: $s") }
  def trace(s: String) { if (traceEnable) println(s"TRACE:: $s") }
}

/**
 * Ленивое логирование
 */
trait LazyLogging {
  val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)
}
