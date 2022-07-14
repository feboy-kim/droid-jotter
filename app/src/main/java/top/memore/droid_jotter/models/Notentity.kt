package top.memore.droid_jotter.models

import top.memore.droid_jotter.locally.Notentry
import top.memore.droid_jotter.models.Litentity.Companion.TITLE_MAX_LENGTH

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
        ymdate = d.ymday
    )

    val isValid: Boolean
        get() {
            if (title.isBlank() || title.length > TITLE_MAX_LENGTH) return false
            brief?.let {
                if (it.length > BRIEF_MAX_LENGTH) return false
            }
            return true
        }

    val ymdTriple: Triple<Short, Byte, Byte>? = ymdate?.let {
        val d = it and 0xFF
        val ym = it shr 8
        val m = ym and 0xFF
        val y = ym shr 8
        Triple(y.toShort(), m.toByte(), d.toByte())
    }

    companion object {
        const val BRIEF_MAX_LENGTH = 900
        fun ymd(y: Short, m: Byte, d: Byte) = (y.toInt() shl 16) or (m.toInt() shl 8) or d.toInt()
    }

}