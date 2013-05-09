package code.model

import java.math.MathContext
import net.liftweb.mapper._
import net.liftweb.common.LRUMap
import net.liftweb.common.Full

object TaobaoItem extends TaobaoItem with CRUDify[Long, TaobaoItem] with LongKeyedMetaMapper[TaobaoItem] {
  override def dbTableName = "taobao_items"
  override def fieldOrder = List(id, nick, cid, itemId, title, price, picUrl, clickUrl, shopClickUrl, commission, volume, likes,isSpread, createdAt, updatedAt)

  var itemsCache = Map[Long, List[TaobaoItem]]()
  def loadItem2Cache = {
    ItemCategory.getCacheCategories map { category =>
      itemsCache += (category.cid.get -> TaobaoItem.findAll(By(TaobaoItem.cid, category.cid.get)))
    }
  }
}

class TaobaoItem extends LongKeyedMapper[TaobaoItem] with CreatedUpdated with IdPK {
  def getSingleton = TaobaoItem

  object title extends MappedString(this, 100)
  object nick extends MappedString(this, 20)
  object cid extends MappedLong(this) {
    override def dbIndexed_? = true
  }
  object itemId extends MappedLong(this) {
    override def dbColumnName = "item_id"
  }
  object price extends MappedString(this, 10)
  object picUrl extends MappedString(this, 300) {
    override def dbColumnName = "pic_url"
  }
  object clickUrl extends MappedString(this, 800) {
    override def dbColumnName = "click_url"
  }
  object shopClickUrl extends MappedString(this, 800) {
    override def dbColumnName = "shop_click_url"
  }

  object commission extends MappedString(this, 10)
  object volume extends MappedLong(this)
  object likes extends MappedLong(this)
  object isSpread extends MappedBoolean(this){
    override def dbColumnName = "is_spread"
    override def defaultValue = true
  }

  override lazy val createdAt = new MyUpdatedAt(this) {
    override def dbColumnName = "created_at"
  }

  override lazy val updatedAt = new MyUpdatedAt(this) {
    override def dbColumnName = "updated_at"
  }

}