package top.memore.droid_jotter.datany

import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity

interface LocalRepository {
    suspend fun loadCategories(): List<Litentity>
    suspend fun loadLiteNotes(pId: Long = 0L): List<Litentity>
    suspend fun saveCategory(c: Litentity): Boolean
    suspend fun loadNotentity(id: Long): Notentity?
    suspend fun saveNotentry(pId: Long?, n: Notentity): Boolean
    suspend fun updateCategoryUsability(id: Long, usable: Boolean): Boolean
    suspend fun updateNotentryUsability(id: Long, usable: Boolean): Boolean
}