package top.memore.droid_jotter.ui

sealed class EventResult<out R> {
    data class Warning<out T>(val d: T): EventResult<T>()
    data class Failure(val e: Throwable): EventResult<Nothing>()
}
