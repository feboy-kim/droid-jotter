package top.memore.droid_jotter.ui.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity

class CategoryListVimodelTests {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initiallyLoadata_withException_failure() = runTest {
        val reposit = object : LocalRepositFake() {
            private val dataFlow = MutableSharedFlow<AccessResult<List<Litentity>>>()
            suspend fun emit(v: AccessResult<List<Litentity>>) = dataFlow.emit(v)
            override suspend fun getCategories(): Flow<AccessResult<List<Litentity>>> =
                dataFlow
        }

        val vm = CategoryListVimodel(reposit)
        reposit.emit(AccessResult.Failure(Exception()))

        Assert.assertTrue(vm.items.value is AccessResult.Failure)
    }

    @Test
    fun initiallyLoadata_withoutError_success() = runTest {
        val c1 = Litentity(nId = 11L, title = "A", millitime = 101L)    // usable
        val c2 = Litentity(nId = 12L, title = "B", millitime = 102L)    // usable
        val c3 = Litentity(nId = 13L, title = "C", millitime = -103L)   // useless
        val cs = listOf(c1, c2, c3)
        val reposit = object : LocalRepositFake() {
            private val dataFlow = MutableSharedFlow<AccessResult<List<Litentity>>>()
            suspend fun emit(v: AccessResult<List<Litentity>>) = dataFlow.emit(v)
            override suspend fun getCategories(): Flow<AccessResult<List<Litentity>>> =
                dataFlow
        }

        val vm = CategoryListVimodel(reposit)
        reposit.emit(AccessResult.Success(cs))

        Assert.assertFalse(vm.items.value is AccessResult.Failure)
        Assert.assertEquals(AccessResult.Success(cs), vm.items.value)
        (vm.items.value as? AccessResult.Success)?.d?.let { list ->
            Assert.assertEquals(cs, list)
            val usables = list.filter { it.isUsable }
            Assert.assertEquals(2, usables.size)
            Assert.assertTrue(usables.any { it.nId == c1.nId && it.title == c1.title })
            Assert.assertTrue(usables.any { it.nId == c2.nId && it.title == c2.title })
            val useless = list.filter { !it.isUsable }
            Assert.assertEquals(1, useless.size)
            Assert.assertTrue(useless.any { it.nId == c3.nId && it.title == c3.title })
            useless[0].millitime = 0 - useless[0].millitime
            Assert.assertFalse(useless[0].isUsable)     // isUsable cannot be changed
        }
    }
}