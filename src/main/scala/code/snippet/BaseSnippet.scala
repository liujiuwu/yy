package code.snippet

import java.util.HashMap
import com.qq.open.OpenApiV3
import net.liftweb.http.S
import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers._
import code.model.User
import net.liftweb.common.Full
import scala.util.Random
import net.liftweb.mapper.By
import code.model.Item

trait BaseSnippet {
  def pf = S.param("pf").map(_.toString()) openOr "qzone"
  def openid = {
    User.currentUser match {
      case Full(user) => user.openId.is
      case _ => S.param("openid").map(_.toString()) openOr "openid"
    }
  }

  def openkey = {
    val k = S.param("openkey").map(_.toString()) openOr "openkey"
    if (k == "" || k == "openkey") {
      User.currentUser match {
        case Full(user) => user.openKey.is
        case _ => S.param("openkey").map(_.toString()) openOr "openkey"
      }
    } else {
      k
    }
  }

  val appid = "100703005"
  val appkey = "a7516cd28bfbfb6738d762a2a86f9741"
  val serverName = "119.147.19.43"
  val sdk = new OpenApiV3(appid, appkey)
  sdk.setServerName(serverName)
  val protocol = "http"

  def fusion = {
    var isLogin = false

    User.currentUser match {
      case Full(user) => isLogin = true
      case _ =>
        if (openid != "" && "openid" != openid) {
          val userInfo = getUserInfo
          if (userInfo.\("ret") == JInt(0)) {
            isLogin = true
            User.find(By(User.openId, openid)) match {
              case Full(user) => User.logUserIn(user)
              case _ => {
                val user = new User
                user.openId(openid)
                user.openKey(openkey)
                userInfo.\("nickname") match {
                  case JString(name) => user.name(name)
                  case _ => user.name("秀色")
                }
                user.password("123456")
                user.save
              }
            }
          }
        }
    }

    "#fusion" #> <script type="text/javascript" charset="utf-8" src={ "http://fusion.qq.com/fusion_loader?appid=100703005&platform=" + pf }/> <script>{ if (!isLogin) "fusion2.dialog.relogin();" }</script>
  }

  /*private def isLogin: Boolean = {
    if (openid == "" || "openid" == openid) {
      return false
    }

    val scriptName = "/v3/user/is_login";
    val params = new HashMap[String, String]()
    params.put("openid", openid)
    params.put("openkey", openkey)
    params.put("pf", pf)

    val resp = sdk.api(scriptName, params, protocol)
    val jv = parse(resp)
    jv.\("ret") == JInt(0)
  }*/

  private def getUserInfo = {
    val scriptName = "/v3/user/get_info";
    val params = new HashMap[String, String]()
    params.put("openid", openid)
    params.put("openkey", openkey)
    params.put("pf", pf)

    val resp = sdk.api(scriptName, params, protocol)
    parse(resp)
  }
  
  def renderItems(items: List[Item]) = {
    val picSuffix = "_200x200.jpg"
    ".item" #> items.map(item => {
      ".title" #> item.title.is &
        ".heart-value" #> item.likeCount.is &
        ".item-img" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;" }><img class="lazy" src="/images/grey.gif" data-original={ item.picUrl + picSuffix }/></a> &
        ".go" #> <a href={ "/view?id=" + item.id } class="go"><i class="icon-chevron-right"></i>&nbsp;去看看</a>
    })
  }
}