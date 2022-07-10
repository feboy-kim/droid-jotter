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
    fun getNonexistentNotentry_noNotentryLoaded() {
        val n = dao.getOneNotentry(10L)

        Assert.assertNull(n)
    }

    @Test
    @Throws(Exception::class)
    fun insertNotentriesWithoutParent_notentriesInserted() {
        val cateId = 10L
        val n1 = Notentry(nId = 101L, title = "One", brief = "The is Note One", ymdate = null)
        val n2 =
            Notentry(nId = 102L, title = "Two", brief = "The is Note Two", ymdate = null).apply {
                this.cateId = cateId
            }
        // without catentry inserted
        dao.insertNotentries(n1, n2)
        val orphanNotes = dao.getLiteNotes()
        val categoNotes = dao.getLiteNotes(cateId)

        Assert.assertEquals(2, orphanNotes.size)
        Assert.assertTrue(orphanNotes.any {
            it.nId == n1.nId && it.title == n1.title
        })
        Assert.assertTrue(orphanNotes.any {
            it.nId == n2.nId && it.title == n2.title
        })
        Assert.assertTrue(categoNotes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertCategorizedNotentries_categorizedNotesInserted() {
        val c = Category(nId = System.currentTimeMillis(), title = "" /* not null */)
        val n1 =
            Notentry(nId = 101L, title = "One", brief = "The is Note One", ymdate = null).apply {
                cateId = c.nId
            }
        val n2 =
            Notentry(nId = 102L, title = "Two", brief = "The is Note Two", ymdate = null).apply {
                cateId = c.nId
            }

        dao.insertCategories(c)
        dao.insertNotentries(n1, n2)
        val categoNotes = dao.getLiteNotes(c.nId)
        val orphanNotes = dao.getLiteNotes()

        Assert.assertEquals(2, categoNotes.size)
        Assert.assertTrue(categoNotes.any {
            it.nId == n1.nId && it.title == n1.title
        })
        Assert.assertTrue(categoNotes.any {
            it.nId == n2.nId && it.title == n2.title
        })
        Assert.assertTrue(orphanNotes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertNotentries_oneCategorizedNote_twoOrphanNotentries_threeNotesInserted() {
        val c1 = Category(nId = 11L, title = "One")
        val c2 = Category(nId = 12L, title = "not null")
        val n1 = Notentry(
            nId = 101L,
            title = "The is Note One",
            brief = "n",
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            cateId = c1.nId
        }
        val n2 = Notentry(
            nId = 102L,
            title = "The is Note Two",
            brief = "not null",
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        )
        val n3 = Notentry(
            nId = 103L,
            title = "The is Note Three",
            brief = "n",
            ymdate = null
        ).apply {
            cateId = c2.nId
        }

        dao.insertCategories(c1)
        dao.insertNotentries(n1, n2, n3)

        val categoNotes = dao.getLiteNotes(c1.nId)
        val orphanNotes = dao.getLiteNotes()

        Assert.assertEquals(1, categoNotes.size)
        Assert.assertTrue(categoNotes.any {
            it.nId == n1.nId && it.title == n1.title
        })
        Assert.assertEquals(2, orphanNotes.size)
        Assert.assertTrue(orphanNotes.any {
            it.nId == n2.nId && it.title == n2.title
        })
        Assert.assertTrue(orphanNotes.any {
            it.nId == n3.nId && it.title == n3.title
        })
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun insertTwoNotentries_withSameIds_failedToInsertLatter() {
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n2 =
            Notentry(nId = n1.nId, title = "Second", brief = "The is second Note", ymdate = null)

        dao.insertNotentries(n1, n2)
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
                cateId = 10L
            }
        val n2 =
            Notentry(
                nId = 120L,
                title = n1.title,
                brief = "The is second Note",
                ymdate = null
            ).apply {
                cateId = n1.cateId
            }

        dao.insertNotentries(n1, n2)
    }

    @Test
    @Throws(Exception::class)
    fun insertTwoCategorizedNotes_withNullCategoAndSameTitle_noConflicts() {
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

        dao.insertNotentries(n1, n2)

        Assert.assertEquals(2, dao.getLiteNotes().size)
    }

    @Test
    @Throws(Exception::class)
    fun upsertTwoNotentries_oneCreated_anotherOneUpdated() {
        val c = Category(nId = System.currentTimeMillis(), title = "One")
        val n1 =
            Notentry(nId = 101L, title = "Note One", brief = "The is Note One", ymdate = null)
        val n2 =
            Notentry(
                nId = 102L,
                title = "Note Two",
                brief = "The is Note Two",
                ymdate = null
            ).apply {
                cateId = c.nId
            }
        val n11 =
            n1.copy(title = "Some", ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN))
                .apply {
                    cateId = c.nId
                }

        dao.upsertCategories(c)
        dao.upsertNotentries(n1, n2, n11)
        val notes = dao.getLiteNotes(c.nId)

        Assert.assertEquals(2, notes.size)
        Assert.assertFalse(notes.any {
            it.nId == n1.nId && it.title == n1.title
        })
        Assert.assertTrue(notes.any {
            it.nId == n2.nId && it.title == n2.title
        })
        Assert.assertTrue(notes.any {
            it.nId == n11.nId && it.title == n11.title
        })
    }

    @Test
    @Throws(Exception::class)
    fun insertTwoOrphanNotentries_differentCategoIds_sameTitle_twoNotesInsert() {
        val n1 =
            Notentry(
                nId = 110L,
                title = "Note",
                brief = "The is first Note",
                ymdate = null
            ).apply {
                cateId = 10L
            }
        val n2 =
            Notentry(
                nId = 120L,
                title = n1.title,
                brief = "The is second Note",
                ymdate = null
            )

        dao.insertNotentries(n1, n2)
        val categoNotes = dao.getLiteNotes(10L)
        val orphanNotes = dao.getLiteNotes()

        Assert.assertEquals(0, categoNotes.size)
        Assert.assertEquals(2, orphanNotes.size)
        Assert.assertTrue(orphanNotes.any {
            it.nId == n1.nId && it.title == n1.title
        })
        Assert.assertTrue(orphanNotes.any {
            it.nId == n2.nId && it.title == n2.title
        })
    }

    @Test
    @Throws(Exception::class)
    fun updateOneOrphanNotentry_oneUpdated() {
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n11 = n1.copy(title = "Second")

        dao.insertNotentries(n1)
        val updated = dao.updateNotentries(n11)

        val notes = dao.getLiteNotes()

        Assert.assertEquals(1, updated)
        Assert.assertEquals(1, notes.size)
        Assert.assertTrue(notes.any {
            it.nId == n11.nId && it.title == n11.title
        })
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistent_noUpdated() {
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)

        val updated = dao.updateNotentries(n1)

        Assert.assertEquals(0, updated)
    }

    @Test
    @Throws(Exception::class)
    fun updateOrphanNotentryToCategorized__oneUpdated() {
        val c = Category(nId = 10L, title = "One")
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n11 = n1.copy(title = "Second").apply {
            cateId = c.nId
        }
        dao.insertCategories(c)
        dao.insertNotentries(n1)

        val updated = dao.updateNotentries(n11)

        val orphanNotes = dao.getLiteNotes()
        val categoNotes = dao.getLiteNotes(c.nId)
        Assert.assertEquals(1, updated)
        Assert.assertEquals(0, orphanNotes.size)
        Assert.assertEquals(1, categoNotes.size)
        Assert.assertTrue(categoNotes.any {
            it.nId == n11.nId && it.title == n11.title
        })
    }

    @Test(expected = SQLiteException::class)
    @Throws(Exception::class)
    fun updateCategorizedNote_withCategoAndTitleUniqueConflict_failedToUpdate() {
        val c = Category(nId = 10L, title = "One")
        val n1 = Notentry(nId = 100L, title = "First", brief = "The first Note", ymdate = null)
        val n2 =
            Notentry(nId = 200L, title = "Second", brief = "Second Note", ymdate = null).apply {
                cateId = c.nId
            }
        val n11 = n1.copy(title = n2.title, brief = "This is the third note").apply {
            cateId = c.nId
        }

        dao.insertCategories(c)
        dao.insertNotentries(n1, n2)
        dao.updateNotentries(n11)
    }

    @Test
    @Throws(Exception::class)
    fun updateUsableNotentryAsUseless_oneUpdated() {
        val n = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        dao.upsertNotentries(n)
        val n1 = dao.getOneNotentry(n.nId)
        Assert.assertNotNull(n1)
        n1?.let {
            Assert.assertTrue(it.millitime > 0)
        }

        val updated = dao.updateNotentryAsUseless(n.nId)

        Assert.assertEquals(1, updated)
        val n2 = dao.getOneNotentry(n.nId)
        Assert.assertNotNull(n2)
        n2?.let {
            Assert.assertTrue(it.millitime < 0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateUselessNotentryAsUsable_oneUpdated() {
        val n = Notentry(
            nId = 100L,
            title = "First",
            brief = "The is first Note",
            ymdate = null,
            millitime = -1000L
        )
        dao.upsertNotentries(n)
        val n1 = dao.getOneNotentry(n.nId)
        Assert.assertNotNull(n1)
        n1?.let {
            Assert.assertTrue(it.millitime < 0)
        }

        val updated = dao.updateNotentryAsUsable(n.nId)

        Assert.assertEquals(1, updated)
        val n2 = dao.getOneNotentry(n.nId)
        Assert.assertNotNull(n2)
        n2?.let {
            Assert.assertTrue(it.millitime > 0)
        }
    }

    @Test
    fun deleteExistingNotentries_existentDeleted() {
        val c = Category(nId = System.currentTimeMillis(), title = "One")
        val n1 = Notentry(
            nId = 101L,
            title = "The is Note One",
            brief = "not null",
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            cateId = c.nId
        }
        val n2 = Notentry(
            nId = 102L,
            title = "The is Note Two",
            brief = "not null",
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        )
        dao.upsertCategories(c)
        dao.upsertNotentries(n1, n2)

        Assert.assertEquals(1, dao.getLiteNotes().size)
        Assert.assertEquals(1, dao.getLiteNotes(c.nId).size)

        val deleted = dao.deleteNotentries(n1, n2)

        Assert.assertEquals(2, deleted)
    }

    @Test
    fun deleteNotentries_onlyExistentDeleted() {
        val n1 = Notentry(
            nId = 101L,
            title = "The is Note One",
            brief = "not null",
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        ).apply {
            cateId = 10L
        }
        val n2 = Notentry(
            nId = 102L,
            title = "The is Note Two",
            brief = "not null",
            ymdate = ymd(y = 2000, m = MONTH_MIN, d = DAY_OF_MONTH_MIN)
        )
        val n3 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        dao.upsertNotentries(n1, n2)

        Assert.assertEquals(2, dao.getLiteNotes().size)

        val deleted = dao.deleteNotentries(n1, n2, n3)

        Assert.assertEquals(2, deleted)
        Assert.assertEquals(0, dao.getLiteNotes().size)
    }

}