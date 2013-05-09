package code.snippet

import code.model.Item
import code.model.ItemType
import net.liftweb.mapper.By
import net.liftweb.mapper.MaxRows
import net.liftweb.mapper.StartAt
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.mapper.Descending
import net.liftweb.mapper.OrderBy

class IndexSnippet extends BaseSnippet {
  private val itemsPerPage = 8

  private def renderItems(items: List[Item]) = {
    val picSuffix = "_200x200.jpg"
    ".item" #> items.map(item => {
      "#title" #> item.title.is &
        ".item-img" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;" }><img class="lazy" src="/images/grey.gif" data-original={ item.picUrl + picSuffix }/></a> &
        ".go" #> <a href="javascript://" onclick={ "goto('" + item.clickUrl.is + "');return false;" } class="go"><i class="icon-chevron-right"></i>&nbsp;去看看</a>
    })
  }

  def meinvs = {
    renderItems(Item.findAll(By(Item.itemType, ItemType.MeiNv), OrderBy(Item.createdAt, Descending), StartAt(0), MaxRows(itemsPerPage)))
  }
  
  def shuaiges = {
    renderItems(Item.findAll(By(Item.itemType, ItemType.ShuaiGe), OrderBy(Item.createdAt, Descending), StartAt(0), MaxRows(itemsPerPage)))
  }
  
  def luolis = {
    renderItems(Item.findAll(By(Item.itemType, ItemType.LuoLi), OrderBy(Item.createdAt, Descending), StartAt(0), MaxRows(itemsPerPage)))
  }

}