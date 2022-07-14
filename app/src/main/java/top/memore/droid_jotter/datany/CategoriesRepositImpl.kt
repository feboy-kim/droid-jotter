package top.memore.droid_jotter.datany

//class CategoriesRepositImpl @Inject constructor(
//    private val localine: LocalAccessible,
//    private val cloudata: CloudAccessible
//) : Repository<Category0> {
//
//    override fun loadCloudEntities(): List<Category0> {
//        return cloudata.getCategories()
//    }
//
//    override fun loadLocalEntities(): List<Category0> {
////        return localine.getUsableCatentries().map {
////            Category.fromCatentry(it)
////        }
//        return listOf()
//    }
//
//    override fun insertLocalEntity(d: Category0): Boolean {
////        return if (d.isValid) {
////            localine.insertCatentry(d.catentry)
////            true
////        } else false
//        return false
//    }
//
//    override fun updateLocalEntity(d: Category0): Boolean {
//        return if (d.isValid) {
//            localine.updateCategories(d.category) > 0
//        } else false
//    }
//
//    override fun deleteLocalEntity(d: Category0): Boolean {
////        return localine.deleteCatentry(d.cId) > 0
//        return false
//    }
//
//    override fun upsertLocalEntities(vararg ds: Category0) {
//        val cs = ds.filter {
//            it.isValid
//        }.map {
//            it.category
//        }.toTypedArray()
//        localine.upsertCategories(*cs)
//    }
//
//    override fun deleteLocalEntities(vararg ds: Category0) {
//        val cs = ds.map {
//            it.category
//        }.toTypedArray()
//        localine.deleteCategories(*cs)
//    }
//
//    override fun upsertCloudEntities(vararg ds: Category0) {
////        ("Not yet implemented")
//    }
//
//    override fun deleteCloudEntities(vararg ds: Category0) {
////        ("Not yet implemented")
//    }
//
//}