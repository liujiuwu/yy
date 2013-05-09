package code.model

import net.liftweb.mapper.CreatedUpdated
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLong
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.MappedInt
import net.liftweb.mapper.MappedBoolean

object TaobaoItemCategory extends TaobaoItemCategory with LongKeyedMetaMapper[TaobaoItemCategory] {
  override def dbTableName = "taobao_item_categories"
  override def fieldOrder = List(id, pcid, cid, name, isParent,categorySort, createdAt, updatedAt)
}

class TaobaoItemCategory extends LongKeyedMapper[TaobaoItemCategory] with CreatedUpdated with IdPK {
  def getSingleton = TaobaoItemCategory

  object pcid extends MappedLong(this)
  object cid extends MappedLong(this)
  object name extends MappedString(this, 30)
  object isParent extends MappedBoolean(this){
    override def dbColumnName = "is_parent"
  }
  object categorySort extends MappedInt(this) {
    override def dbColumnName = "category_sort"
  }

  override lazy val createdAt = new MyUpdatedAt(this) {
    override def dbColumnName = "created_at"
  }

  override lazy val updatedAt = new MyUpdatedAt(this) {
    override def dbColumnName = "updated_at"
  }
}