package top.memore.droid_jotter.models

import top.memore.droid_jotter.TITLE_MAX_LENGTH
import top.memore.droid_jotter.BRIEF_MAX_LENGTH
import top.memore.droid_jotter.locally.Notentry

data class Notentity(
    val nId: Long,
    val title: String,
    val brief: String?,
    val ymdate: Int?
) {
    constructor(d: Notentry) : this(
        nId = d.nId,
        title = d.title,
        brief = d.brief,
        ymdate = d.ymdate
    )

    val isValid: Boolean
        get() {
            if (title.isBlank() || title.length > TITLE_MAX_LENGTH) return false
            brief?.let {
                if (it.length > BRIEF_MAX_LENGTH) return false
            }
            return true
        }

}