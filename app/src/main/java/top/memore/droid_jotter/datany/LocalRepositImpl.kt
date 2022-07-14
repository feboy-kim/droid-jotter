package top.memore.droid_jotter.datany

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import top.memore.droid_jotter.locally.Category
import top.memore.droid_jotter.locally.LiteAccessible
import top.memore.droid_jotter.locally.Notentry
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity
import javax.inject.Inject

class LocalRepositImpl @Inject constructor(
    private val localine: LiteAccessible,
    private val dispatcher: CoroutineDispatcher
) : LocalRepository {

    override suspend fun loadCategories(): List<Litentity> = withContext(dispatcher) {
        localine.getCategories().map {
            Litentity(it)
        }
    }

    override suspend fun loadLiteNotes(pId: Long): List<Litentity> = withContext(dispatcher) {
        localine.getLiteNotes(pId).map {
            Litentity(it).apply {
                millitime = it.millitime
            }
        }
    }

    override suspend fun loadLiteNotes(): List<Litentity> = withContext(dispatcher) {
        localine.getLiteNotes().map {
            Litentity(it).apply {
                millitime = it.millitime
            }
        }
    }

    override suspend fun saveCategory(c: Litentity): Boolean =
        withContext(dispatcher) {
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

    override suspend fun loadNotentity(id: Long): Notentity? = withContext(dispatcher) {
        localine.getOneNotentry(id)?.let {
            Notentity(it)
        }
    }

    override suspend fun saveNotentry(pId: Long?, n: Notentity): Boolean = withContext(dispatcher) {
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

    override suspend fun updateCategoryAsUseless(id: Long) = withContext(dispatcher) {
        localine.updateCategoryAsUseless(id) > 0
    }

    override suspend fun updateNotentryAsUseless(id: Long) = withContext(dispatcher) {
        localine.updateNotentryAsUseless(id) > 0
    }

    override suspend fun updateCategoryAsUsable(id: Long) = withContext(dispatcher) {
        localine.updateCategoryAsUsable(id) > 0
    }

    override suspend fun updateNotentryAsUsable(id: Long) = withContext(dispatcher) {
        localine.updateNotentryAsUsable(id) > 0
    }

}
