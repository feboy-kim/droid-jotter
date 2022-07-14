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
class CategoryListVimodel @Inject constructor(
    private val reposit: LocalRepository
) : ViewModel() {
    private val _items = mutableStateOf<List<Litentity>>(emptyList())
    val items: State<List<Litentity>> = _items
    private val _event = MutableStateFlow(AccessEvent(Eventype.OTHERS))
    val event: StateFlow<AccessEvent> = _event

    init {
        viewModelScope.launch {
            try {
                _items.value = reposit.loadCategories()
            } catch (e: Throwable) {
                _event.value = AccessEvent(Eventype.LOADING_FAILED)
            }
        }
    }

    fun updateOneAsUseless(id: Long) {
        val newList = _items.value.filter {
            it.nId != id
        }
        _items.value = newList
    }

}