package top.memore.droid_jotter.locally

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "catentries",
    indices = [Index(value = ["name"], unique = true)]
)
data class Catentry(
    @PrimaryKey val cId: Long,
    val name: String,
    val ready: Boolean = true
) {
    var deleting: Boolean = false
    var millitime: Long = System.currentTimeMillis()

}
