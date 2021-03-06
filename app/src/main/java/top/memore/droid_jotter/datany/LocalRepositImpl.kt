package top.memore.droid_jotter.datany

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.memore.droid_jotter.locally.Category
import top.memore.droid_jotter.locally.LiteAccessible
import top.memore.droid_jotter.locally.Notentry
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity
import javax.inject.Inject

class LocalRepositImpl @Inject constructor(
    private val localine: LiteAccessible
) : LocalRepository {

    override suspend fun loadCategories(): List<Litentity> =
        withContext(Dispatchers.IO) {
            localine.getCategories().map {
                Litentity(it).apply { millitime = it.millitime }
            }
        }

    override suspend fun loadLiteNotes(pId: Long): List<Litentity> =
        withContext(Dispatchers.IO) {
            if (pId > 0) {
                localine.getLiteNotes(pId).map {
                    Litentity(it).apply {
                        millitime = it.millitime
                    }
                }
            } else {
                localine.getLiteNotes().map {
                    Litentity(it).apply {
                        millitime = it.millitime
                    }
                }
            }
        }

    override suspend fun saveCategory(c: Litentity): Boolean =
        withContext(Dispatchers.IO) {
            if (c.isValid) {
                val category = Category(
                    nId = c.nId,
                    title = c.title,
                    millitime = System.currentTimeMillis()
                )
                localine.upsertCategories(category)
                true
            } else false
        }

    override suspend fun loadNotentity(id: Long): Notentity? =
        withContext(Dispatchers.IO) {
            localine.getOneNotentry(id)?.let {
                Notentity(it)
            }
        }

    override suspend fun saveNotentry(pId: Long?, n: Notentity): Boolean =
        withContext(Dispatchers.IO) {
            if (n.isValid) {
                val note = Notentry(
                    nId = n.nId,
                    title = n.title,
                    brief = n.brief,
                    ymday = n.ymdate
                ).also { n ->
                    n.cateId = pId
                }
                localine.upsertNotentries(note)
                true
            } else false
        }

    override suspend fun updateCategoryUsability(id: Long, usable: Boolean) =
        withContext(Dispatchers.IO) {
            if (usable) localine.updateCategoryAsUsable(id) > 0
            else localine.updateCategoryAsUseless(id) > 0
        }

    override suspend fun updateNotentryUsability(id: Long, usable: Boolean) =
        withContext(Dispatchers.IO) {
            if (usable) localine.updateNotentryAsUsable(id) > 0
            else localine.updateNotentryAsUseless(id) > 0
        }

}
