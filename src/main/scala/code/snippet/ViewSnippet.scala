package code.snippet

import scala.collection.JavaConversions._
import scala.xml.NodeSeq.seqToNodeSeq
import code.model.Item
import net.liftweb.common.Box.box2Option
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds.jsExpToJsCmd
import net.liftweb.mapper._
import net.liftweb.util.Helpers._
import net.liftweb.util.Helpers.chooseTemplate
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.Helpers.strToSuperArrowAssoc
import net.liftweb.common.Full
import net.liftweb.common.Empty
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds

class ViewSnippet extends BaseSnippet {
  def viewItem = {
    val id = S.param("id").map(_.toLong) openOr (0L)

    Item.find(By(Item.id, id)) match {
      case Full(item) =>
        val picSuffix = "_400x400.jpg"
        S.set("title", item.title.is)
        S.appendJs(JsCmds.Run("document.title=\"" + item.title.is + "\";"))
        "#b-title" #> item.title.is & 
        ".b-img" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;" }><img class="lazy" src="/images/grey.gif" data-original={ item.picUrl + picSuffix }/></a>
      case _ => <div></div>
    }

    
  }

}