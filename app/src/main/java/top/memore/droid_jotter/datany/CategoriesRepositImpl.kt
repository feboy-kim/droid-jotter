package top.memore.droid_jotter.datany

import top.memore.droid_jotter.cloudy.CloudAccessible
import top.memore.droid_jotter.cloudy.CloudSharable
import top.memore.droid_jotter.locally.LocalAccessible
import top.memore.droid_jotter.models.Category
import javax.inject.Inject

class CategoriesRepositImpl @Inject constructor(
    private val localine: LocalAccessible,
    private val cloudata: CloudAccessible,
    private val cloudeal: CloudSharable
) : Repository<Category> {

    override fun loadCloudEntities(): List<Category> {
        return cloudata.getCategories(cloudeal.categoryUri, cloudeal.accessToken)
    }

    override fun loadLocalEntities(): List<Category> {
        return localine.getCatentries().map {
            Category.fromCatentry(it)
        }
    }

    override fun insertLocalEntity(d: Category): Boolean {
        return if (d.isValid) {
            localine.insertCatentry(d.catentry)
            true
        } else false
    }

    override fun updateLocalEntity(d: Category): Boolean {
        return if (d.isValid) {
            localine.updateCatentry(d.catentry) > 0
        } else false
    }

    override fun deleteLocalEntity(d: Category): Boolean {
        return localine.deleteCatentry(d.cId) > 0
    }

    override fun upsertLocalEntities(vararg ds: Category) {
        val cs = ds.filter {
            it.isValid
        }.map {
            it.catentry
        }.toTypedArray()
        localine.upsertCatentries(*cs)
    }

    override fun deleteLocalEntities(vararg ds: Category) {
        val cs = ds.map {
            it.catentry
        }.toTypedArray()
        localine.deleteCatentries(*cs)
    }

    override fun upsertCloudEntities(vararg ds: Category) {
//        ("Not yet implemented")
    }

    override fun deleteCloudEntities(vararg ds: Category) {
//        ("Not yet implemented")
    }

}