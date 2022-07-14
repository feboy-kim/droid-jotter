package top.memore.droid_jotter.models

class AccessEvent(private val content: Eventype) {
    private var beenHandled = false

    val notHandledContent: Eventype?
        get() = if (beenHandled) {
            null
        } else {
            beenHandled = true
            content
        }
}
