package top.memore.droid_jotter.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import top.memore.droid_jotter.datany.LocalRepository
import top.memore.droid_jotter.models.AccessEvent
import top.memore.droid_jotter.models.Eventype
import top.memore.droid_jotter.models.Litentity
import javax.inject.Inject

@HiltViewModel
class LiteNoteListVimodel @Inject constructor(
    private val reposit: LocalRepository
) : ViewModel() {
    var notes by mutableStateOf<List<Litentity>>(listOf())
        private set
    private val _event = MutableStateFlow(AccessEvent(Eventype.OTHERS))
    val event: StateFlow<AccessEvent> = _event

    fun loadNotes(id: Long = 0L) {
        viewModelScope.launch {
            try {
                notes = if (id == 0L) reposit.loadLiteNotes() else reposit.loadLiteNotes(id)
            } catch (e: Exception) {
                _event.value = AccessEvent(Eventype.LOADING_FAILED)
            }
        }
    }

//    fun updateOneAsUseless(id: Long) {
//
//    }

}