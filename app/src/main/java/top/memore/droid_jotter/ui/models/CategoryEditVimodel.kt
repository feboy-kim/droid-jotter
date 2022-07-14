package top.memore.droid_jotter.ui.models

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class CategoryEditVimodel @Inject constructor() : ViewModel() {
    private val _eventChannel = Channel<Int>(Channel.BUFFERED)
    val eventChannel = _eventChannel.receiveAsFlow()

}