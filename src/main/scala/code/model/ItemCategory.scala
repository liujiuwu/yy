package code.model

import net.liftweb.mapper.CreatedUpdated
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedBoolean
import net.liftweb.mapper.MappedInt
import net.liftweb.mapper.MappedLong
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.QueryParam
import net.liftweb.mapper.ByList
import scala.collection.mutable.LinkedList
import net.liftweb.mapper.By

object ItemCategory extends ItemCategory with LongKeyedMetaMapper[ItemCategory] {
  override def dbTableName = "item_categories"
  override def fieldOrder = List(id, name, cids, categorySort, createdAt, updatedAt)

  private var cacheCategories = LinkedList[TaobaoItemCategory]()
  def getCacheCategories: LinkedList[TaobaoItemCategory] = {
    if (cacheCategories.isEmpty) {
      cacheCategories ++= ItemCategory.findByKey(1).get.taobaoItemCategories
    }
    cacheCategories
  }
}

class ItemCategory extends LongKeyedMapper[ItemCategory] with CreatedUpdated with IdPK {
  def getSingleton = ItemCategory

  object name extends MappedString(this, 30)
  object cids extends MappedString(this, 800)
  object categorySort extends MappedInt(this) {
    override def dbColumnName = "category_sort"
  }

  override lazy val createdAt = new MyUpdatedAt(this) {
    override def dbColumnName = "created_at"
  }

  override lazy val updatedAt = new MyUpdatedAt(this) {
    override def dbColumnName = "updated_at"
  }

  def taobaoItemCategories = {
    var categories = new LinkedList[TaobaoItemCategory]
    for (cid <- cids.get.split(",")) {
      categories ++= TaobaoItemCategory.find(By(TaobaoItemCategory.cid, cid.toLong))
    }
    categories
  }
}