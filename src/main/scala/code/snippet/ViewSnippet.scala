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
    val item = Item.find(By(Item.id, id))

    item match {
      case Full(it) =>
        S.set("title", it.title.is)
        S.appendJs(JsCmds.Run("document.title=\""+it.title.is+"\";"))
      case _ =>
    }

    <div></div>
  }

}