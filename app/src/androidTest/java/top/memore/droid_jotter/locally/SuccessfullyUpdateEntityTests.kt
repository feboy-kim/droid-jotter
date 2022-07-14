package top.memore.droid_jotter.locally

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SuccessfullyUpdateEntityTests {
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
    fun updateCategory_fromUsableToUseless() {
        dao.insertCategories(c1, c2)

        Assert.assertEquals(0, dao.updateCategoryAsUseless(c1.nId))
        Assert.assertEquals(1, dao.updateCategoryAsUseless(c2.nId))
        val cs = dao.getCategories()
        Assert.assertEquals(0, cs.filter { it.millitime > 0 }.size)
        Assert.assertEquals(2, cs.filter { it.millitime < 0 }.size)
    }

    @Test
    @Throws(Exception::class)
    fun updateCategory_fromUselessToUsable() {
        dao.insertCategories(c1, c2)

        Assert.assertEquals(1, dao.updateCategoryAsUsable(c1.nId))
        Assert.assertEquals(0, dao.updateCategoryAsUsable(c2.nId))
        val cs = dao.getCategories()
        Assert.assertEquals(2, cs.filter { it.millitime > 0 }.size)
        Assert.assertEquals(0, cs.filter { it.millitime < 0 }.size)
    }

    @Test
    @Throws(Exception::class)
    fun updateNotentry_fromUsableToUseless() {
        dao.insertNotentries(n1, n2)

        Assert.assertEquals(
            0,
            dao.updateNotentryAsUseless(n1.nId)
        )
        Assert.assertEquals(
            1,
            dao.updateNotentryAsUseless(n2.nId)
        )
        val notes = dao.getLiteNotes()
        Assert.assertEquals(0, notes.filter { it.millitime > 0 }.size)
        Assert.assertEquals(2, notes.filter { it.millitime < 0 }.size)
    }

    @Test
    @Throws(Exception::class)
    fun updateNotentry_fromUselessToUsable() {
        dao.insertNotentries(n1, n2)

        Assert.assertEquals(
            1,
            dao.updateNotentryAsUsable(n1.nId)
        )
        Assert.assertEquals(
            0,
            dao.updateNotentryAsUsable(n2.nId)
        )
        val notes = dao.getLiteNotes()
        Assert.assertEquals(2, notes.filter { it.millitime > 0 }.size)
        Assert.assertEquals(0, notes.filter { it.millitime < 0 }.size)
    }

    companion object {
        private val c1 = Category(nId = 101L, title = "A", millitime = -2000L)
        private val c2 = Category(nId = 102L, title = "B", millitime = 2000L)
        private val n1 = Notentry(
            nId = 100L, title = "", brief = "The is Note One", ymday = null, millitime = -20002L
        )
        private val n2 = Notentry(nId = 101L, title = "A", brief = "The is Note One", ymday = null)
    }

}