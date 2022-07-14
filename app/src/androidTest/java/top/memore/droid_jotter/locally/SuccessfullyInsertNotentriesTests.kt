package top.memore.droid_jotter.locally

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import top.memore.droid_jotter.models.Notentity

@RunWith(Parameterized::class)
class SuccessfullyInsertNotentriesTests(
    private val note1: Notentry,
    private val note2: Notentry,
    private val orphanCount: Int,
    private val categorized: Int
) {
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

    @Test
    @Throws(Exception::class)
    fun insertNotentries_successfully_notentriesInserted() {
        dao.insertCategories(c1, c2)    // no c0 inserted

        dao.insertNotentries(note1, note2)

        Assert.assertEquals(orphanCount, dao.getLiteNotes().size)
        Assert.assertEquals(categorized, dao.getLiteNotes(c1.nId).size)
    }

    companion object {
        val c1 = Category(nId = 11L, title = "One", millitime = +101L)
        val c2 = Category(nId = 12L, title = "Two", millitime = -102L)

        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            val ymd = Notentity.ymd(2000, 10, 20)
            val n1 = Notentry(nId = 101L, title = "A", brief = "The is Note One", ymday = ymd)
            val n2 = Notentry(nId = 102L, title = "B", brief = "The is Note Two", ymday = ymd)
            return listOf(
                arrayOf(
                    n1,
                    n2,
                    2,
                    0
                ), // all orphan notes
                arrayOf(
                    n1,
                    n2.copy(title = n1.title),      // the same title, null category
                    2,
                    0
                ), // all orphan notes
                arrayOf(
                    n1,
                    n2.copy().apply { cateId = 0L },        // nonexistent category
                    2,
                    0
                ), // all orphan notes
                arrayOf(
                    n1,
                    n2.copy().apply { cateId = c2.nId },    // useless category
                    2,
                    0
                ), // all orphan notes
                arrayOf(
                    n1.copy().apply { cateId = c1.nId },
                    n2,
                    1,
                    1
                ),
                arrayOf(
                    n1.copy().apply { cateId = c1.nId },
                    n2.copy().apply { cateId = c1.nId },
                    0,
                    2
                ),
                arrayOf(
                    n1.copy().apply { cateId = c1.nId },    // usable category
                    n2.copy().apply { cateId = c2.nId },    // useless category
                    1,
                    1
                ),
            )
        }
    }

}