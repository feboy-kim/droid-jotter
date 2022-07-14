package top.memore.droid_jotter.locally

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SuccessfullyUpdateNotentriesTests(
    private val note: Notentry,
    private val updated: Int,
    private val usable: Int,
    private val useless: Int,
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
    fun updateNotentry_successfullyUpdated() {
        dao.insertNotentries(n0, n1, n2)

        Assert.assertEquals(updated, dao.updateNotentries(note))

        val notes = dao.getLiteNotes()

        Assert.assertEquals(usable, notes.filter { it.millitime > 0 } .size)
        Assert.assertEquals(useless, notes.filter { it.millitime < 0 } .size)

        if(updated > 0) {
            val updatedNote = dao.getOneNotentry(note.nId)
            Assert.assertNotNull(updatedNote)
            updatedNote?.let {
                Assert.assertEquals(note.title, it.title)
                Assert.assertEquals(note.millitime, it.millitime)
            }
        } else {
            val updatedNote = dao.getOneNotentry(note.nId)
            Assert.assertNull(updatedNote)
        }
    }

    companion object {
        private val n0 = Notentry(
            nId = 100L, title = "", brief = "The is Note One", ymday = null, millitime = -20002L
        )
        private val n1 = Notentry(nId = 101L, title = "A", brief = "The is Note One", ymday = null)
        private val n2 = Notentry(
            nId = 102L, title = "B", brief = "The is Note Two", ymday = null, millitime = 20002L
        ).apply {
            cateId = 20L
        }

        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    n1.copy().apply { cateId = 10L },   // change category id
                    1,
                    2,
                    1
                ),
                arrayOf(
                    n1.copy(title = n2.title),  // existent title, but with null category id
                    1,
                    2,
                    1
                ),
                arrayOf(
                    n2.copy(millitime = -3003L),    // change to useless
                    1,
                    1,
                    2
                ),
                arrayOf(
                    n0.copy(millitime = 3003L),    // change to usable
                    1,
                    3,
                    0
                ),
                arrayOf(
                    n2.copy(nId = 20002L),      // nonexistent
                    0,
                    2,
                    1
                )
            )
        }
    }

}