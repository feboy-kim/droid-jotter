package top.memore.droid_jotter.datany

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import top.memore.droid_jotter.datany.MateBusiness
import top.memore.droid_jotter.datany.MateBusinessImpl
import top.memore.droid_jotter.models.Category

class CategoriesBusinessTests {
    private lateinit var business: MateBusiness<Category>
    private val c1 = Category(cId = 11L, name = "A").apply {
        deleting = false
        millitime = 101L
    }
    private val c2 = Category(cId = 12L, name = "B").apply {
        deleting = false
        millitime = 102L
    }
    private val c3 = Category(cId = 13L, name = "C").apply {
        deleting = true
        millitime = 103L
    }

    @Before
    fun setup() {
        business = MateBusinessImpl(listOf(c1, c2, c3))
    }

    @Test
    fun mateCategoriesWithSource_sourceCreatingNewEntities_obtainCreated() {
        val d1 = Category(
            cId = c1.cId + 100L,
            name = "A1",
        )
        val d2 = Category(
            cId = c2.cId + 100L,
            name = "B2",
        )
        val d3 = Category(
            cId = c3.cId + 100L,
            name = "C3",
        )

        val (created, updated, deleted) = business.mateWithSource(
            listOf(d1, d2, d3)
        )

        Assert.assertFalse(created.isEmpty())
        Assert.assertTrue(updated.isEmpty())
        Assert.assertTrue(deleted.isEmpty())
        Assert.assertEquals(3, created.size)
    }

    @Test
    fun mateCategoriesWithSource_sourceCreatingEntities_obtainCreatedWithoutExistent() {
        val d1 = c1
        val d2 = Category(
            cId = c2.cId + 100L,
            name = "B2",
        )
        val d3 = Category(
            cId = c3.cId + 100L,
            name = "C3",
        )

        val (created, updated, deleted) = business.mateWithSource(
            listOf(d1, d2, d3)
        )

        Assert.assertFalse(created.isEmpty())
        Assert.assertEquals(2, created.size)
        Assert.assertTrue(updated.isEmpty())
        Assert.assertTrue(deleted.isEmpty())
    }

    @Test
    fun mateCategoriesWithSource_whenNewSourceEntitiesIsDeleting_noCreated() {
        val d2 = Category(
            cId = 2L,
            name = "B2",
        ).apply { deleting = true }
        val d3 = Category(
            cId = 3L,
            name = "C3",
        ).apply { deleting = true }

        val (created, updated, deleted) = business.mateWithSource(
            listOf(d2, d3)
        )

        Assert.assertTrue(created.isEmpty())
        Assert.assertTrue(updated.isEmpty())
        Assert.assertTrue(deleted.isEmpty())
    }

    @Test
    fun mateCategoriesWithSource_newerSourceEntitiesToUpdate_obtainUpdated() {
        val d1 = Category(
            cId = c1.cId,
            name = "A1",
        )
        val d2 = Category(
            cId = c2.cId,
            name = "A2",
        )
        val d3 = Category(
            cId = c3.cId,
            name = c3.name,
        )
        val (created, updated, deleted) = business.mateWithSource(
            listOf(d1, d2, d3)
        )

        Assert.assertTrue(created.isEmpty())
        Assert.assertFalse(updated.isEmpty())
        Assert.assertEquals(3, updated.size)
        Assert.assertTrue(deleted.isEmpty())
    }

    @Test
    fun mateCategoriesWithSource_olderSourceEntitiesToUpdate_noUpdated() {
        val d1 = Category(
            cId = c1.cId,
            name = "A1",
        ).apply {
            millitime = c1.millitime - 2L
        }
        val d2 = Category(
            cId = c2.cId,
            name = "A2",
        ).apply {
            millitime = c2.millitime - 2L
        }
        val d3 = Category(
            cId = c3.cId,
            name = "C3",
        ).apply {
            millitime = c3.millitime - 2L
        }

        val (created, updated, deleted) = business.mateWithSource(
            listOf(d1, d2, d3)
        )

        Assert.assertTrue(created.isEmpty())
        Assert.assertTrue(updated.isEmpty())
        Assert.assertTrue(deleted.isEmpty())
    }

    @Test
    fun mateCategoriesWithSource_olderSourceEntitiesToUpdate_deleteFirst() {
        val d1 = Category(
            cId = c1.cId,
            name = "A1",
        ).apply {
            millitime = c1.millitime + 2L
        }
        val d2 = Category(
            cId = c2.cId,
            name = "A2",
        ).apply {
            deleting = true
            millitime = c2.millitime + 2L
        }
        val d3 = Category(
            cId = c3.cId,
            name = "C3",
        ).apply {
            deleting = true
            millitime = c3.millitime + 2L
        }

        val (created, updated, deleted) = business.mateWithSource(
            listOf(d1, d2, d3)
        )

        Assert.assertTrue(created.isEmpty())
        Assert.assertFalse(updated.isEmpty())
        Assert.assertEquals(1, updated.size)
        Assert.assertFalse(deleted.isEmpty())
        Assert.assertEquals(2, deleted.size)
    }

    @Test
    fun mateCategoriesWithSource_newerSourceEntitiesToDelete_obtainDeleted() {
        val d1 = Category(
            cId = c1.cId,
            name = "A1",
        ).apply {
            millitime = c1.millitime
        }
        val d2 = Category(
            cId = c2.cId,
            name = "B2",
        ).apply {
            deleting = true
            millitime = c2.millitime + 2L
        }
        val d3 = Category(
            cId = c3.cId,
            name = "C3",
        ).apply {
            deleting = true
            millitime = c3.millitime + 2L
        }

        val (created, updated, deleted) = business.mateWithSource(
            listOf(d1, d2, d3)
        )

        Assert.assertTrue(created.isEmpty())
        Assert.assertTrue(updated.isEmpty())
        Assert.assertFalse(deleted.isEmpty())
        Assert.assertEquals(2, deleted.size)
    }

    @Test
    fun mateCategoriesWithSource_olderSourceEntitiesToDelete_noDeleted() {
        val d1 = Category(
            cId = c1.cId,
            name = "A1",
        ).apply {
            millitime = c1.millitime
        }
        val d2 = Category(
            cId = c2.cId,
            name = "B",
        ).apply {
            deleting = true
            millitime = c2.millitime - 2L
        }
        val d3 = Category(
            cId = c3.cId,
            name = "C",
        ).apply {
            deleting = true
            millitime = c3.millitime - 2L
        }

        val (created, updated, deleted) = business.mateWithSource(
            listOf(d1, d2, d3)
        )

        Assert.assertTrue(created.isEmpty())
        Assert.assertTrue(updated.isEmpty())
        Assert.assertTrue(deleted.isEmpty())
    }

}
