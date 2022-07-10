package top.memore.droid_jotter.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import top.memore.droid_jotter.datany.LocalRepository
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity
import javax.inject.Inject

@HiltViewModel
class LiteNoteListVimodel @Inject constructor(
    private val reposit: LocalRepository
) : ViewModel() {
    private val _notes =
        MutableStateFlow<AccessResult<List<Litentity>>>(AccessResult.Success(listOf()))
    val notes: StateFlow<AccessResult<List<Litentity>>> = _notes

    fun loadNotes(id: Long = 0L) {
        viewModelScope.launch {
            val f = if (id == 0L) reposit.getLiteNotes() else reposit.getLiteNotes(id)
            f.collect {
                _notes.value = it
            }
        }
    }

    fun updateOneAsUseless(id: Long) {

    }

}