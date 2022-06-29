package top.memore.droid_jotter.model

import org.junit.Assert
import org.junit.Test
import top.memore.droid_jotter.NAME_MAX_LENGTH
import top.memore.droid_jotter.models.*

class CategoryTests {
    @Test
    fun newInstance_withValidName_newCategoryCreated() {
        val c = Category.newInstance(n = "Brief")

        Assert.assertNotNull(c)
        c?.let {
            Assert.assertTrue(it.cId <= System.currentTimeMillis())
        }
    }

    @Test
    fun newInstance_withInvalidName_noCategoryCreated() {
        val c1 = Category.newInstance(n = "")
        val c2 = Category.newInstance(n = "Brief".padEnd(NAME_MAX_LENGTH + 1, 'N'))

        Assert.assertNull(c1)
        Assert.assertNull(c2)
    }
}