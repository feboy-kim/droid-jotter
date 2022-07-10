package top.memore.droid_jotter.cloudy

class Cloudeal {
    var accessToken: String = ""
        private set
    var categoryUri: String = ""
        private set
    var plainoteUri: String = ""
        private set

    fun setupAccessor(token: String) {
        accessToken = token
    }

    fun setupCategory(uri: String) {
        categoryUri = uri
    }

    fun setupPlainote(uri: String) {
        plainoteUri = uri
    }
}