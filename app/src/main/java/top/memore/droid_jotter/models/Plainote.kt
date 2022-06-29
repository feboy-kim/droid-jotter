package top.memore.droid_jotter.models

import top.memore.droid_jotter.*
import top.memore.droid_jotter.locally.Notentry

data class Plainote(
    val nId: Long,
    val title: String,
    val brief: String?,
    val ymdate: Int? = null
) : Litentity(id = nId) {
    val ymdTriple: Triple<Short, Byte, Byte>?
        get() {
            val ymd = ymdate ?: return null
            val d = ymd and 0xFF
            val ym = ymd shr 8
            val m = ym and 0xFF
            val y = ym shr 8
            return Triple(y.toShort(), m.toByte(), d.toByte())
        }

    fun toNotentry(cId: Long?): Notentry = Notentry(
        nId = this.nId,
        title = this.title,
        brief = this.brief,
        ymdate = this.ymdate
    ).also {
        it.categoId = cId
        it.deleting = this.deleting
        it.millitime = System.currentTimeMillis()
    }

    val isValid: Boolean
        get() {
            if (title.isBlank() || title.length > NAME_MAX_LENGTH) return false
            brief?.let {
                if (it.length > TEXT_MAX_LENGTH) return false
            }
            return true
        }

    companion object {
        fun newInstance(t: String, y: Short, m: Byte, d: Byte, b: String? = null): Plainote? {
            if (t.isBlank() || t.length > NAME_MAX_LENGTH) return null
            b?.let {
                if (it.length > TEXT_MAX_LENGTH) return null
            }
            if (m < MONTH_MIN || m > MONTH_MAX || d < DAY_OF_MONTH_MIN || d > DAY_OF_MONTH_MAX) return null
            return Plainote(
                nId = System.currentTimeMillis(),
                title = t,
                brief = b,
                ymdate = ymd(y, m, d)
            )
        }

        fun newInstance(t: String, b: String? = null): Plainote? {
            if (t.isBlank() || t.length > NAME_MAX_LENGTH) return null
            b?.let {
                if (it.length > TEXT_MAX_LENGTH) return null
            }
            return Plainote(nId = System.currentTimeMillis(), title = t, brief = b)
        }

        fun fromNotentry(n: Notentry): Plainote {
            return Plainote(
                nId = n.nId,
                title = n.title,
                brief = n.brief,
                ymdate = n.ymdate
            ).apply {
                deleting = n.deleting
                millitime = n.millitime
            }
        }
    }
}