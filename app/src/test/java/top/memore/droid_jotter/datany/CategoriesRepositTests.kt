package top.memore.droid_jotter.datany

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import top.memore.droid_jotter.NAME_MAX_LENGTH
import top.memore.droid_jotter.cloudy.CloudAccessible
import top.memore.droid_jotter.cloudy.CloudSharable
import top.memore.droid_jotter.locally.Catentry
import top.memore.droid_jotter.locally.LocalAccessible
import top.memore.droid_jotter.models.Category

class CategoriesRepositTests {
    private lateinit var localine: LocalAccessible
    private lateinit var cloudata: CloudAccessible
    private lateinit var cloudeal: CloudSharable
    private lateinit var reposit: Repository<Category>

    @Before
    fun setup() {
        localine = mockk()
        cloudata = mockk()
        cloudeal = mockk()
        reposit = CategoriesRepositImpl(localine, cloudata, cloudeal)
    }

    @Test(expected = Exception::class)
    fun loadLocalEntities_withException_failedToLoadCategories() {
        every { localine.getCatentries() } throws Exception("getCatentries failed")

        reposit.loadLocalEntities()

        verify { localine.getCatentries() }
    }

    @Test
    fun loadLocalEntities_successfullyLoadCategories() {
        val c1 = Catentry(cId = 10L, name = "One")
        val c2 = Catentry(cId = 20L, name = "Two")
        every { localine.getCatentries() } returns listOf(c1, c2)

        val cs = reposit.loadLocalEntities()

        verify { localine.getCatentries() }
        Assert.assertEquals(2, cs.size)
    }

    @Test
    fun loadCloudEntities_successfullyLoadCategories() {
        val c1 = Category(cId = 10L, name = "A")
        val c2 = Category(cId = 20L, name = "B")
        val c3 = Category(cId = 30L, name = "C")
        every { cloudeal.categoryUri } returns "https://"
        every { cloudeal.accessToken } returns "12345678"
        every { cloudata.getCategories(any(), any()) } returns listOf(c1, c2, c3)

        val categories = reposit.loadCloudEntities()

        verify { cloudata.getCategories(any(), any()) }
        Assert.assertEquals(3, categories.size)
        Assert.assertTrue(categories.contains(c1))
        Assert.assertTrue(categories.contains(c2))
        Assert.assertTrue(categories.contains(c3))
    }

    @Test
    fun insertLocalEntity_withValidName_successfullyInsert() {
        val c1 = Category(cId = 10L, name = "A")
        every { localine.insertCatentry(c1.catentry) } returns Unit

        val inserted = reposit.insertLocalEntity(c1)

        verify { localine.insertCatentry(c1.catentry) }
        Assert.assertTrue(inserted)
    }

    @Test
    fun insertLocalEntity_withEmptyName_noInserted() {
        val c1 = Category(cId = 10L, name = "")
        every { localine.insertCatentry(c1.catentry) } returns Unit

        val inserted = reposit.insertLocalEntity(c1)

        verify(exactly = 0) { localine.insertCatentry(c1.catentry) }
        Assert.assertFalse(inserted)
    }

    @Test
    fun insertLocalEntity_withLengthyName_noInserted() {
        val c1 = Category(cId = 10L, name = "A".padEnd(NAME_MAX_LENGTH + 1, 'N'))
        every { localine.insertCatentry(c1.catentry) } returns Unit

        val inserted = reposit.insertLocalEntity(c1)

        verify(exactly = 0) { localine.insertCatentry(c1.catentry) }
        Assert.assertFalse(inserted)
    }

    @Test(expected = Exception::class)
    fun insertLocalEntity_withException_failedToInsert() {
        val c1 = Category(cId = 10L, name = "A")
        every { localine.insertCatentry(c1.catentry) } throws Exception()

        reposit.insertLocalEntity(c1)
    }

    @Test
    fun updateLocalEntity_withValidName_successfullyUpdate() {
        val c1 = Category(cId = 10L, name = "A")
        every { localine.updateCatentry(c1.catentry) } returns 1

        val updated = reposit.updateLocalEntity(c1)

        verify { localine.updateCatentry(c1.catentry) }
        Assert.assertTrue(updated)
    }

    @Test
    fun updateLocalEntity_withEmptyName_noUpdated() {
        val c1 = Category(cId = 10L, name = "")
        every { localine.updateCatentry(c1.catentry) } returns 0

        val updated = reposit.updateLocalEntity(c1)

        verify(exactly = 0) { localine.updateCatentry(c1.catentry) }
        Assert.assertFalse(updated)
    }

    @Test
    fun updateLocalEntity_withLengthyName_noUpdated() {
        val c1 = Category(cId = 10L, name = "A".padEnd(NAME_MAX_LENGTH + 1, 'N'))
        every { localine.updateCatentry(c1.catentry) } returns 0

        val updated = reposit.updateLocalEntity(c1)

        verify(exactly = 0) { localine.updateCatentry(c1.catentry) }
        Assert.assertFalse(updated)
    }

    @Test(expected = Exception::class)
    fun updateLocalEntity_withException_failedToUpdate() {
        val c1 = Category(cId = 10L, name = "A")
        every { localine.updateCatentry(c1.catentry) } throws Exception()

        reposit.updateLocalEntity(c1)
    }

    @Test
    fun deleteLocalExistingCategory_oneDeleted() {
        val c1 = Category(cId = 10L, name = "")
        every { localine.deleteCatentry(c1.cId) } returns 1

        val deleted = reposit.deleteLocalEntity(c1)

        verify { localine.deleteCatentry(c1.cId) }
        Assert.assertTrue(deleted)
    }

    @Test
    fun deleteLocalNonexistent_noCategoryDeleted() {
        val c1 = Category(cId = 10L, name = "")
        every { localine.deleteCatentry(c1.cId) } returns 0

        val deleted = reposit.deleteLocalEntity(c1)

        verify { localine.deleteCatentry(c1.cId) }
        Assert.assertFalse(deleted)
    }

    @Test(expected = Exception::class)
    fun deleteLocalCategory_withException_failedToDeleted() {
        val c1 = Category(cId = 10L, name = "")
        every { localine.deleteCatentry(c1.cId) } throws Exception("deleting")

        reposit.deleteLocalEntity(c1)
    }

}