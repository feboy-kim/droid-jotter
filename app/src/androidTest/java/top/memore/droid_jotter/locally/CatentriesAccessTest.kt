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
class CatentriesAccessTest {
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
    fun insertOneCatentry_thenGetCatentries_obtainOneCatentry() {
        val c = Catentry(cId = 10L, name = "One")

        dao.insertCatentry(c)
        val cs = dao.getCatentries()

        Assert.assertEquals(1, cs.size)
        Assert.assertEquals(c.name, cs[0].name)
        Assert.assertFalse(cs[0].deleting)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoCategories_withSameId_failedToInsertAnotherOne() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = Catentry(cId = c1.cId, name = "Two")

        dao.insertCatentry(c1)
        dao.insertCatentry(c2)
        val cs = dao.getCatentries()

        Assert.assertEquals(1, cs.size)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoCategories_withSameName_failedToInsertAnotherOne() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = Catentry(cId = 20L, name = "One")

        dao.insertCatentry(c1)
        dao.insertCatentry(c2)
        val cs = dao.getCatentries()

        Assert.assertEquals(1, cs.size)
    }

    @Test
    @Throws(Exception::class)
    fun upsertTwoCategories_withOneCreated_anotherOneUpdated() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = Catentry(cId = 20L, name = "Two")
        val c3 = c2.copy(name = "Three")

        dao.upsertCatentries(c1, c2, c3)
        val cs = dao.getCatentries()

        Assert.assertEquals(2, cs.size)
        Assert.assertTrue(cs.contains(c1))
        Assert.assertFalse(cs.contains(c2))
        Assert.assertTrue(cs.contains(c3))
    }

    @Test
    @Throws(Exception::class)
    fun updateOneCatentry_thenGetCatentries_obtainTheUpdated() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = c1.copy(name = "Two").apply {
            millitime = c1.millitime + 1
        }

        dao.insertCatentry(c1)
        val updated = dao.updateCatentry(c2)

        val cs = dao.getCatentries()

        Assert.assertEquals(1, updated)
        Assert.assertEquals(1, cs.size)
        Assert.assertEquals(c2.name, cs[0].name)
        Assert.assertTrue(cs[0].millitime > c1.millitime)
        Assert.assertFalse(cs[0].deleting)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun updateOneCatentry_withExistingName_failedToUpdated() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = Catentry(cId = 20L, name = "Two")
        val c3 = c1.copy(name = "Two", ready = false).apply {
            millitime = c1.millitime + 1
        }

        dao.insertCatentry(c1)
        dao.insertCatentry(c2)
        val updated = dao.updateCatentry(c3)

        Assert.assertEquals(0, updated)
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistent_noUpdated() {
        val c1 = Catentry(cId = 10L, name = "One")

        val updated = dao.updateCatentry(c1)

        Assert.assertEquals(0, updated)
    }

    @Test
    @Throws(Exception::class)
    fun deleteOneCatentry_oneDeleted() {
        val c = Catentry(cId = 10L, name = "One")
        dao.insertCatentry(c)

        val deleted = dao.deleteCatentry(c.cId)

        Assert.assertEquals(1, deleted)
    }

    @Test
    @Throws(Exception::class)
    fun deleteNonexistent_noDeleted() {
        val deleted = dao.deleteCatentry(100L)

        Assert.assertEquals(0, deleted)
    }

    @Test
    fun deleteExistingCategories_existentDeleted() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = Catentry(cId = 20L, name = "Two")
        dao.insertCatentry(c1)
        dao.insertCatentry(c2)

        Assert.assertEquals(2, dao.getCatentries().size)

        dao.deleteCatentries(c1, c2)

        Assert.assertEquals(0, dao.getCatentries().size)
    }

    @Test
    fun deleteExistingCategories_andOneNonexistent_existentDeleted() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = Catentry(cId = 20L, name = "Two")
        val c3 = Catentry(cId = 30L, name = "Three")
        dao.insertCatentry(c1)
        dao.insertCatentry(c2)

        Assert.assertEquals(2, dao.getCatentries().size)

        dao.deleteCatentries(c1, c2, c3)

        Assert.assertEquals(0, dao.getCatentries().size)
    }

}