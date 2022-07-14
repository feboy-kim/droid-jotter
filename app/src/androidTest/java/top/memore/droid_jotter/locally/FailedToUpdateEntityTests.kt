package top.memore.droid_jotter.locally

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FailedToUpdateEntityTests {
    private lateinit var dao: LiteAccessible
    private lateinit var db: AppDatabase

    @Before
    fun setupDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = AppDatabase.getTestDatabase(context)
        dao = db.getAccessor()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun updateOneCategory_withAnotherExistingName_failedToUpdate() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = 20L, title = "Two")
        val c3 = Category(nId = c1.nId, title = c2.title)
        dao.insertCategories(c1, c2)
        dao.updateCategories(c3)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun updateCategorizedNote_withUniqueConflict_failedToUpdate() {
        val c = Category(nId = 11L, title = "One", millitime = +101L)
        val n1 =
            Notentry(nId = 101L, title = "A", brief = "The is Note One", ymday = null).apply {
                cateId = c.nId
            }
        val n2 =
            Notentry(nId = 102L, title = "B", brief = "The is Note Two", ymday = null).apply {
                cateId = c.nId
            }
        val note = n2.copy(title = n1.title).apply {
            cateId = n1.cateId
        }
        dao.insertCategories(c)
        dao.insertNotentries(n1, n2)
        dao.updateNotentries(note)
    }

}