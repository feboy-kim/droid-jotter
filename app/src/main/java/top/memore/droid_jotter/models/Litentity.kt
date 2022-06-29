package top.memore.droid_jotter.models

abstract class Litentity(
    var id: Long,
    var deleting: Boolean = false,
    var millitime: Long = System.currentTimeMillis()
)
