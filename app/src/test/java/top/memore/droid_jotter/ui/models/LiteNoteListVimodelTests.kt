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

class LiteNoteListVimodelTests {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var reposit: LocalRepository

    @Before
    fun setup() {
        reposit = mockk()
    }

    @Test
    fun loadOrphanLiteNotes_withException_failure(): Unit = runTest {
        coEvery { reposit.loadLiteNotes() } throws Exception()
        val vm = SearchingVimodel(reposit)
        vm.loadNotes(Litentity(0L, ""))

        val event = vm.event.first()

        coVerify { reposit.loadLiteNotes() }
        Assert.assertNotNull(event.notHandledContent)
        Assert.assertNull(event.notHandledContent)      // handled only once
    }

    @Test
    fun loadOrphanLiteNotes_withoutError_success(): Unit = runTest {
        val t1 = Litentity(nId = 11L, title = "A").apply {
            millitime = 101L
        }    // usable
        val t2 = Litentity(nId = 12L, title = "B").apply {
            millitime = 102L
        }    // usable
        val t3 = Litentity(nId = 13L, title = "C").apply {
            millitime = -103L
        }   // useless
        val ts = listOf(t1, t2, t3)

        val vm = SearchingVimodel(reposit)
        vm.loadNotes(Litentity(0L, ""))
//        reposit.emit(AccessState.Success(ts))

//        Assert.assertFalse(vm.notes.value is AccessState.Failure)
//        Assert.assertEquals(AccessState.Success(ts), vm.notes.value)
//        (vm.notes.value as? AccessState.Success)?.d?.let { list ->
//            Assert.assertEquals(ts, list)
//            val usables = list.filter { it.isUsable }
//            Assert.assertEquals(2, usables.size)
//            Assert.assertTrue(usables.any { it.nId == t1.nId && it.title == t1.title })
//            Assert.assertTrue(usables.any { it.nId == t2.nId && it.title == t2.title })
//            val useless = list.filter { !it.isUsable }
//            Assert.assertEquals(1, useless.size)
//            Assert.assertTrue(useless.any { it.nId == t3.nId && it.title == t3.title })
//            Assert.assertFalse(useless[0].isUsable)     // isUsable cannot be changed
//        }

    }

    @Test
    fun loadCategorizedNotes_withException_failure(): Unit = runTest {
        val vm = SearchingVimodel(reposit)
//        vm.loadNotes(10L)
//        reposit.emit(AccessState.Failure(Exception()))

//        Assert.assertTrue(vm.notes.value is AccessState.Failure)
    }

    @Test
    fun loadCategorizedNotes_withoutError_success(): Unit = runTest {
        val t1 = Litentity(nId = 11L, title = "A").apply {
            millitime = 101L
        }    // usable
        val t2 = Litentity(nId = 12L, title = "B").apply {
            millitime = 102L
        }    // usable
        val t3 = Litentity(nId = 13L, title = "C").apply {
            millitime = -103L
        }   // useless
        val ts = listOf(t1, t2, t3)

        val vm = SearchingVimodel(reposit)
//        vm.loadNotes(10L)
//        reposit.emit(AccessState.Success(ts))

//        Assert.assertFalse(vm.notes.value is AccessState.Failure)
//        Assert.assertEquals(AccessState.Success(ts), vm.notes.value)
//        (vm.notes.value as? AccessState.Success)?.d?.let { list ->
//            Assert.assertEquals(ts, list)
//            val usables = list.filter { it.isUsable }
//            Assert.assertEquals(2, usables.size)
//            Assert.assertTrue(usables.any { it.nId == t1.nId && it.title == t1.title })
//            Assert.assertTrue(usables.any { it.nId == t2.nId && it.title == t2.title })
//            val useless = list.filter { !it.isUsable }
//            Assert.assertEquals(1, useless.size)
//            Assert.assertTrue(useless.any { it.nId == t3.nId && it.title == t3.title })
//            Assert.assertFalse(useless[0].isUsable)     // isUsable cannot be changed
//        }

    }

}

