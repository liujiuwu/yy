package code.lib

import scala.collection.JavaConversions._
import scala.io.Source
import com.taobao.api.DefaultTaobaoClient
import com.taobao.api.request.ItemcatsGetRequest
import com.taobao.api.request.TaobaokeItemsGetRequest
import code.model.ItemCategory
import code.model.MyDBVendor
import code.model.TaobaoItem
import code.model.TaobaoItemCategory
import net.liftweb.db.DB
import net.liftweb.db.DB1.db1ToDb
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.mapper.By
import net.liftweb.mapper.Schemifier
import sys.process._
import java.net.URL
import java.io.File

object Taobao {
  val (url, appkey, secret) = ("http://gw.api.taobao.com/router/rest", "21419108", "380c3fb3c41f7f9abbbe987e17de08ee")
  val client = new DefaultTaobaoClient(url, appkey, secret);

  def initDb = {
    DB.defineConnectionManager(DefaultConnectionIdentifier, MyDBVendor)
    Schemifier.schemify(true, Schemifier.infoF _, TaobaoItem, TaobaoItemCategory, ItemCategory)
  }
  def main(args: Array[String]) {
    if (args.length < 2) {
      println("pls input action <pullItems pageNo|pullCategories 0|downImages 0>")
      return
    }

    initDb
    args(0) match {
      case "pullItems" => pullItems(client, args(1).toInt)
      case "pullCategories" => pullItemCategory(client)
      case "downImages" => downItemImages
    }
  }

  def pullItems(client: DefaultTaobaoClient, pageNo: Int) {
    val categories = ItemCategory.findByKey(1).get.taobaoItemCategories
    println("pull " + categories.size + " category .............." + pageNo)
    for (itemCatalog <- categories; cid = itemCatalog.cid.get) {
      println("pull " + itemCatalog.name + " item .............." + pageNo)
      val request = new TaobaokeItemsGetRequest
      request.setFields("""num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume""")
      request.setNick("pure20052008")
      request.setCid(cid)
      request.setPageNo(pageNo)

      val items = client.execute(request).getTaobaokeItems()
      for (item <- items) {
        val taobaoItem = TaobaoItem.findOrCreate(By(TaobaoItem.itemId, item.getNumIid()))
        taobaoItem.cid(cid)
        taobaoItem.itemId(item.getNumIid())
        taobaoItem.title(item.getTitle().replaceAll("<span class=H>|</span>", ""))
        taobaoItem.nick(item.getNick())
        taobaoItem.clickUrl(item.getClickUrl())
        taobaoItem.shopClickUrl(item.getShopClickUrl())
        taobaoItem.picUrl(item.getPicUrl())
        taobaoItem.price(item.getPrice())
        taobaoItem.commission(item.getCommission())
        val volume: Long = if (item.getVolume() == null) 0 else item.getVolume()
        taobaoItem.volume(volume)
        taobaoItem.likes(0)
        taobaoItem.save
      }
    }
    println("Pull item finished.............." + pageNo)
  }

  def pullItemCategory(client: DefaultTaobaoClient, parentCid: Long = 50008165) {
    println("sync category ..............")
    val request = new ItemcatsGetRequest
    request.setParentCid(parentCid)
    val result = client.execute(request)
    val itemCategories = result.getItemCats()
    for (itemCategory <- itemCategories; cid = itemCategory.getCid(); isParent = itemCategory.getIsParent()) {
      if (cid > 0) {
        println(cid + "|" + itemCategory.getName() + "|" + itemCategory.getIsParent())
        val taobaoItemCategory = TaobaoItemCategory.findOrCreate(By(TaobaoItemCategory.cid, cid))
        taobaoItemCategory.name(itemCategory.getName())
        taobaoItemCategory.pcid(parentCid)
        taobaoItemCategory.isParent(isParent)
        taobaoItemCategory.cid(cid)
        taobaoItemCategory.save
        if (isParent) {
          pullItemCategory(client, itemCategory.getCid())
        }
      }
    }
    println("sync category finished ..............")
  }

  def downItemImages() = {
    println("down item pic ..............")
    TaobaoItem.findAll map { item =>
      //, "_400x400.jpg"
      List("_250x250.jpg").map { suffix =>
        new URL(item.picUrl + suffix) #> new File("/alidata/tb_image/" + item.itemId + suffix) !!
      }
    }
    println("down item pic finished ..............")
  }

}