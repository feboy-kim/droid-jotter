package top.memore.droid_jotter.datany

import kotlinx.coroutines.flow.Flow
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity

interface LocalRepository {
    suspend fun getCategories(): Flow<AccessResult<List<Litentity>>>
    suspend fun getLiteNotes(pId: Long): Flow<AccessResult<List<Litentity>>>
    suspend fun getLiteNotes(): Flow<AccessResult<List<Litentity>>>
    suspend fun saveCategory(c: Litentity): Flow<AccessResult<Boolean>>
    suspend fun loadNotentity(id: Long): Flow<AccessResult<Notentity>>
    suspend fun saveNotentry(pId: Long?, n: Notentity): Flow<AccessResult<Boolean>>
    suspend fun updateCategoryAsUseless(nId: Long): Flow<AccessResult<Int>>
    suspend fun updateNotentryAsUseless(nId: Long): Flow<AccessResult<Int>>
}