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
class SuccessfullyDeleteNotentriesTests(
    private val note1: Notentry,
    private val note2: Notentry,
    private val deleted: Int,
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
    fun deleteNotentries_onlyExistentDeleted() {
        dao.upsertNotentries(note1, note2)
        val note3 = Notentry(nId = 103L, title = "C", brief = "The is Note", ymday = null)

        Assert.assertEquals(deleted, dao.deleteNotentries(note1, note2, note3))
        Assert.assertEquals(0, dao.getLiteNotes().size)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            val n1 = Notentry(nId = 101L, title = "A", brief = "The is Note One", ymday = null)
            val n2 = Notentry(nId = 102L, title = "B", brief = "The is Note Two", ymday = null)
            return listOf(
                arrayOf(n1, n2, 2),
            )
        }
    }

}