package top.memore.droid_jotter.ui.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import top.memore.droid_jotter.datany.LocalRepository
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity

internal abstract class LocalRepositFake : LocalRepository {

    override suspend fun getCategories(): Flow<AccessResult<List<Litentity>>> {
        return flowOf()
    }

    override suspend fun getLiteNotes(pId: Long): Flow<AccessResult<List<Litentity>>> {
        return flowOf()
    }

    override suspend fun getLiteNotes(): Flow<AccessResult<List<Litentity>>> {
        return flowOf()
    }

    override suspend fun saveCategory(c: Litentity): Flow<AccessResult<Boolean>> {
        return flowOf()
    }

    override suspend fun loadNotentity(id: Long): Flow<AccessResult<Notentity>> {
        return flowOf()
    }

    override suspend fun saveNotentry(pId: Long?, n: Notentity): Flow<AccessResult<Boolean>> {
        return flowOf()
    }

    override suspend fun updateCategoryAsUseless(nId: Long): Flow<AccessResult<Int>> {
        return flowOf()
    }

    override suspend fun updateNotentryAsUseless(nId: Long): Flow<AccessResult<Int>> {
        return flowOf()
    }
}