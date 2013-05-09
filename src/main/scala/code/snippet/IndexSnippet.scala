package code.snippet

import scala.collection.JavaConversions._
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq
import code.model.ItemCategory
import code.model.TaobaoItem
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

class IndexSnippet extends MyPaginatorSnippet[TaobaoItem] {
  private def cid: Long = S.param("cid").map(_.toLong) openOr 50013693

  private def pf = S.param("pf").map(_.toString()) openOr "qzone"

  override val itemsPerPage = 30
  override def count = TaobaoItem.count(By(TaobaoItem.cid, cid))
  override def page = TaobaoItem.findAll(By(TaobaoItem.cid, cid), By(TaobaoItem.isSpread, true), StartAt(curPage * itemsPerPage), MaxRows(itemsPerPage))
  //override def page = TaobaoItem.itemsCache.get(cid).get.slice(curPage * itemsPerPage, curPage * itemsPerPage + itemsPerPage)
  //override def count = TaobaoItem.itemsCache.get(cid).get.size
  override def pageUrl(offset: Long) = appendParams(super.pageUrl(offset), List("cid" -> cid.toString))

  def taobaoItemCategories(xhtml: NodeSeq) = {
    ItemCategory.getCacheCategories.flatMap(category =>
      bind("category", xhtml,
        "name" -> <a href={ "/index?cid=" + category.cid.get } class={ if (category.cid.get == cid) "selected" else "" }>{ category.name.get }</a>))

  }

  def fusion = {
    "#fusion" #> <script type="text/javascript" charset="utf-8" src={ "http://fusion.qq.com/fusion_loader?appid=100703005&platform=" + pf }/>
  }

  /*def items(xhtml: NodeSeq) = {
    val picSuffix = "_250x250.jpg"
    page.flatMap(item =>
      bind("item", xhtml,
        "title" -> item.title,
        "price" -> item.price,
        "volume" -> item.volume,
        "go" -> <a href={ item.clickUrl.is } target="_blank" class="go"><i class="icon-chevron-right"></i>&nbsp;去看看</a>,
        "image" -> <a href={ item.clickUrl.is } target="_blank"><img class="lazy" src="/images/grey.gif" data-original={ item.picUrl + picSuffix }/></a>))

  }*/

  def items = {
    val picSuffix = "_200x200.jpg"
    ".item" #> page.map(item => {
      "#title" #> item.title.is &
        ".item-img" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;" }><img class="lazy" src="/images/grey.gif" data-original={ item.picUrl + picSuffix }/></a> &
        "#price" #> item.price.is &
        "#volume" #> item.volume.is &
        ".go" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;"} class="go"><i class="icon-chevron-right"></i>&nbsp;去看看</a> 
    })

  }

  def siteUrl = {
    "#siteUrl *" #> <a href={ "http://www.52bigo.com/?pf=" + pf }>爱比购</a>
  }

}