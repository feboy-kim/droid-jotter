package top.memore.droid_jotter.datany

import top.memore.droid_jotter.models.Litentity

class MateBusinessImpl<T : Litentity>(private val entities: List<T>) : MateBusiness<T> {
    override fun mateWithSource(
        source: List<T>
    ): Triple<List<T>, List<T>, List<T>> {
        val created = mutableListOf<T>()
        val updated = mutableListOf<T>()
        val deleted = mutableListOf<T>()
        val changed = source.filter { s ->
            !entities.any { t ->
                t.id == s.id
                        && t.deleting == s.deleting
                        && !t.deleting
                        && t.millitime == s.millitime
            }
        }
        for (c in changed) {
            val d = entities.find {
                it.id == c.id
            }
            if (d != null) {
                if (d.millitime < c.millitime) {
                    if (c.deleting) deleted.add(c)
                    else updated.add(c)
                }
            } else {
                if (!c.deleting) created.add(c)
            }
        }

        return Triple(created, updated, deleted)
    }

}