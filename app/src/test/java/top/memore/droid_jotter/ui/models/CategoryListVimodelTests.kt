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

class CategoryListVimodelTests {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var reposit: LocalRepository

    @Before
    fun setup() {
        reposit = mockk()
    }

    @Test
    fun initiallyLoadata_withException_failure() = runTest {
        coEvery { reposit.loadCategories() } throws Exception()
        val vm = CategoryListVimodel(reposit)

        val event = vm.event.first()

        coVerify { reposit.loadCategories() }
        Assert.assertNotNull(event.notHandledContent)
        Assert.assertNull(event.notHandledContent)      // handled only once
    }

    @Test
    fun initiallyLoadata_withoutError_success() = runTest {
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

        val vm = CategoryListVimodel(reposit)
//        reposit.emit(AccessState.Success(cs))

//        Assert.assertFalse(vm.items.value is AccessState.Failure)
//        Assert.assertEquals(AccessState.Success(cs), vm.items.value)
//        (vm.items.value as? AccessState.Success)?.d?.let { list ->
//            Assert.assertEquals(cs, list)
//            val usables = list.filter { it.isUsable }
//            Assert.assertEquals(2, usables.size)
//            Assert.assertTrue(usables.any { it.nId == c1.nId && it.title == c1.title })
//            Assert.assertTrue(usables.any { it.nId == c2.nId && it.title == c2.title })
//            val useless = list.filter { !it.isUsable }
//            Assert.assertEquals(1, useless.size)
//            Assert.assertTrue(useless.any { it.nId == c3.nId && it.title == c3.title })
//            Assert.assertFalse(useless[0].isUsable)     // isUsable cannot be changed
//        }
    }
}