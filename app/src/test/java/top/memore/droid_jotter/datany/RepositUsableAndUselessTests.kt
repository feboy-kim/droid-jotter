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

@RunWith(Parameterized::class)
class RepositUsableAndUselessTests(
    private val updatedCount: Int,
    private val reposUpdated: Boolean
) {
    private lateinit var localine: LiteAccessible

    @Before
    fun setup() {
        localine = mockk()
    }

    @Test
    fun updateCategoryAsUsable_withoutError_successfully() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.updateCategoryAsUsable(any()) } returns updatedCount

        val result = repository.updateCategoryAsUsable(10L)

        verify { localine.updateCategoryAsUsable(any()) }
        Assert.assertEquals(reposUpdated, result)
    }

    @Test
    fun updateCategoryAsUseless_withoutError_successfully() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.updateCategoryAsUseless(any()) } returns updatedCount

        val result = repository.updateCategoryAsUseless(10L)

        verify { localine.updateCategoryAsUseless(any()) }
        Assert.assertEquals(reposUpdated, result)
    }

    @Test
    fun updateNotentryAsUsable_withoutError_successfully() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.updateNotentryAsUsable(any()) } returns updatedCount

        val result = repository.updateNotentryAsUsable(10L)

        verify { localine.updateNotentryAsUsable(any()) }
        Assert.assertEquals(reposUpdated, result)
    }

    @Test
    fun updateNotentryAsUseless_withoutError_successfully() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.updateNotentryAsUseless(any()) } returns updatedCount

        val result = repository.updateNotentryAsUseless(10L)

        verify { localine.updateNotentryAsUseless(any()) }
        Assert.assertEquals(reposUpdated, result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            return listOf(
                arrayOf(1, true),
                arrayOf(0, false)
            )
        }
    }
}