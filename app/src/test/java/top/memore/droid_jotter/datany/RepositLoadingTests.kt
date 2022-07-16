package top.memore.droid_jotter.datany

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import top.memore.droid_jotter.locally.Litentry
import top.memore.droid_jotter.locally.LiteAccessible
import top.memore.droid_jotter.locally.Notentry
import top.memore.droid_jotter.models.Notentity

class RepositLoadingTests {
    private lateinit var localine: LiteAccessible

    @Before
    fun setup() {
        localine = mockk()
    }

    @Test
    fun loadCategories_withoutError_success() = runTest {
        val repository = LocalRepositImpl(localine)
        val c1 = Litentry(nId = 10L, title = "One", millitime = +1000)
        val c2 = Litentry(nId = 20L, title = "Two", millitime = -2000)
        every { localine.getCategories() } returns listOf(c1, c2)

        val result = repository.loadCategories()

        verify { localine.getCategories() }
        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result.any { c ->
            c.nId == c1.nId && c.title == c1.title && c.isUsable
        })
        Assert.assertTrue(result.any { c ->
            c.nId == c2.nId && c.title == c2.title && !c.isUsable
        })
    }

    @Test
    fun loadOrphanLiteNotes_withoutError_success() = runTest {
        val repository = LocalRepositImpl(localine)
        val n1 = Litentry(nId = 10L, title = "One", millitime = +1000)
        val n2 = Litentry(nId = 20L, title = "Two", millitime = -2000)
        every { localine.getLiteNotes() } returns listOf(n1, n2)

        val result = repository.loadLiteNotes()

        verify { localine.getLiteNotes() }
        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result.any { n ->
            n.nId == n1.nId && n.title == n1.title && n.isUsable
        })
        Assert.assertTrue(result.any { n ->
            n.nId == n2.nId && n.title == n2.title && !n.isUsable
        })
    }

    @Test
    fun loadUsableCategorizedNotes_withoutError_success() = runTest {
        val repository = LocalRepositImpl(localine)
        val n1 = Litentry(nId = 10L, title = "One", millitime = +1000)
        val n2 = Litentry(nId = 20L, title = "Two", millitime = -2000)
        every { localine.getLiteNotes(any()) } returns listOf(n1, n2)

        val result = repository.loadLiteNotes(10L)

        verify { localine.getLiteNotes(any()) }
        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result.any { n ->
            n.nId == n1.nId && n.title == n1.title && n.isUsable
        })
        Assert.assertTrue(result.any { n ->
            n.nId == n2.nId && n.title == n2.title && !n.isUsable
        })
    }

    @Test
    fun loadExistentNotentity_withoutError_successWithData() = runTest {
        val repository = LocalRepositImpl(localine)
        val n = Notentry(
            nId = 100L, title = "One", brief = "",
            ymday = Notentity.ymd(2000, 11, 22)
        )
        every { localine.getOneNotentry(any()) } returns n

        val result = repository.loadNotentity(100L)

        verify { localine.getOneNotentry(any()) }
        Assert.assertNotNull(result)
        result?.let {
            Assert.assertEquals(Notentity(n), it)
            Assert.assertEquals(2000.toShort(), it.ymdTriple?.first)
            Assert.assertEquals(11.toByte(), it.ymdTriple?.second)
            Assert.assertEquals(22.toByte(), it.ymdTriple?.third)
        }
    }

    @Test
    fun loadNonexistentNotentity_withoutError_successWithNull() = runTest {
        val repository = LocalRepositImpl(localine)
        every { localine.getOneNotentry(any()) } returns null

        val result = repository.loadNotentity(100L)

        verify { localine.getOneNotentry(any()) }
        Assert.assertNull(result)
    }

}