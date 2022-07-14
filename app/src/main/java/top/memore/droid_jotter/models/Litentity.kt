package top.memore.droid_jotter.models

import androidx.compose.runtime.Stable
import top.memore.droid_jotter.locally.Litentry

@Stable
data class Litentity(
    val nId: Long,
    val title: String
) {
    var millitime: Long = 0L    // can be changed from negative to positive for comparing last time

    constructor(d: Litentry) : this(nId = d.nId, title = d.title)

    val isUsable: Boolean get() = millitime > 0  // cannot be changed
    var isMarked: Boolean = false

    val isValid: Boolean
        get() {
            return title.isNotBlank() && title.length < TITLE_MAX_LENGTH
        }

    companion object {
        const val TITLE_MAX_LENGTH = 100
    }
}
