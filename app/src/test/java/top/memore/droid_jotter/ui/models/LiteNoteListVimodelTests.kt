package top.memore.droid_jotter.ui.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity

class LiteNoteListVimodelTests {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadOrphanLiteNotes_withException_failure(): Unit = runTest {
        val reposit = object : LocalRepositFake() {
            private val dataFlow = MutableSharedFlow<AccessResult<List<Litentity>>>()
            suspend fun emit(v: AccessResult<List<Litentity>>) = dataFlow.emit(v)
            override suspend fun getLiteNotes(): Flow<AccessResult<List<Litentity>>> =
                dataFlow
        }

        val vm = LiteNoteListVimodel(reposit)
        vm.loadNotes()
        reposit.emit(AccessResult.Failure(Exception()))

        Assert.assertTrue(vm.notes.value is AccessResult.Failure)
    }

    @Test
    fun loadOrphanLiteNotes_withoutError_success(): Unit = runTest {
        val t1 = Litentity(nId = 11L, title = "A", millitime = 101L)    // usable
        val t2 = Litentity(nId = 12L, title = "B", millitime = 102L)    // usable
        val t3 = Litentity(nId = 13L, title = "C", millitime = -103L)   // useless
        val ts = listOf(t1, t2, t3)
        val reposit = object : LocalRepositFake() {
            private val dataFlow = MutableSharedFlow<AccessResult<List<Litentity>>>()
            suspend fun emit(v: AccessResult<List<Litentity>>) = dataFlow.emit(v)
            override suspend fun getLiteNotes(): Flow<AccessResult<List<Litentity>>> =
                dataFlow
        }

        val vm = LiteNoteListVimodel(reposit)
        vm.loadNotes()
        reposit.emit(AccessResult.Success(ts))

        Assert.assertFalse(vm.notes.value is AccessResult.Failure)
        Assert.assertEquals(AccessResult.Success(ts), vm.notes.value)
        (vm.notes.value as? AccessResult.Success)?.d?.let { list ->
            Assert.assertEquals(ts, list)
            val usables = list.filter { it.isUsable }
            Assert.assertEquals(2, usables.size)
            Assert.assertTrue(usables.any { it.nId == t1.nId && it.title == t1.title })
            Assert.assertTrue(usables.any { it.nId == t2.nId && it.title == t2.title })
            val useless = list.filter { !it.isUsable }
            Assert.assertEquals(1, useless.size)
            Assert.assertTrue(useless.any { it.nId == t3.nId && it.title == t3.title })
            useless[0].millitime = 0 - useless[0].millitime
            Assert.assertFalse(useless[0].isUsable)     // isUsable cannot be changed
        }

    }

    @Test
    fun loadCategorizedNotes_withException_failure(): Unit = runTest {
        val reposit = object : LocalRepositFake() {
            private val dataFlow = MutableSharedFlow<AccessResult<List<Litentity>>>()
            suspend fun emit(v: AccessResult<List<Litentity>>) = dataFlow.emit(v)
            override suspend fun getLiteNotes(pId: Long): Flow<AccessResult<List<Litentity>>> =
                dataFlow
        }

        val vm = LiteNoteListVimodel(reposit)
        vm.loadNotes(10L)
        reposit.emit(AccessResult.Failure(Exception()))

        Assert.assertTrue(vm.notes.value is AccessResult.Failure)
    }

    @Test
    fun loadCategorizedNotes_withoutError_success(): Unit = runTest {
        val t1 = Litentity(nId = 11L, title = "A", millitime = 101L)    // usable
        val t2 = Litentity(nId = 12L, title = "B", millitime = 102L)    // usable
        val t3 = Litentity(nId = 13L, title = "C", millitime = -103L)   // useless
        val ts = listOf(t1, t2, t3)
        val reposit = object : LocalRepositFake() {
            private val dataFlow = MutableSharedFlow<AccessResult<List<Litentity>>>()
            suspend fun emit(v: AccessResult<List<Litentity>>) = dataFlow.emit(v)
            override suspend fun getLiteNotes(pId: Long): Flow<AccessResult<List<Litentity>>> =
                dataFlow
        }

        val vm = LiteNoteListVimodel(reposit)
        vm.loadNotes(10L)
        reposit.emit(AccessResult.Success(ts))

        Assert.assertFalse(vm.notes.value is AccessResult.Failure)
        Assert.assertEquals(AccessResult.Success(ts), vm.notes.value)
        (vm.notes.value as? AccessResult.Success)?.d?.let {list ->
            Assert.assertEquals(ts, list)
            val usables = list.filter { it.isUsable }
            Assert.assertEquals(2, usables.size)
            Assert.assertTrue(usables.any { it.nId == t1.nId && it.title == t1.title })
            Assert.assertTrue(usables.any { it.nId == t2.nId && it.title == t2.title })
            val useless = list.filter { !it.isUsable }
            Assert.assertEquals(1, useless.size)
            Assert.assertTrue(useless.any { it.nId == t3.nId && it.title == t3.title })
            useless[0].millitime = 0 - useless[0].millitime
            Assert.assertFalse(useless[0].isUsable)     // isUsable cannot be changed
        }

    }

}

