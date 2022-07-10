package top.memore.droid_jotter.models

import top.memore.droid_jotter.TITLE_MAX_LENGTH
import top.memore.droid_jotter.locally.Litentry

data class Litentity(
    val nId: Long,
    val title: String,
    var millitime: Long = 0L    // can be changed from negative to positive for comparing last time
) {
    constructor(d: Litentry) : this(nId = d.nId, title = d.title, millitime = d.millitime)

    val isUsable: Boolean = millitime > 0  // cannot be changed
    var isMarked: Boolean = false

    val isValid: Boolean
        get() {
            return title.isNotBlank() && title.length < TITLE_MAX_LENGTH
        }

}
