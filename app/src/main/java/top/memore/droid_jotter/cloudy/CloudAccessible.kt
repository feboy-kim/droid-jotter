package top.memore.droid_jotter.cloudy

import top.memore.droid_jotter.models.Category
import top.memore.droid_jotter.models.Plainote

interface CloudAccessible {
    fun getCategories(uri: String, token: String): List<Category>
    fun getPlainotes(uri: String, token: String): List<Plainote>
    fun getPlainotes(uri: String, token: String, pId: Long): List<Plainote>
    fun upsertCategories(vararg cgs: Category)
    fun deleteCategories(vararg cgs: Category)
    fun getOrphanPlainotes(uri: String, token: String): List<Plainote>
    fun getPlainotesByCatego(uri: String, token: String, cId: Long): List<Plainote>
}