package top.memore.droid_jotter.datany

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import top.memore.droid_jotter.TITLE_MAX_LENGTH
import top.memore.droid_jotter.locally.Litentry
import top.memore.droid_jotter.locally.LocalAccessible
import top.memore.droid_jotter.locally.Notentry
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity

class LocalRepositoryTests {
    private lateinit var localine: LocalAccessible

    @Before
    fun setup() {
        localine = mockk()
    }

    @Test
    fun getCategories_withoutError_success() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val c1 = Litentry(nId = 10L, title = "One")
        val c2 = Litentry(nId = 20L, title = "Two")
        every { localine.getCategories() } returns listOf(c1, c2)

        val result = repository.getCategories().first()

        verify { localine.getCategories() }
        Assert.assertTrue(result is AccessResult.Success<List<Litentity>>)
        (result as? AccessResult.Success<List<Litentity>>)?.d?.let {
            Assert.assertEquals(2, it.size)
            Assert.assertTrue(it.any { c ->
                c.nId == c1.nId && c.title == c1.title
            })
            Assert.assertTrue(it.any { c ->
                c.nId == c2.nId && c.title == c2.title
            })
        }
    }

    @Test
    fun getCategories_withException_failure() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.getCategories() } throws Exception()

        val result = repository.getCategories().first()

        verify { localine.getCategories() }
        Assert.assertTrue(result is AccessResult.Failure)
    }

    @Test
    fun getOrphanNoteItems_withoutError_success() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val n1 = Litentry(nId = 10L, title = "One")
        val n2 = Litentry(nId = 20L, title = "Two")
        every { localine.getLiteNotes() } returns listOf(n1, n2)

        val result = repository.getLiteNotes().first()

        verify { localine.getLiteNotes() }
        Assert.assertTrue(result is AccessResult.Success<List<Litentity>>)
        (result as? AccessResult.Success<List<Litentity>>)?.d?.let {
            Assert.assertEquals(2, it.size)
            Assert.assertTrue(it.any { n ->
                n.nId == n1.nId && n.title == n1.title
            })
            Assert.assertTrue(it.any { n ->
                n.nId == n2.nId && n.title == n2.title
            })
        }
    }

    @Test
    fun getOrphanNoteItems_withException_failure() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.getLiteNotes() } throws Exception()

        val result = repository.getLiteNotes().first()

        verify { localine.getLiteNotes() }
        Assert.assertTrue(result is AccessResult.Failure)
    }

    @Test
    fun getUsableCategorizedNotes_withoutError_success() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val n1 = Litentry(nId = 10L, title = "One")
        val n2 = Litentry(nId = 20L, title = "Two")
        every { localine.getLiteNotes(any()) } returns listOf(n1, n2)

        val result = repository.getLiteNotes(10L).first()

        verify { localine.getLiteNotes(any()) }
        Assert.assertTrue(result is AccessResult.Success<List<Litentity>>)
        (result as? AccessResult.Success<List<Litentity>>)?.d?.let {
            Assert.assertEquals(2, it.size)
            Assert.assertTrue(it.any { n ->
                n.nId == n1.nId && n.title == n1.title
            })
            Assert.assertTrue(it.any { n ->
                n.nId == n2.nId && n.title == n2.title
            })
        }
    }

    @Test
    fun getUsableCategorizedNotes_withException_failure() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.getLiteNotes(any()) } throws Exception()

        val result = repository.getLiteNotes(10L).first()

        verify { localine.getLiteNotes(any()) }
        Assert.assertTrue(result is AccessResult.Failure)
    }

    @Test
    fun saveValidCategory_withoutError_successWithTrue() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val c1 = Litentity(nId = 10L, title = "One")
        every { localine.upsertCategories(any()) } returns Unit

        val result = repository.saveCategory(c1).first()

        verify { localine.upsertCategories(any()) }
        Assert.assertTrue(result is AccessResult.Success<Boolean>)
        (result as? AccessResult.Success<Boolean>)?.d?.let {
            Assert.assertTrue(it)
        }
    }

    @Test
    fun saveInvalidCategory_withoutError_successWithFalse() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val c1 = Litentity(nId = 10L, title = "One".padEnd(TITLE_MAX_LENGTH + 1, 'n'))
        every { localine.upsertCategories(any()) } returns Unit

        val result = repository.saveCategory(c1).first()

        verify(exactly = 0) { localine.upsertCategories(any(), any()) }
        Assert.assertTrue(result is AccessResult.Success<Boolean>)
        (result as? AccessResult.Success<Boolean>)?.d?.let {
            Assert.assertFalse(it)
        }
    }

    @Test
    fun saveValidCategory_withException_failure() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val c1 = Litentity(nId = 10L, title = "One")
        every { localine.upsertCategories(any()) } throws Exception()

        val result = repository.saveCategory(c1).first()

        verify { localine.upsertCategories(any()) }
        Assert.assertTrue(result is AccessResult.Failure)
    }

    @Test
    fun loadExistentNotentity_withoutError_successWithData() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val n = Notentry(nId = 100L, title = "One", brief = "", ymdate = null)
        every { localine.getOneNotentry(any()) } returns n

        val result = repository.loadNotentity(100L).first()

        verify { localine.getOneNotentry(any()) }
        Assert.assertTrue(result is AccessResult.Success<Notentity>)
        (result as? AccessResult.Success<Notentity>)?.d?.let {
            Assert.assertEquals(Notentity(n), it)
        }
    }

    @Test
    fun loadNonexistentNotentity_withoutError_successWithFalse() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.getOneNotentry(any()) } returns null

        val result = repository.loadNotentity(100L).first()

        verify { localine.getOneNotentry(any()) }
        Assert.assertTrue(result is AccessResult.Warning)
    }

    @Test
    fun loadNotentity_withException_failure() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        every { localine.getOneNotentry(any()) } throws Exception()

        val result = repository.loadNotentity(100L).first()

        verify { localine.getOneNotentry(any()) }
        Assert.assertTrue(result is AccessResult.Failure)
    }

    @Test
    fun saveValidNotentry_withoutError_successWithTrue() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val n1 = Notentity(nId = 10L, title = "One", brief = "", ymdate = null)
        every { localine.upsertNotentries(any()) } returns Unit

        val result = repository.saveNotentry(10L, n1).first()

        verify { localine.upsertNotentries(any()) }
        Assert.assertTrue(result is AccessResult.Success<Boolean>)
        (result as? AccessResult.Success<Boolean>)?.d?.let {
            Assert.assertTrue(it)
        }
    }

    @Test
    fun saveInvalidNotentry_withoutError_successWithFalse() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val n1 = Notentity(nId = 10L, title = "",
            brief = "", ymdate = null)
        every { localine.upsertNotentries(any()) } returns Unit

        val result = repository.saveNotentry(10L, n1).first()

        verify(exactly = 0) { localine.upsertNotentries(any()) }
        Assert.assertTrue(result is AccessResult.Success<Boolean>)
        (result as? AccessResult.Success<Boolean>)?.d?.let {
            Assert.assertFalse(it)
        }
    }

    @Test
    fun saveValidNotentry_withException_failure() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = LocalRepositImpl(localine, dispatcher)
        val n1 = Notentity(nId = 10L, title = "One", brief = "", ymdate = null)
        every { localine.upsertNotentries(any()) } throws Exception()

        val result = repository.saveNotentry(10L, n1).first()

        verify { localine.upsertNotentries(any()) }
        Assert.assertTrue(result is AccessResult.Failure)
    }

}