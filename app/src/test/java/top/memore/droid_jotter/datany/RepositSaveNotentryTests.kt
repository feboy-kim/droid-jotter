package top.memore.droid_jotter.datany

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import top.memore.droid_jotter.locally.LiteAccessible
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity

@RunWith(Parameterized::class)
class RepositSaveNotentryTests(
    private val entity: Notentity,
    private val result: Boolean
) {
    private lateinit var localine: LiteAccessible

    @Before
    fun setup() {
        localine = mockk()
    }

    @Test
    fun saveNotentity_withoutError_successWithBoolean() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.upsertNotentries(any()) } returns Unit

        val saved = repository.saveNotentry(10L, entity)

        if(result) {
            verify(exactly = 1) { localine.upsertNotentries(any()) }
        } else {
            verify(exactly = 0) { localine.upsertNotentries(any()) }
        }
        Assert.assertEquals(result, saved)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    Notentity(  // normal
                        nId = 10L,
                        title = "One",
                        brief = "abc",
                        ymdate = null
                    ),
                    true
                ),
                arrayOf(
                    Notentity(  // empty title
                        nId = 10L,
                        title = "",
                        brief = "abc",
                        ymdate = null
                    ),
                    false
                ),
                arrayOf(
                    Notentity(  // lengthy title
                        nId = 10L,
                        title = "One".padEnd(Litentity.TITLE_MAX_LENGTH + 1, 'n'),
                        brief = "abc",
                        ymdate = null
                    ),
                    false
                ),
                arrayOf(
                    Notentity(  // lengthy brief
                        nId = 10L,
                        title = "One",
                        brief = "abc".padEnd(Notentity.BRIEF_MAX_LENGTH + 1, 'n'),
                        ymdate = null
                    ),
                    false
                ),
                arrayOf(    // null brief
                    Notentity(
                        nId = 10L,
                        title = "One",
                        brief = null,
                        ymdate = null
                    ),
                    true
                ),
                arrayOf(    // empty brief
                    Notentity(
                        nId = 10L,
                        title = "One",
                        brief = "",
                        ymdate = null
                    ),
                    true
                )
            )
        }
    }
}