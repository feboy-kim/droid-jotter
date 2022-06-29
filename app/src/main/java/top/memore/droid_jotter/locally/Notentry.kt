package top.memore.droid_jotter.locally

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notentries",
    indices = [Index(value = ["categoId", "title"], unique = true)]
)
data class Notentry(
    @PrimaryKey val nId: Long,
    val title: String,
    val brief: String?,
    val ymdate: Int?
) {
    var categoId: Long? = null
    var deleting: Boolean = false
    var millitime: Long = System.currentTimeMillis()

}
