package code.snippet

import scala.xml.NodeSeq
import scala.xml.Text

import net.liftweb.http.PaginatorSnippet
import net.liftweb.http.S._
import net.liftweb.util.Helpers._

trait MyPaginatorSnippet[T] extends PaginatorSnippet[T] {
  /*override def prevXml: NodeSeq = if (curPage == 0) { <span class="current  prev">上一页</span> } else { Text("上一页") }
  override def nextXml: NodeSeq = if (curPage == 0) { Text("下一页") } else { <span class="next">下一页</span> }
  override def firstXml: NodeSeq = Text("第一页")
  override def lastXml: NodeSeq = Text("最后一页")*/

  override def prevXml: NodeSeq = Text("上一页")
  override def nextXml: NodeSeq = Text("下一页")
  override def firstXml: NodeSeq = Text("首页")
  override def lastXml: NodeSeq = Text("尾页")

  override def pageXml(newFirst: Long, ns: NodeSeq): NodeSeq =
    if (first == newFirst || newFirst < 0 || newFirst >= count)
      <span class="current">{ ns }</span>
    else
      <a href={ pageUrl(newFirst) } class="page-link">{ ns }</a>

  override def pagesXml(pages: Seq[Int], sep: NodeSeq): NodeSeq =
    pages.toList map { n =>
      pageXml(n * itemsPerPage, Text(n + 1 toString))
    } match {
      case one :: Nil => one
      case first :: rest => rest.foldLeft(first) {
        case (a, b) => a ++ sep ++ b
      }
      case Nil => Nil
    }
}