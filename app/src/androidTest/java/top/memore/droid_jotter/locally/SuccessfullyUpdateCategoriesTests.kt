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

@RunWith(Parameterized::class)
class SuccessfullyUpdateCategoriesTests(
    private val c1: Category,
    private val c2: Category,
    private val updated: Int,
    private val usable: Int,
    private val useless: Int
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
    fun updateCategory_successfullyUpdated() {
        dao.insertCategories(c1)

        Assert.assertEquals(updated, dao.updateCategories(c2))

        val cs = dao.getCategories()

        Assert.assertEquals(usable, cs.filter { it.millitime > 0 }.size)
        Assert.assertEquals(useless, cs.filter { it.millitime < 0 }.size)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    Category(nId = 10L, title = "One"),
                    Category(nId = 10L, title = "Two"),
                    1,
                    1,
                    0
                ),
                arrayOf(
                    Category(nId = 10L, title = "One"),
                    Category(nId = 11L, title = "Two"),
                    0,
                    1,
                    0
                ),
                arrayOf(
                    Category(nId = 10L, title = "One", millitime = -10001L),
                    Category(nId = 10L, title = "One", millitime = +10002L),
                    1,
                    1,
                    0
                ),
                arrayOf(
                    Category(nId = 10L, title = "One", millitime = +10001L),
                    Category(nId = 10L, title = "One", millitime = -10002L),
                    1,
                    0,
                    1
                )
            )
        }
    }

}