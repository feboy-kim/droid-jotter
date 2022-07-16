package top.memore.droid_jotter.ui.models

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import top.memore.droid_jotter.datany.LocalRepository
import top.memore.droid_jotter.models.Litentity

@RunWith(Parameterized::class)
class CategoriesVimodelUselessTests(
    private val entity: Litentity,
    private val updated: Boolean,
    private val usableSize0: Int,
    private val usableSize1: Int,
    private val uselessSize: Int
) {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var reposit: LocalRepository

    @Before
    fun setup() {
        reposit = mockk()
    }

    @Test
    fun updateAsUseless_withoutError_success() = runTest {
        val cs = listOf(entity, c2, c3)
        coEvery { reposit.loadCategories() } returns cs
        coEvery { reposit.updateCategoryUsability(any(), false) } returns updated

        val vm = CategoriesVimodel(reposit)
        coVerify { reposit.loadCategories() }
        Assert.assertEquals(usableSize0, vm.usableItems.value.size)

        vm.updateOneAsUseless(entity)
        coVerify { reposit.updateCategoryUsability(any(), false) }
        Assert.assertEquals(usableSize1, vm.usableItems.value.size)
        Assert.assertTrue(vm.usableItems.value.contains(c2))
        Assert.assertEquals(uselessSize, vm.uselessItems.value.size)
        Assert.assertTrue(vm.uselessItems.value.contains(c3))
    }

    @Test
    fun updateAsUseless_withException_failure() = runTest {
        val cs = listOf(entity, c2, c3)
        coEvery { reposit.loadCategories() } returns cs
        coEvery { reposit.updateCategoryUsability(any(), false) } throws Exception()

        val vm = CategoriesVimodel(reposit)
        vm.updateOneAsUseless(entity)
        val event = vm.failEvent.first()

        coVerify { reposit.loadCategories() }
        coVerify { reposit.updateCategoryUsability(any(), false) }

        Assert.assertNotNull(event.notHandledContent)
        Assert.assertNull(event.notHandledContent)      // handled only once
    }

    companion object {
        val c2 = Litentity(nId = 12L, title = "B").also {
            it.millitime = 102L
        }    // usable
        val c3 = Litentity(nId = 13L, title = "C").also {
            it.millitime = -103L
        }   // useless

        @JvmStatic
        @Parameterized.Parameters
        fun testData(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    Litentity(nId = 11L, title = "A").also {
                        it.millitime = 101L
                    },    // usable
                    true,
                    2,
                    1,
                    2
                ),
                arrayOf(
                    Litentity(nId = 11L, title = "A").also {
                        it.millitime = -101L
                    },    // useless
                    false,
                    1,
                    1,
                    2
                )
            )
        }
    }
}