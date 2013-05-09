package code.snippet

import net.liftweb.http.S
import net.liftweb.util.Helpers._

trait BaseSnippet {
  private def pf = S.param("pf").map(_.toString()) openOr "qzone"

  def fusion = {
    "#fusion" #> <script type="text/javascript" charset="utf-8" src={ "http://fusion.qq.com/fusion_loader?appid=100703005&platform=" + pf }/>
  }
}