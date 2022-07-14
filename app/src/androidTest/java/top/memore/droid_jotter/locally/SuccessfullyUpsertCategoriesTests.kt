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
class SuccessfullyUpsertCategoriesTests(
    private val c1: Category,
    private val c2: Category,
    private val c3: Category,
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
    fun upsertOneCategoryAndAnother_successfullyInserted() {
        dao.upsertCategories(c1)
        dao.upsertCategories(c2)
        dao.upsertCategories(c3)

        val cs = dao.getCategories()

        Assert.assertEquals(usable, cs.filter { it.millitime > 0 }.size)
        Assert.assertEquals(useless, cs.filter { it.millitime < 0 }.size)
    }

    @Test
    @Throws(Exception::class)
    fun upsertCategories_successfullyInserted() {
        dao.upsertCategories(c1, c2, c3)

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
                    Category(nId = 20L, title = "Two"),
                    Category(nId = 30L, title = "Three"),
                    3,
                    0
                ),
                arrayOf(    // id conflict
                    Category(nId = 10L, title = "One"),
                    Category(nId = 10L, title = "Two"),
                    Category(nId = 30L, title = "Three"),
                    2,
                    0
                ),
                arrayOf(    // name conflict
                    Category(nId = 10L, title = "One"),
                    Category(nId = 20L, title = "One"),
                    Category(nId = 30L, title = "Three"),
                    2,
                    0
                ),
                arrayOf(    // name or id conflict
                    Category(nId = 10L, title = "One"),
                    Category(nId = 20L, title = "Two"),
                    Category(nId = 10L, title = "Two"),
                    1,
                    0
                ),
                arrayOf(
                    Category(nId = 10L, title = "One", millitime = -10001L),
                    Category(nId = 20L, title = "Two", millitime = -10002L),
                    Category(nId = 30L, title = "Three"),
                    1,
                    2
                )
            )
        }
    }

}