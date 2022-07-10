package top.memore.droid_jotter.models

sealed class AccessResult<out R> {
    data class Success<out T>(val d: T): AccessResult<T>()
    data class Warning(val w: String = ""): AccessResult<Nothing>()
    data class Failure(val e: Throwable): AccessResult<Nothing>()
}
