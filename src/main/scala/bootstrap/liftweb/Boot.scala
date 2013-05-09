package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import net.liftmodules._
import net.liftweb.http.js.jquery._
import net.liftweb.db.DB
import net.liftweb.db.DefaultConnectionIdentifier
import code.model.MyDBVendor
import net.liftweb.mapper.Schemifier
import code.model.TaobaoItem
import code.model.ItemCategory
import net.liftweb.http.provider.HTTPRequest

class Boot {
  def boot {
    LiftRules.addToPackages("code")
    DB.defineConnectionManager(DefaultConnectionIdentifier, MyDBVendor)
    Schemifier.schemify(true, Schemifier.infoF _, TaobaoItem, ItemCategory)

    FoBo.InitParam.JQuery = FoBo.JQuery182
    FoBo.InitParam.ToolKit = FoBo.Bootstrap230
    FoBo.InitParam.ToolKit = FoBo.FontAwesome300
    FoBo.init()

    LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
    LiftRules.early.append(_.setCharacterEncoding("utf-8"))
    LiftRules.htmlProperties.default.set((r: Req) => new StarXHtmlInHtml5OutProperties(r.userAgent))
    /*LiftRules.htmlProperties.default.set((r: Req) => {
      val v = new Html5Properties(r.userAgent)
      val d = v.setContentType(()=> Full("text/html; charset=utf-8"))
      d.setEncoding(() => Full("utf-8"))
      println(d.contentType)
      d
    })*/

    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu

      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
        "Static Content")))

    LiftRules.setSiteMap(SiteMap(entries: _*))

    //TaobaoItem.loadItem2Cache
    //Rewrite
    /*LiftRules.statelessRewrite.append {
      case RewriteRequest(ParsePath("index" :: offset :: cid :: Nil, _, _, _), _, _) =>
        RewriteResponse("index" :: Nil, Map("offset" -> offset, "cid" -> cid))
      case RewriteRequest(ParsePath("index" :: Nil, _, _, _), _, _) =>
        RewriteResponse("index" :: Nil)
    }*/

  }
}
