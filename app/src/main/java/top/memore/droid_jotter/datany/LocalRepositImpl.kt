package top.memore.droid_jotter.datany

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import top.memore.droid_jotter.locally.Category
import top.memore.droid_jotter.locally.LocalAccessible
import top.memore.droid_jotter.locally.Notentry
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.models.Notentity
import javax.inject.Inject

class LocalRepositImpl @Inject constructor(
    private val localine: LocalAccessible,
    private val dispatcher: CoroutineDispatcher
) : LocalRepository {

    override suspend fun getCategories(): Flow<AccessResult<List<Litentity>>> {
        val d = withContext(dispatcher) {
            try {
                AccessResult.Success(localine.getCategories().map {
                    Litentity(it)
                })
            } catch (e: Exception) {
                AccessResult.Failure(e)
            }
        }
        return flow {
            emit(d)
        }
    }

    override suspend fun getLiteNotes(pId: Long): Flow<AccessResult<List<Litentity>>> {
        val d = withContext(dispatcher) {
            try {
                AccessResult.Success(localine.getLiteNotes(pId).map {
                    Litentity(it).apply {
                        millitime = it.millitime
                    }
                })
            } catch (e: Exception) {
                AccessResult.Failure(e)
            }
        }
        return flow {
            emit(d)
        }
    }

    override suspend fun getLiteNotes(): Flow<AccessResult<List<Litentity>>> {
        val d = withContext(dispatcher) {
            try {
                AccessResult.Success(localine.getLiteNotes().map {
                    Litentity(it).apply {
                        millitime = it.millitime
                    }
                })
            } catch (e: Exception) {
                AccessResult.Failure(e)
            }
        }
        return flow {
            emit(d)
        }
    }

    override suspend fun saveCategory(c: Litentity): Flow<AccessResult<Boolean>> {
        val d = withContext(dispatcher) {
            try {
                if (c.isValid) {
                    val category = Category(
                        nId = c.nId,
                        title = c.title,
                        millitime = System.currentTimeMillis()
                    )
                    localine.upsertCategories(category)
                    AccessResult.Success(true)
                } else {
                    AccessResult.Success(false)
                }
            } catch (e: Exception) {
                AccessResult.Failure(e)
            }
        }
        return flow {
            emit(d)
        }
    }

    override suspend fun loadNotentity(id: Long): Flow<AccessResult<Notentity>> {
        val d = withContext(dispatcher) {
            try {
                localine.getOneNotentry(id)?.let {
                    AccessResult.Success(Notentity(it))
                } ?: AccessResult.Warning()
            } catch (e: Exception) {
                AccessResult.Failure(e)
            }
        }
        return flow {
            emit(d)
        }
    }

    override suspend fun saveNotentry(pId: Long?, n: Notentity): Flow<AccessResult<Boolean>> {
        val d = withContext(dispatcher) {
            try {
                if (n.isValid) {
                    val note = Notentry(
                        nId = n.nId,
                        title = n.title,
                        brief = n.brief,
                        ymdate = n.ymdate
                    ).also { n ->
                        n.cateId = pId
                    }
                    localine.upsertNotentries(note)
                    AccessResult.Success(true)
                } else {
                    AccessResult.Success(false)
                }
            } catch (e: Exception) {
                AccessResult.Failure(e)
            }
        }
        return flow {
            emit(d)
        }
    }

    override suspend fun updateCategoryAsUseless(nId: Long): Flow<AccessResult<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateNotentryAsUseless(nId: Long): Flow<AccessResult<Int>> {
        TODO("Not yet implemented")
    }

}
