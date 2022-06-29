package top.memore.droid_jotter.datany

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import top.memore.droid_jotter.TEXT_MAX_LENGTH
import top.memore.droid_jotter.cloudy.CloudAccessible
import top.memore.droid_jotter.cloudy.CloudSharable
import top.memore.droid_jotter.locally.LocalAccessible
import top.memore.droid_jotter.locally.Notentry
import top.memore.droid_jotter.models.Category
import top.memore.droid_jotter.models.Plainote

class PlainotesRepositTests {
    private lateinit var localine: LocalAccessible
    private lateinit var cloudata: CloudAccessible
    private lateinit var cloudeal: CloudSharable
    private lateinit var parental: NotesParental
    private lateinit var reposit: Repository<Plainote>

    @Before
    fun setup() {
        localine = mockk()
        cloudata = mockk()
        cloudeal = mockk()
        parental = mockk()
        reposit = PlainotesRepositImpl(localine, cloudata, parental, cloudeal)
    }

    @Test(expected = Exception::class)
    fun loadLocalOrphanNotentries_withException_failedToLoadNotes() {
        every { parental.currentParentId } returns null
        every { localine.getOrphanNotentries() } throws Exception("getCatentries failed")

        reposit.loadLocalEntities()
    }

    @Test(expected = Exception::class)
    fun loadLocalCategorizedNotes_withException_failedToLoadNotes() {
        every { parental.currentParentId } returns 10L
        every { localine.getCategorizedNotes(10L) } throws Exception("getCatentries failed")

        reposit.loadLocalEntities()
    }

    @Test
    fun loadLocalOrphanEntities_successfullyLoadNotes() {
        every { parental.currentParentId } returns null
        val n1 = Notentry(nId = 100L, title = "First", brief = "The is first Note", ymdate = null)
        val n2 = Notentry(nId = 200L, title = "Second", brief = "The is second Note", ymdate = null)
        every { localine.getOrphanNotentries() } returns listOf(n1, n2)

        val ns = reposit.loadLocalEntities()

        verify { localine.getOrphanNotentries() }
        Assert.assertEquals(2, ns.size)
    }

    @Test
    fun loadLocalCategorizedNotentries_successfullyLoadNotes() {
        every { parental.currentParentId } returns 10L
        val n1 = Notentry(
            nId = 100L,
            title = "First",
            brief = "The is first Note",
            ymdate = null
        ).apply {
            categoId = 10L
        }
        val n2 = Notentry(
            nId = 200L,
            title = "Second",
            brief = "The is second Note",
            ymdate = null
        ).apply {
            categoId = 10L
        }
        every { localine.getCategorizedNotes(10L) } returns listOf(n1, n2)

        val ns = reposit.loadLocalEntities()

        verify { localine.getCategorizedNotes(10L) }
        Assert.assertEquals(2, ns.size)
    }

    @Test
    fun loadCloudCategorizedEntities_successfullyLoadNotes() {
        val n1 = Plainote(nId = 10L, title = "A", brief = "A brief")
        val n2 = Plainote(nId = 20L, title = "B", brief = "B brief")
        val n3 = Plainote(nId = 30L, title = "C", brief = "A brief")
        every { cloudeal.plainoteUri } returns "https://"
        every { cloudeal.accessToken } returns "12345678"
        every { parental.currentParentId } returns 10L
        every { cloudata.getPlainotes(any(), any(), any()) } returns listOf(n1, n2, n3)

        val plainotes = reposit.loadCloudEntities()

        verify { cloudata.getPlainotes(any(), any(), any()) }
        Assert.assertEquals(3, plainotes.size)
        Assert.assertTrue(plainotes.contains(n1))
        Assert.assertTrue(plainotes.contains(n2))
        Assert.assertTrue(plainotes.contains(n3))
    }

    @Test
    fun loadCloudOrphanEntities_successfullyLoadNotes() {
        val n1 = Plainote(nId = 10L, title = "A", brief = "A brief")
        val n2 = Plainote(nId = 20L, title = "B", brief = "B brief")
        val n3 = Plainote(nId = 30L, title = "C", brief = "A brief")
        every { cloudeal.plainoteUri } returns "https://"
        every { cloudeal.accessToken } returns "12345678"
        every { parental.currentParentId } returns null
        every { cloudata.getPlainotes(any(), any()) } returns listOf(n1, n2, n3)

        val plainotes = reposit.loadCloudEntities()

        verify { cloudata.getPlainotes(any(), any()) }
        Assert.assertEquals(3, plainotes.size)
        Assert.assertTrue(plainotes.contains(n1))
        Assert.assertTrue(plainotes.contains(n2))
        Assert.assertTrue(plainotes.contains(n3))
    }

