package top.memore.droid_jotter.locally

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import top.memore.droid_jotter.models.Notentity

@RunWith(Parameterized::class)
class FailedToInsertNotentriesTests(
    private val note1: Notentry,
    private val note2: Notentry
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

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoNotentries_withConflict_failedToInsertLatter() {
        dao.insertCategories(c)
        dao.insertNotentries(note1, note2)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertNotentryAndAnother_withConflict_failedToInsertLatter() {
        dao.insertCategories(c)
        dao.insertNotentries(note1)
        dao.insertNotentries(note2)
    }

    companion object {
        private val c = Category(nId = 11L, title = "One", millitime = +101L)

        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            val ymd = Notentity.ymd(2000, 10, 20)
            val n1 = Notentry(nId = 101L, title = "A", brief = "The is Note One", ymday = ymd)
            val n2 = Notentry(nId = 102L, title = "B", brief = "The is Note Two", ymday = ymd)
            return listOf(
                arrayOf(    // id conflict
                    n1.copy().apply { cateId = c.nId },
                    n2.copy(nId = n1.nId).apply { cateId = c.nId }
                ),
                arrayOf(    // name conflict
                    n1.copy().apply { cateId = c.nId },
                    n2.copy(title = n1.title).apply { cateId = c.nId }
                )

            )
        }
    }

}