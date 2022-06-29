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
import top.memore.droid_jotter.DAY_OF_MONTH_MIN
import top.memore.droid_jotter.MONTH_MIN
import top.memore.droid_jotter.ymd

@RunWith(AndroidJUnit4::class)
class NotentriesAccessTest {
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
    fun insertEntities_obtainOrphanNotentries_noCategorizedNotes() {
        val c = Catentry(cId = System.currentTimeMillis(), name = "One")
        val n1 = Notentry(nId = 101L, title = "One", brief = "The is Note One", ymdate = null)
        val n2 =
            Notentry(nId = 102L, title = "Two", brief = "The is Note Two", ymdate = null).apply {
                categoId = c.cId + 1L
            }

        dao.insertCatentry(c)
        dao.insertNotentry(n1)
        dao.insertNotentry(n2)
        val orphanNotes = dao.getOrphanNotentries()
        val categoNotes = dao.getCategorizedNotes(c.cId)

        Assert.assertEquals(2, orphanNotes.size)
        Assert.assertTrue(orphanNotes.contains(n1))
        Assert.assertTrue(orphanNotes.contains(n2))
        Assert.assertTrue(categoNotes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertEntities_obtainCategorizedNotes_noOrphanNotentries() {
        val c = Catentry(cId = System.currentTimeMillis(), name = "One")
        val n1 =
            Notentry(nId = 101L, title = "One", brief = "The is Note One", ymdate = null).apply {
                categoId = c.cId
            }
        val n2 =
            Notentry(nId = 102L, title = "Two", brief = "The is Note Two", ymdate = null).apply {
                categoId = c.cId
            }

        dao.insertCatentry(c)
        dao.insertNotentry(n1)
        dao.insertNotentry(n2)
        val categoNotes = dao.getCategorizedNotes(c.cId)
        val orphanNotes = dao.getOrphanNotentries()

        Assert.assertEquals(2, categoNotes.size)
        Assert.assertTrue(categoNotes.contains(n1))
        Assert.assertTrue(categoNotes.contains(n2))
        Assert.assertTrue(orphanNotes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertEntities_oneCategorizedNote_oneOrphanNotentry_successfully() {
        val c = Catentry(cId = System.currentTimeMillis(), name = "One")
        val n1 = Notentry(
            nId = 101L,
            title = "The is Note One",
            brief = null,
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            categoId = c.cId
        }
        val n2 = Notentry(
            nId = 102L,
            title = "The is Note Two",
            brief = null,
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            categoId = c.cId + 1L
        }

        dao.insertCatentry(c)
        dao.insertNotentry(n1)
        dao.insertNotentry(n2)

        val categoNotes = dao.getCategorizedNotes(c.cId)
        val orphanNotes = dao.getOrphanNotentries()

        Assert.assertEquals(1, categoNotes.size)
        Assert.assertEquals(n1.title, categoNotes[0].title)
        Assert.assertNotNull(categoNotes[0].ymdate)
        Assert.assertFalse(categoNotes[0].deleting)
        Assert.assertEquals(1, orphanNotes.size)
        Assert.assertEquals(n2.title, orphanNotes[0].title)
        Assert.assertNotNull(orphanNotes[0].ymdate)
        Assert.assertFalse(orphanNotes[0].deleting)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoNotentries_withSameIds_failedToInsertLatter() {
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n2 =
            Notentry(nId = n1.nId, title = "Second", brief = "The is second Note", ymdate = null)

        dao.insertNotentry(n1)
        dao.insertNotentry(n2)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoCategorizedNotes_withSameCategoAndTitle_failedToInsertLatter() {
        val n1 =
            Notentry(
                nId = 110L,
                title = "Note",
                brief = "The is first Note",
                ymdate = null
            ).apply {
                categoId = 10L
            }
        val n2 =
            Notentry(
                nId = 120L,
                title = n1.title,
                brief = "The is second Note",
                ymdate = null
            ).apply {
                categoId = n1.categoId
            }

        dao.insertNotentry(n1)
        dao.insertNotentry(n2)
    }

    @Test
    @Throws(Exception::class)
    fun upsertTwoNotentries_withOneCreated_anotherOneUpdated() {
        val c = Catentry(cId = System.currentTimeMillis(), name = "One")
        val n1 =
            Notentry(nId = 101L, title = "Note One", brief = "The is Note One", ymdate = null)
        val n2 =
            Notentry(
                nId = 102L,
                title = "Note Two",
                brief = "The is Note Two",
                ymdate = null
            ).apply {
                categoId = c.cId
            }
        val n11 = n1.copy(ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)).apply {
            categoId = c.cId
        }

        dao.insertCatentry(c)
        dao.upsertNotentries(n1, n2, n11)
        val notes = dao.getNotentries()

        Assert.assertEquals(2, notes.size)
        Assert.assertFalse(notes.contains(n1))
        Assert.assertTrue(notes.contains(n2))
        Assert.assertTrue(notes.contains(n11))
    }

    @Test
    @Throws(Exception::class)
    fun insertTwoOrphanNotentries_withSameTitle_successfullyInsert() {
        val n1 =
            Notentry(
                nId = 110L,
                title = "Note",
                brief = "The is first Note",
                ymdate = null
            )
        val n2 =
            Notentry(
                nId = 120L,
                title = n1.title,
                brief = "The is second Note",
                ymdate = null
            )

        dao.insertNotentry(n1)
        dao.insertNotentry(n2)
        val orphanNotes = dao.getOrphanNotentries()

        Assert.assertEquals(2, orphanNotes.size)
        Assert.assertTrue(orphanNotes.contains(n1))
        Assert.assertTrue(orphanNotes.contains(n2))
    }

    @Test
    @Throws(Exception::class)
    fun updateOneOrphanNotentry_oneUpdated() {
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n2 = n1.copy(title = "Second").apply {
            millitime = n1.millitime + 1
        }

        dao.insertNotentry(n1)
        dao.updateNotentry(n2)

        val notes = dao.getOrphanNotentries()

        Assert.assertEquals(1, notes.size)
        Assert.assertEquals(n2.title, notes[0].title)
        Assert.assertEquals(n1.brief, notes[0].brief)
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistent_noOneUpdated() {
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)

        val updated = dao.updateNotentry(n1)

        Assert.assertEquals(0, updated)
    }

    @Test
    @Throws(Exception::class)
    fun updateOrphanNotentryToCategorized__oneUpdatedToCategorizedNote() {
        val c = Catentry(cId = 10L, name = "One")
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n2 = n1.copy(title = "Second").apply {
            categoId = c.cId
        }
        dao.insertCatentry(c)
        dao.insertNotentry(n1)
        dao.updateNotentry(n2)
        val orphanNotes = dao.getOrphanNotentries()
        val categoNotes = dao.getCategorizedNotes(c.cId)

        Assert.assertEquals(0, orphanNotes.size)
        Assert.assertEquals(1, categoNotes.size)
        Assert.assertEquals(n2.title, categoNotes[0].title)
        Assert.assertEquals(n1.brief, categoNotes[0].brief)
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun updateCategorizedNote_withExistingCategoAndTitle_failedToUpdate() {
        val c = Catentry(cId = 10L, name = "One")
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n2 = n1.copy(nId = 200L, title = "Second").apply {
            categoId = c.cId
        }
        val n11 = n1.copy(title = n2.title, brief = "This is the third note").apply {
            categoId = c.cId
        }
        dao.insertCatentry(c)
        dao.insertNotentry(n1)
        dao.insertNotentry(n2)
        dao.updateNotentry(n11)
    }

    @Test
    @Throws(Exception::class)
    fun deleteOrphanNotentry_oneDeleted() {
        val n = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        dao.insertNotentry(n)

        val deleted = dao.deleteNotentry(n.nId)

        Assert.assertEquals(1, deleted)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCategorizedNote_oneDeleted() {
        val c = Catentry(cId = 10L, name = "One")
        val n = Notentry(
            nId = 100L,
            title = "First",
            brief = "The is first Note",
            ymdate = null
        ).apply {
            categoId = c.cId
        }
        dao.insertCatentry(c)
        dao.insertNotentry(n)

        val deleted = dao.deleteNotentry(n.nId)

        Assert.assertEquals(1, deleted)
    }

    @Test
    @Throws(Exception::class)
    fun deleteNonexistent_noDeleted() {
        val deleted = dao.deleteNotentry(100L)

        Assert.assertEquals(0, deleted)
    }

    @Test
    fun deleteExistingNotentries_existentDeleted() {
        val c = Catentry(cId = System.currentTimeMillis(), name = "One")
        val n1 = Notentry(
            nId = 101L,
            title = "The is Note One",
            brief = null,
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            categoId = c.cId
        }
        val n2 = Notentry(
            nId = 102L,
            title = "The is Note Two",
            brief = null,
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            categoId = c.cId + 1L
        }
        dao.insertCatentry(c)
        dao.insertNotentry(n1)
        dao.insertNotentry(n2)

        Assert.assertEquals(2, dao.getNotentries().size)

        dao.deleteNotentries(n1, n2)

        Assert.assertEquals(0, dao.getNotentries().size)
    }

    @Test
    fun deleteExistingNotentries_andOneNonexistent_existentDeleted() {
        val c = Catentry(cId = System.currentTimeMillis(), name = "One")
        val n1 = Notentry(
            nId = 101L,
            title = "The is Note One",
            brief = null,
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            categoId = c.cId
        }
        val n2 = Notentry(
            nId = 102L,
            title = "The is Note Two",
            brief = null,
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            categoId = c.cId + 1L
        }
        val n3 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        dao.insertCatentry(c)
        dao.insertNotentry(n1)
        dao.insertNotentry(n2)

        Assert.assertEquals(2, dao.getNotentries().size)

        dao.deleteNotentries(n1, n2, n3)

        Assert.assertEquals(0, dao.getNotentries().size)
    }

}