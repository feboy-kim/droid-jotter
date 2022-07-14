package top.memore.droid_jotter.datany

import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity

interface LocalRepository {
    suspend fun loadCategories(): List<Litentity>
    suspend fun loadLiteNotes(pId: Long): List<Litentity>
    suspend fun loadLiteNotes(): List<Litentity>
    suspend fun saveCategory(c: Litentity): Boolean
    suspend fun loadNotentity(id: Long): Notentity?
    suspend fun saveNotentry(pId: Long?, n: Notentity): Boolean
    suspend fun updateCategoryAsUseless(id: Long): Boolean
    suspend fun updateNotentryAsUseless(id: Long): Boolean
    suspend fun updateCategoryAsUsable(id: Long): Boolean
    suspend fun updateNotentryAsUsable(id: Long): Boolean
}