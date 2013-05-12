package code.snippet

import code.model.Item
import code.model.ItemType
import net.liftweb.mapper.By
import net.liftweb.mapper.MaxRows
import net.liftweb.mapper.StartAt
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.mapper.Descending
import net.liftweb.mapper.OrderBy
import com.qq.open.OpenApiV3
import java.util.HashMap
import net.liftweb.json._
import net.liftweb.common.Full
import code.model.User

class IndexSnippet extends BaseSnippet {
  private val itemsPerPage = 8

  def meinvs = renderItems(Item.getItems(ItemType.MeiNv, itemsPerPage))

  def shuaiges = renderItems(Item.getItems(ItemType.ShuaiGe, itemsPerPage))

  def keais = renderItems(Item.getItems(ItemType.KeAi, itemsPerPage))

  def t = {
    <div class="item masonry_brick">
      <div class="item_t">
        <div class="img">
          <span class="item-img"></span><span class="price">￥<span id="price"></span></span>
          <div class="btns"></div>
        </div>
        <div class="title">
          <span id="title"></span>
        </div>
      </div>
      <div class="item_b pull-right">
        <i class="icon-heart"></i>
        美丽指数:200<span class="go"></span>
      </div>
    </div>
  }

  def userStatus = User.currentUser match {
    case Full(user) =>
      <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
          <div id="login-face">
          </div>{ user.name }<b class='caret'></b>
        </a>
        <ul class="dropdown-menu">
          <li>
            <a href="/user/">用户中心</a>
          </li>{
            if (User.superUser_?)
              <li>
                <a href="/admin/">后台管理</a>
              </li>
          }<li>
             <a href="/user/profile">账户设置</a>
           </li>
          <li>
            <a href="/user/favorites">我的关注</a>
          </li>
          <li class="last">
            <a href="/user/sign_out">退出</a>
          </li>
        </ul>
      </li>
    case _ => <span></span>

  }

}