package top.memore.droid_jotter.datany

import top.memore.droid_jotter.models.Litentity

interface Repository<T: Litentity> {
    fun loadLocalEntities(): List<T>
    fun loadCloudEntities(): List<T>
    fun insertLocalEntity(d: T): Boolean
    fun updateLocalEntity(d: T): Boolean
    fun deleteLocalEntity(d: T): Boolean
    fun upsertLocalEntities(vararg ds: T)
    fun deleteLocalEntities(vararg ds: T)
    fun upsertCloudEntities(vararg ds: T)
    fun deleteCloudEntities(vararg ds: T)
}