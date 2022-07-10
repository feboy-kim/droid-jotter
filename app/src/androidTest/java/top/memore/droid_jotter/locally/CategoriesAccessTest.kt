package top.memore.droid_jotter.locally

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesAccessTest {
    private lateinit var dao: LocalAccessible
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
    fun getNonexistentCategory_noCategoryLoaded() {
        val c = dao.getOneCategory(10L)

        Assert.assertNull(c)
    }

    @Test
    @Throws(Exception::class)
    fun insertOneCategory_oneCategoryInserted() {
        val c = Category(nId = 10L, title = "One")

        dao.insertCategories(c)
        val cs = dao.getCategories()

        Assert.assertEquals(1, cs.size)
        Assert.assertEquals(c.title, cs[0].title)
    }

    @Test
    @Throws(Exception::class)
    fun upsertOneCategory_oneCategoryUpserted() {
        val c = Category(nId = 10L, title = "One")

        dao.upsertCategories(c)
        val cs = dao.getCategories()

        Assert.assertEquals(1, cs.size)
        Assert.assertEquals(c.title, cs[0].title)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoCategories_withSameId_failedToInsertAnotherOne() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = c1.nId, title = "Two")

        dao.insertCategories(c1, c2)
    }

    @Test
    @Throws(Exception::class)
    fun upsertTwoCategories_withSameId_oneInsertThenUpdated() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = c1.nId, title = "Two")

        dao.upsertCategories(c1, c2)
        val cs = dao.getCategories()

        Assert.assertEquals(1, cs.size)
        Assert.assertEquals(c2.title, cs[0].title)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoCategories_withSameName_failedToInsertAnotherOne() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = 20L, title = c1.title)

        dao.insertCategories(c1, c2)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun upsertTwoCategories_withSameName_failedToUpsert() {
        val c1 = Category(nId = 10L, title = "not null")
        val c2 = Category(nId = 20L, title = c1.title)

        dao.insertCategories(c1, c2)
    }

    @Test
    @Throws(Exception::class)
    fun upsertTwoCategories_withOneCreated_anotherOneUpdated() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = 20L, title = "Two")
        val c3 = c2.copy(title = "Three")

        dao.upsertCategories(c1, c2, c3)
        val cs = dao.getCategories()

        Assert.assertEquals(2, cs.size)
        Assert.assertTrue(cs.any {
            it.nId == c1.nId && it.title == c1.title
        })
        Assert.assertFalse(cs.any {
            it.nId == c2.nId && it.title == c2.title
        })
        Assert.assertTrue(cs.any {
            it.nId == c3.nId && it.title == c3.title
        })
    }

    @Test
    @Throws(Exception::class)
    fun insertOneCategory_thenUpdate_oneUpdated() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = c1.copy(title = "Two")

        dao.insertCategories(c1)
        val updated = dao.updateCategories(c2)

        val cs = dao.getCategories()

        Assert.assertEquals(1, updated)
        Assert.assertEquals(1, cs.size)
        Assert.assertEquals(c2.title, cs[0].title)
    }

    @Test
    @Throws(Exception::class)
    fun updateOneUsableCategoryAsUseless_oneUpdated() {
        val c1 = Category(nId = 10L, title = "One")
        dao.insertCategories(c1)
        val d1 = dao.getOneCategory(c1.nId)
        Assert.assertNotNull(d1)
        d1?.let {
            Assert.assertTrue(it.millitime > 0)
        }

        val updated = dao.updateCategoryAsUseless(c1.nId)

        Assert.assertEquals(1, updated)
        val d2 = dao.getOneCategory(c1.nId)
        Assert.assertNotNull(d2)
        d2?.let {
            Assert.assertTrue(it.millitime < 0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateOneUselessCategoryAsUsable_oneUpdated() {
        val c = Category(nId = 10L, title = "One", millitime = -1100L)
        dao.insertCategories(c)
        val c1 = dao.getOneCategory(c.nId)
        Assert.assertNotNull(c1)
        c1?.let {
            Assert.assertTrue(it.millitime < 0)
        }

        val updated = dao.updateCategoryAsUsable(c.nId)

        Assert.assertEquals(1, updated)
        val c2 = dao.getOneCategory(c.nId)
        Assert.assertNotNull(c2)
        c2?.let {
            Assert.assertTrue(it.millitime > 0)
        }
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun updateOneCategory_withAnotherExistingName_failedToUpdate() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = 20L, title = "Two")
        val c11 = c1.copy(title = "Two")

        dao.insertCategories(c1, c2)
        dao.updateCategories(c11)
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistent_noUpdated() {
        val updated = dao.updateCategories(Category(nId = 10L, title = "One"))

        Assert.assertEquals(0, updated)
    }

    @Test
    fun deleteExistingCategories_existentDeleted() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = 20L, title = "Two")
        dao.insertCategories(c1, c2)

        Assert.assertEquals(2, dao.getCategories().size)

        val deleted = dao.deleteCategories(c1, c2)

        Assert.assertEquals(2, deleted)
        Assert.assertEquals(0, dao.getCategories().size)
    }

    @Test
    fun deleteExistingCategories_withOneNonexistent_existentDeleted() {
        val c1 = Category(nId = 10L, title = "One")
        val c2 = Category(nId = 20L, title = "Two")
        val c3 = Category(nId = 30L, title = "Three")
        dao.insertCategories(c1, c2)

        Assert.assertEquals(2, dao.getCategories().size)

        val deleted = dao.deleteCategories(c1, c2, c3)

        Assert.assertEquals(2, deleted)
        Assert.assertEquals(0, dao.getCategories().size)
    }

}