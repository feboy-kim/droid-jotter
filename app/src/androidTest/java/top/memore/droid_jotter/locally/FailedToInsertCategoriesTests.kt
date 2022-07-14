package top.memore.droid_jotter.locally

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FailedToInsertCategoriesTests(
    private val c1: Category,
    private val c2: Category
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
    fun insertTwoCategories_withConflict_failed() {
        dao.insertCategories(c1, c2)

    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertOneCategoryAndAnother_withConflict_failed() {
        dao.insertCategories(c1)
        dao.insertCategories(c2)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            return listOf(
                arrayOf( // same id
                    Category(nId = 10L, title = "One"),
                    Category(nId = 10L, title = "Two")
                ),
                arrayOf( // same name
                    Category(nId = 10L, title = "One"),
                    Category(nId = 20L, title = "One")
                )
            )
        }
    }

}