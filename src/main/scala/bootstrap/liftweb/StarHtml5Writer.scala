/*
    // Setting this in bootstrap.liftweb.Boot.scala
    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new StarXHtmlInHtml5OutProperties(r.userAgent))
*/

package bootstrap.liftweb

import java.io.InputStream
import java.io.Writer

import scala.xml.MetaData
import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq
import scala.xml.Null
import scala.xml.PrefixedAttribute
import scala.xml.UnprefixedAttribute

import net.liftweb.common.Box
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.http.HtmlProperties
import net.liftweb.http.LiftRules
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.Req
import net.liftweb.http.S
import net.liftweb.util.Html5Writer
import net.liftweb.util.PCDataXmlParser

case class StarXHtmlInHtml5OutProperties(userAgent: Box[String]) extends HtmlProperties {
  def docType: Box[String] = Full("<!DOCTYPE html>")
  def encoding: Box[String] = Empty

  def contentType: Box[String] = {
    Full("text/html; charset=utf-8")
  }

  def htmlParser: InputStream => Box[NodeSeq] = PCDataXmlParser.apply _

  def htmlWriter: (Node, Writer) => Unit = {
    StarHtml5Writer.write(_, _, false, !LiftRules.convertToEntity.vend)
  }

  def htmlOutputHeader: Box[String] = docType.map(_ + "\n")

  val html5FormsSupport: Boolean = {
    val r = S.request openOr Req.nil
    r.isSafari5 || r.isFirefox36 || r.isFirefox40 ||
      r.isChrome5 || r.isChrome6
  }

  val maxOpenRequests: Int =
    LiftRules.maxConcurrentRequests.vend(S.request openOr Req.nil)
}

object StarHtml5Writer extends Html5Writer {
  /**
   * Write the attributes in HTML5 valid format
   * @param m the attributes
   * @param writer the place to write the attribute
   */
  override def writeAttributes(m: MetaData, writer: Writer) {
    m match {
      case null =>
      case Null =>
      case md if (null eq md.value) => writeAttributes(md.next, writer)
      case up: UnprefixedAttribute => {
        writer.append(' ')
        writer.append(up.key)
        val v = up.value
        writer.append("=\"")
        val str = v.text
        var pos = 0
        val len = str.length
        while (pos < len) {
          str.charAt(pos) match {
            case '"' => writer.append("&quot;")
            case '<' => writer.append("&lt;")
            case c if c >= ' ' && c.toInt <= 127 => writer.append(c)
            case c if c == '\u0085' =>
            case c => {
              //              val str = Integer.toHexString(c)
              //              writer.append("&#x")
              //              writer.append("0000".substring(str.length))
              //              writer.append(str)
              //              writer.append(';')
              writer.append(c)
            }
          }

          pos += 1
        }

        writer.append('"')

        writeAttributes(up.next, writer)
      }

      case pa: PrefixedAttribute => {
        writer.append(' ')
        writer.append(pa.pre)
        writer.append(':')
        writer.append(pa.key)
        val v = pa.value
        if ((v ne null) && !v.isEmpty) {
          writer.append("=\"")
          val str = v.text
          var pos = 0
          val len = str.length
          while (pos < len) {
            str.charAt(pos) match {
              case '"' => writer.append("&quot;")
              case '<' => writer.append("&lt;")
              case c if c >= ' ' && c.toInt <= 127 => writer.append(c)
              case c if c == '\u0085' =>
              case c => {
                //                val str = Integer.toHexString(c)
                //                writer.append("&#x")
                //                writer.append("0000".substring(str.length))
                //                writer.append(str)
                //                writer.append(';')
                writer.append(c)
              }
            }

            pos += 1
          }

          writer.append('"')
        }

        writeAttributes(pa.next, writer)
      }

      case x => writeAttributes(x.next, writer)
    }
  }

}