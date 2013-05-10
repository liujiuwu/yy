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
    renderItems(Item.getItems(ItemType.MeiNv, itemsPerPage))
  }

  def shuaiges = {
    renderItems(Item.getItems(ItemType.ShuaiGe, itemsPerPage))
  }

  def luolis = {
    renderItems(Item.getItems(ItemType.KeAi, itemsPerPage))
  }
  
  def t = {
    <div class="item masonry_brick">
	<div class="item_t">
		<div class="img">
			<span class="item-img"></span> <span class="price">￥<span id="price"></span></span>
			<div class="btns"></div>
		</div>
		<div class="title">
			<span id="title"></span>
		</div>
	</div>
	<div class="item_b pull-right">
		<i class="icon-heart"></i> 美丽指数:200 <span class="go"></span>
	</div>
</div>
  }

}