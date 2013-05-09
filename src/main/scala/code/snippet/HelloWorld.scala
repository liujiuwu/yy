package code
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import net.liftweb.http._
import net.liftweb.http.js._
import JsCmds._
import JE._

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> date.map( "再试试，===保存就好了吗？原来是这样哦，不错，现在是:" +_.toString+" 这就是爱在心")

  /*
   lazy val date: Date = DependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> "现在是:"+date.toString
   */
  
  def ajaxFunc() : JsCmd = {
    JsCrVar("myObject", JsObj(("persons", JsArray(
        JsObj(("name", "Thor"), ("race", "Asgard")),
        JsObj(("name", "Todd"), ("race", "Wraith")),
        JsObj(("name", "Rodney"), ("race", "Human"))
    )))) & JsRaw("alert(myObject.persons[0].name)")
  }
  
  def renderAjaxButton(xhtml: NodeSeq): NodeSeq = {
    bind("ex", xhtml,"button" -> SHtml.ajaxButton(Text("Press me"), ajaxFunc _))
  }

  def tt = {
	"#tt" #> <meta name="description" content="浓浓的爱在心，默默关爱宝贝成长" /><meta name="keywords" content="童装,裙子,宝贝,童鞋,爱宝贝,童装品牌,儿童服饰" />
  }
}

