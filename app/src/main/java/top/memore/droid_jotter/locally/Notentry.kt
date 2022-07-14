package top.memore.droid_jotter.locally

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notentries",
    indices = [Index(value = ["cateId", "title"], unique = true)]
)
data class Notentry(
    @PrimaryKey val nId: Long,
    val title: String,
    val brief: String?,
    val ymday: Int?,
    val millitime: Long = System.currentTimeMillis()
) {
    var cateId: Long? = null

}
