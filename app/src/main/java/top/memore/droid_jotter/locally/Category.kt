package top.memore.droid_jotter.locally

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["title"], unique = true)]
)
data class Category(
    @PrimaryKey val nId: Long,
    val title: String,
    val millitime: Long = System.currentTimeMillis()
)
