package code.snippet

import scala.collection.JavaConversions._
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq
import net.liftweb.common.Box.box2Option
import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds.jsExpToJsCmd
import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.util.Helpers.chooseTemplate
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.Helpers.strToSuperArrowAssoc
import scala.xml.Text
import code.model.Item

class YySnippet extends MyPaginatorSnippet[Item] {
  private def itemType: Int = S.param("itemType").map(_.toInt) openOr 0

  override val itemsPerPage = 30
  override def count = Item.count()
  override def page = Item.findAll(StartAt(curPage * itemsPerPage), MaxRows(itemsPerPage))
  override def pageUrl(offset: Long) = appendParams(super.pageUrl(offset), List("itemType" -> itemType.toString))

  def items = {
    val picSuffix = "_200x200.jpg"
    ".item" #> page.map(item => {
      "#title" #> item.title.is &
        ".item-img" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;" }><img class="lazy" src="/images/grey.gif" data-original={ item.picUrl + picSuffix }/></a> &
        ".go" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;" } class="go"><i class="icon-chevron-right"></i>&nbsp;去看看</a>
    })

  }

}