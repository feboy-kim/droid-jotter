package top.memore.droid_jotter.locally

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Category::class, Notentry::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAccessor(): LocalAccessible

    companion object {
        fun getTestDatabase(cx: Context) =
            Room.inMemoryDatabaseBuilder(cx, AppDatabase::class.java).build()

        fun getWorkDatabase(cx: Context) =
            Room.databaseBuilder(cx, AppDatabase::class.java, "${cx.packageName}.db3").build()
    }
}