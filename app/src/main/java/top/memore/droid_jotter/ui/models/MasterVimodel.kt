package top.memore.droid_jotter.ui.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
class MasterVimodel @Inject constructor(
    private val reposit: LocalRepository
) : ViewModel() {
    private val _categories = mutableStateOf<List<Litentity>>(emptyList())
    val categories: State<List<Litentity>> = _categories

    private val _selected = mutableStateOf(categoryForOrphanotes)
    val selected: State<Litentity> = _selected

    private val _failEvent = MutableStateFlow(AccessEvent(Eventype.OTHERS))
    val failEvent: StateFlow<AccessEvent> = _failEvent

    init {
        viewModelScope.launch {
            try {
                val items = listOf(categoryForOrphanotes) + reposit.loadCategories().filter { it.isUsable }
                _categories.value = items.sortedBy { it.nId }
            } catch (e: Throwable) {
                _failEvent.value = AccessEvent(Eventype.LOADING_FAILED)
            }
        }
    }

    companion object {
        val categoryForOrphanotes = Litentity(0L, "")
    }

}