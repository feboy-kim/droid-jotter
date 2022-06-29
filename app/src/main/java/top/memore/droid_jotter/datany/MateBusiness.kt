package top.memore.droid_jotter.datany

import top.memore.droid_jotter.models.Litentity

interface MateBusiness<T: Litentity> {
    fun mateWithSource(
        source: List<T>
    ): Triple<List<T>, List<T>, List<T>>
}