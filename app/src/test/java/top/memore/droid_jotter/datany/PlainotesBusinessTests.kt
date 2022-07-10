package top.memore.droid_jotter.datany

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import top.memore.droid_jotter.*

class PlainotesBusinessTests {
//    private lateinit var business: MateBusiness<LiteNote>
//    private val n1 = LiteNote(
//        nId = 11L,
//        title = "A",
//        brief = "brief A",
//        ymdate = ymd(2001, 1, 11)
//    ).apply {
//        deleting = false
//        millitime = 101L
//    }
//    private val n2 = LiteNote(
//        nId = 12L,
//        title = "B",
//        brief = "brief B",
//        ymdate = ymd(2002, 2, 12)
//    ).apply {
//        deleting = false
//        millitime = 102L
//    }
//    private val n3 = LiteNote(
//        nId = 13L,
//        title = "C",
//        brief = "brief C",
//        ymdate = ymd(2003, 3, 13)
//    ).apply {
//        deleting = false
//        millitime = 103L
//    }
//
//    @Before
//    fun setup() {
//        business = MateBusinessImpl(listOf(n1, n2, n3))
//    }
//
//    @Test
//    fun mateNotentriesWithSource_sourceCreatingNewEntities_obtainCreated() {
//        val d1 = n1.copy(nId = n1.nId + 100L, title = "Another title")
//        val d2 = n2.copy(nId = n2.nId + 100L, brief = "Another brief")
//        val d3 = n3.copy(nId = n3.nId + 100L, ymdate = 1234)
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2, d3)
//        )
//
//        Assert.assertFalse(created.isEmpty())
//        Assert.assertTrue(updated.isEmpty())
//        Assert.assertTrue(deleted.isEmpty())
//        Assert.assertEquals(3, created.size)
//    }
//
//    @Test
//    fun mateNotentriesWithSource_sourceCreatingEntities_obtainCreatedWithoutExistent() {
//        val d1 = n1
//        val d2 = n2.copy(nId = n2.nId + 100L, title = "Another title")
//        val d3 = n3.copy(nId = n3.nId + 100L, brief = "Another brief")
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2, d3)
//        )
//
//        Assert.assertFalse(created.isEmpty())
//        Assert.assertEquals(2, created.size)
//        Assert.assertTrue(updated.isEmpty())
//        Assert.assertTrue(deleted.isEmpty())
//    }
//
//    @Test
//    fun mateNotentriesWithSource_whenNewSourceEntitiesIsDeleting_noCreated() {
//        val d1 = n1.copy(nId = n1.nId + 100L, title = "Another title").apply {
//            deleting = true
//        }
//        val d2 = n2.copy(nId = n2.nId + 100L, brief = "Another brief").apply {
//            deleting = true
//        }
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2)
//        )
//
//        Assert.assertTrue(created.isEmpty())
//        Assert.assertTrue(updated.isEmpty())
//        Assert.assertTrue(deleted.isEmpty())
//    }
//
//    @Test
//    fun mateNotentriesWithSource_newerSourceEntitiesToUpdate_obtainUpdated() {
//        val d1 = n1.copy(title = "Another title").apply {
//            millitime = n1.millitime + 10L
//        }
//        val d2 = n2.copy(brief = "Another brief").apply {
//            millitime = n1.millitime + 10L
//        }
//        val d3 = n3.copy(ymdate = 1234).apply {
//            millitime = n1.millitime + 10L
//        }
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2, d3)
//        )
//
//        Assert.assertTrue(created.isEmpty())
//        Assert.assertFalse(updated.isEmpty())
//        Assert.assertEquals(3, updated.size)
//        Assert.assertTrue(deleted.isEmpty())
//    }
//
//    @Test
//    fun mateNotentriesWithSource_olderSourceEntitiesToUpdate_noUpdated() {
//        val d1 = n1.copy(title = "Another title").apply {
//            millitime = n1.millitime - 10L
//        }
//        val d2 = n2.copy(brief = "Another brief").apply {
//            millitime = n1.millitime - 10L
//        }
//        val d3 = n3.copy(ymdate = 1234).apply {
//            millitime = n1.millitime - 10L
//        }
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2, d3)
//        )
//
//        Assert.assertTrue(created.isEmpty())
//        Assert.assertTrue(updated.isEmpty())
//        Assert.assertTrue(deleted.isEmpty())
//    }
//
//    @Test
//    fun mateNotentriesWithSource_olderSourceEntitiesToUpdate_deleteFirst() {
//        val d1 = n1.copy(title = "Another title").apply {
//            millitime = n1.millitime + 10L
//        }
//        val d2 = n2.copy(brief = "Another brief").apply {
//            deleting = true
//            millitime = n1.millitime + 10L
//        }
//        val d3 = n3.copy(ymdate = 1234).apply {
//            deleting = true
//            millitime = n1.millitime + 10L
//        }
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2, d3)
//        )
//
//        Assert.assertTrue(created.isEmpty())
//        Assert.assertFalse(updated.isEmpty())
//        Assert.assertEquals(1, updated.size)
//        Assert.assertFalse(deleted.isEmpty())
//        Assert.assertEquals(2, deleted.size)
//    }
//
//    @Test
//    fun mateNotentriesWithSource_newerSourceEntitiesToDelete_obtainDeleted() {
//        val d1 = n1.copy(title = "Another title").apply {
//            deleting = true
//            millitime = n1.millitime - 10L
//        }
//        val d2 = n2.copy(brief = "Another brief").apply {
//            deleting = true
//            millitime = n1.millitime + 10L
//        }
//        val d3 = n3.copy(ymdate = 1234).apply {
//            deleting = true
//            millitime = n1.millitime + 10L
//        }
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2, d3)
//        )
//
//        Assert.assertTrue(created.isEmpty())
//        Assert.assertTrue(updated.isEmpty())
//        Assert.assertFalse(deleted.isEmpty())
//        Assert.assertEquals(2, deleted.size)
//    }
//
//    @Test
//    fun mateNotentriesWithSource_olderSourceEntitiesToDelete_noDeleted() {
//        val d1 = n1.copy(title = "Another title").apply {
//            deleting = true
//            millitime = n1.millitime
//        }
//        val d2 = n2.copy(brief = "Another brief").apply {
//            deleting = true
//            millitime = n1.millitime - 10L
//        }
//        val d3 = n3.copy(ymdate = 1234).apply {
//            deleting = true
//            millitime = n1.millitime - 10L
//        }
//
//        val (created, updated, deleted) = business.mateWithSource(
//            listOf(d1, d2, d3)
//        )
//
//        Assert.assertTrue(created.isEmpty())
//        Assert.assertTrue(updated.isEmpty())
//        Assert.assertTrue(deleted.isEmpty())
//    }

}