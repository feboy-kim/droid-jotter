package top.memore.droid_jotter.datany

class Parentaline {
    var parentId: Long? = null
        private set

    fun setParent(id: Long?) {
        parentId = id
    }
}