    @Test
    fun insertLocalOrphanEntity_withValidPlainote_successfullyInsert() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { parental.currentParentId } returns null
        every { localine.insertNotentry(any()) } returns Unit

        val inserted = reposit.insertLocalEntity(n)

        verify { localine.insertNotentry(any()) }
        Assert.assertTrue(inserted)
    }

    @Test
    fun insertLocalEntity_withEmptyTitle_noInserted() {
        val n = Plainote(nId = 10L, title = "", brief = "A brief")
        every { localine.insertNotentry(any()) } returns Unit

        val inserted = reposit.insertLocalEntity(n)

        verify(exactly = 0) { localine.insertNotentry(any()) }
        Assert.assertFalse(inserted)
    }

    @Test
    fun insertLocalEntity_withLengthyTitle_noInserted() {
        val n = Plainote(nId = 110L, title = "A".padEnd(TEXT_MAX_LENGTH + 1, 'N'), brief = "brief")
        every { localine.insertNotentry(any()) } returns Unit

        val inserted = reposit.insertLocalEntity(n)

        verify(exactly = 0) { localine.insertNotentry(any()) }
        Assert.assertFalse(inserted)
    }

    @Test(expected = Exception::class)
    fun insertLocalEntity_withException_failedToInsert() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { localine.insertNotentry(any()) } throws Exception()

        reposit.insertLocalEntity(n)
    }

    @Test
    fun updateLocalOrphanEntity_withValidPlainote_successfullyUpdate() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { parental.currentParentId } returns null
        every { localine.updateNotentry(any()) } returns 1

        val updated = reposit.updateLocalEntity(n)

        verify { localine.updateNotentry(any()) }
        Assert.assertTrue(updated)
    }

    @Test
    fun updateLocalCategorizedEntity_withValidPlainote_successfullyUpdate() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { parental.currentParentId } returns 10L
        every { localine.updateNotentry(any()) } returns 1

        val updated = reposit.updateLocalEntity(n)

        verify { localine.updateNotentry(any()) }
        Assert.assertTrue(updated)
    }

    @Test
    fun updateLocalEntity_withEmptyTitle_noUpdated() {
        val n = Plainote(nId = 10L, title = "", brief = "A brief")
        every { localine.updateNotentry(any()) } returns 0

        val updated = reposit.updateLocalEntity(n)

        verify(exactly = 0) { localine.insertNotentry(any()) }
        Assert.assertFalse(updated)
    }

    @Test
    fun updateLocalEntity_withLengthyTitle_noUpdated() {
        val n = Plainote(nId = 110L, title = "A".padEnd(TEXT_MAX_LENGTH + 1, 'N'), brief = "brief")
        every { localine.updateNotentry(any()) } returns 0

        val updated = reposit.updateLocalEntity(n)

        verify(exactly = 0) { localine.insertNotentry(any()) }
        Assert.assertFalse(updated)
    }

    @Test(expected = Exception::class)
    fun updateLocalEntity_withException_failedToUpdate() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { localine.updateNotentry(any()) } throws Exception()

        reposit.updateLocalEntity(n)
    }

    @Test
    fun deleteLocalExistingPlainote_oneDeleted() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { localine.deleteNotentry(n.nId) } returns 1

        val deleted = reposit.deleteLocalEntity(n)

        verify { localine.deleteNotentry(n.nId) }
        Assert.assertTrue(deleted)
    }

    @Test
    fun deleteLocalNonexistent_noPlainoteDeleted() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { localine.deleteNotentry(n.nId) } returns 0

        val deleted = reposit.deleteLocalEntity(n)

        verify { localine.deleteNotentry(n.nId) }
        Assert.assertFalse(deleted)
    }

    @Test(expected = Exception::class)
    fun deleteLocalPlainote_withException_failedToDeleted() {
        val n = Plainote(nId = 10L, title = "A", brief = "A brief")
        every { localine.deleteCatentry(n.nId) } throws Exception("deleting")

        reposit.deleteLocalEntity(n)
    }

}