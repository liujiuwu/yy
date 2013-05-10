package bootstrap.liftweb

import code.model.Item
import code.model.MyDBVendor
import net.liftmodules._
import net.liftweb._
import net.liftweb.common._
import net.liftweb.db.DB
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.http._
import net.liftweb.http.provider.HTTPRequest
import net.liftweb.mapper.Schemifier
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import code.model.ItemType

class Boot {
  def boot {
    LiftRules.addToPackages("code")
    DB.defineConnectionManager(DefaultConnectionIdentifier, MyDBVendor)
    Schemifier.schemify(true, Schemifier.infoF _, Item)

    FoBo.InitParam.JQuery = FoBo.JQuery182
    FoBo.InitParam.ToolKit = FoBo.Bootstrap230
    FoBo.InitParam.ToolKit = FoBo.FontAwesome300
    FoBo.init()

    LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
    LiftRules.early.append(_.setCharacterEncoding("utf-8"))
    LiftRules.htmlProperties.default.set((r: Req) => new StarXHtmlInHtml5OutProperties(r.userAgent))

    LiftRules.setSiteMapFunc(() => MenuInfo.sitemap)

    /*LiftRules.statelessRewrite.prepend(NamedPF("YyRewrite") {
      case RewriteRequest(
        ParsePath("index" :: itemType :: Nil, _, _, _), _, _) =>
        RewriteResponse(
          "index" :: Nil, Map("itemType" -> itemType)
          )
    })*/
  }
}

object MenuInfo {
  import Loc._
  import scala.xml._

  val menus = List(
    Menu("首页") / "index" >> LocGroup("main"),
    Menu("美女") / "meinv" / ** >> LocGroup("main"),
    Menu("帅哥") / "shuaige" / ** >> LocGroup("main"),
    Menu("可爱") / "keai" / ** >> LocGroup("main"),
    Menu("查看") / "view" / ** >> LocGroup("main") >> Hidden)

  def sitemap() = SiteMap(menus: _*)
}
