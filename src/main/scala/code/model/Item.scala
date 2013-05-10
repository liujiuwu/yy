package code.model

import java.math.MathContext
import net.liftweb.mapper._
import net.liftweb.common.LRUMap
import net.liftweb.common.Full

object ItemType extends Enumeration {
  type ItemType = Value
  val MeiNv = Value(0, "美女")
  val ShuaiGe = Value(1, "帅哥")
  val KeAi = Value(2, "可爱")
}

object Item extends Item with CRUDify[Long, Item] with LongKeyedMetaMapper[Item] {
  override def dbTableName = "items"
  override def fieldOrder = List(id, title, descn, picUrl, clickUrl, likeCount, commentCount, isSpread, createdAt, updatedAt)

  def getItems(itemType: ItemType.Value, rows: Int) = {
    Item.findAll(By(Item.itemType, itemType), OrderBy(Item.createdAt, Descending), StartAt(0), MaxRows(rows))
  }

}

class Item extends LongKeyedMapper[Item] with CreatedUpdated with IdPK {
  def getSingleton = Item

  object title extends MappedString(this, 100)
  object itemType extends MappedEnum(this, ItemType) {
    override def dbColumnName = "item_type"
    override def defaultValue = ItemType.MeiNv
  }
  object descn extends MappedString(this, 500)
  object picUrl extends MappedString(this, 300) {
    override def dbColumnName = "pic_url"
  }

  object clickUrl extends MappedString(this, 800) {
    override def dbColumnName = "click_url"
  }

  object likeCount extends MappedLong(this) {
    override def dbColumnName = "like_count"
    override def defaultValue = 0
  }

  object commentCount extends MappedLong(this) {
    override def dbColumnName = "comment_count"
    override def defaultValue = 0
  }

  object isSpread extends MappedBoolean(this) {
    override def dbColumnName = "is_spread"
    override def defaultValue = false
  }

  override lazy val createdAt = new MyUpdatedAt(this) {
    override def dbColumnName = "created_at"
  }

  override lazy val updatedAt = new MyUpdatedAt(this) {
    override def dbColumnName = "updated_at"
  }
}