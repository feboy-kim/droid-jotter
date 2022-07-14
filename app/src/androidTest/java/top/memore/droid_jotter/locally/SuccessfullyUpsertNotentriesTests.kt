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
class SuccessfullyUpsertNotentriesTests(
    private val note1: Notentry,
    private val note2: Notentry,
    private val note3: Notentry,
    private val upserted: Int
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
    fun upsertNotentries_successfully() {
        dao.upsertNotentries(note1, note2, note3)

        val notes = dao.getLiteNotes()
        Assert.assertEquals(upserted, notes.size)
    }

    @Test
    @Throws(Exception::class)
    fun upsertNotentries_oneByOne_successfully() {
        dao.upsertNotentries(note1)
        dao.upsertNotentries(note2)
        dao.upsertNotentries(note3)

        val notes = dao.getLiteNotes()
        Assert.assertEquals(upserted, notes.size)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            val n1 = Notentry(nId = 101L, title = "A", brief = "The is Note One", ymday = null)
            val n2 = Notentry(nId = 102L, title = "B", brief = "The is Note Two", ymday = null)
            return listOf(
                arrayOf(
                    n1,
                    n2,
                    n2.copy(title = "C"),
                    2
                ),  // same id
                arrayOf(
                    n1,
                    n2.copy().apply { cateId = 2L },
                    Notentry(
                        nId = 103L,
                        title = n2.title,
                        brief = "The is Note",
                        ymday = null
                    ).apply { cateId = 2L },
                    2
                ),  // same parent id and same title
                arrayOf(
                    n1,
                    n2,
                    n2.copy(title = n1.title),
                    2
                ),  // same null parent and same title
                arrayOf(
                    n1,
                    n2,
                    Notentry(nId = 103L, title = "C", brief = "The is Note", ymday = null),
                    3
                )
            )
        }
    }

}