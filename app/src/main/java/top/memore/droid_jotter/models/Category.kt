package top.memore.droid_jotter.models

import top.memore.droid_jotter.NAME_MAX_LENGTH
import top.memore.droid_jotter.locally.Catentry

data class Category(
    val cId: Long,
    val ready: Boolean = true,
    val name: String
) : Litentity(id = cId) {
    val catentry: Catentry
        get() = Catentry(cId = this.cId, name = this.name).also {
            it.deleting = this.deleting
            it.millitime = this.millitime
        }

    val isValid: Boolean
        get() = name.isNotBlank() && name.length <= NAME_MAX_LENGTH

    companion object {
        fun newInstance(n: String): Category? {
            if (n.isBlank() || n.length > NAME_MAX_LENGTH) return null
            return Category(cId = System.currentTimeMillis(), name = n)
        }

        fun fromCatentry(c: Catentry) = Category(cId = c.cId, name = c.name).apply {
            deleting = c.deleting
            millitime = c.millitime
        }
    }
}