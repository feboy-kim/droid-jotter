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
import top.memore.droid_jotter.datany.LocalRepository
import top.memore.droid_jotter.models.Litentity

class MasterVimodelTests {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var reposit: LocalRepository

    @Before
    fun setup() {
        reposit = mockk()
    }

    @Test
    fun initiallyLoadCategories_withException_failure() = runTest {
        coEvery { reposit.loadCategories() } throws Exception()
        val vm = MasterVimodel(reposit)

        val event = vm.failEvent.first()

        coVerify { reposit.loadCategories() }
        Assert.assertNotNull(event.notHandledContent)
        Assert.assertNull(event.notHandledContent)      // handled only once
    }

    @Test
    fun initiallyLoadCategories_withoutError_success() = runTest {
        val c1 = Litentity(nId = 11L, title = "A").also {
            it.millitime = 101L
        }    // usable
        val c2 = Litentity(nId = 12L, title = "B").also {
            it.millitime = 102L
        }    // usable
        val c3 = Litentity(nId = 13L, title = "C").also {
            it.millitime = -103L
        }   // useless
        val cs = listOf(c1, c2, c3)
        coEvery { reposit.loadCategories() } returns cs

        val vm = MasterVimodel(reposit)

        coVerify { reposit.loadCategories() }
        Assert.assertEquals(3, vm.categories.value.size)
        vm.categories.value.let {
            Assert.assertEquals(MasterVimodel.categoryForOrphanotes, it.first())
            Assert.assertTrue(it.contains(c1))
            Assert.assertTrue(it.contains(c2))
            Assert.assertFalse(it.contains(c3))
        }
        Assert.assertEquals(MasterVimodel.categoryForOrphanotes, vm.selected.value)
    }

}