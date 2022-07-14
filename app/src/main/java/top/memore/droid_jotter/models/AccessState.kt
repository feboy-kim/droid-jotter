package top.memore.droid_jotter.models

sealed class AccessState<out R> {
    data class Success<out T>(val d: T): AccessState<T>()
    data class Warning(val w: String = ""): AccessState<Nothing>()
    data class Failure(val e: Throwable): AccessState<Nothing>()
}
