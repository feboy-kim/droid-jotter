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
class RepositSaveCategoryTests(
    private val entity: Litentity,
    private val result: Boolean
) {
    private lateinit var localine: LiteAccessible

    @Before
    fun setup() {
        localine = mockk()
    }

    @Test
    fun saveValidCategory_withoutError_successWithTrue() = runTest {
        val repository = LocalRepositImpl(localine)
        every { localine.upsertCategories(any()) } returns Unit

        val saved = repository.saveCategory(entity)

        if (result) {
            verify(exactly = 1) { localine.upsertCategories(any()) }
        } else {
            verify(exactly = 0) { localine.upsertCategories(any()) }
        }
        Assert.assertEquals(result, saved)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    Litentity(nId = 10L, title = "One"),
                    true
                ),
                arrayOf(
                    Litentity(nId = 10L, title = ""),
                    false
                ),
                arrayOf(
                    Litentity(
                        nId = 10L,
                        title = "One".padEnd(Litentity.TITLE_MAX_LENGTH + 1, 'c')
                    ),
                    false
                ),
            )
        }
    }
}