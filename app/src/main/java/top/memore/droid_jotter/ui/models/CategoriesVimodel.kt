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
class CategoriesVimodel @Inject constructor(
    private val reposit: LocalRepository
) : ViewModel() {
    private lateinit var _categories: List<Litentity>

    private val _usableItems = mutableStateOf<List<Litentity>>(emptyList())
    val usableItems: State<List<Litentity>> = _usableItems

    private val _uselessItems = mutableStateOf<List<Litentity>>(emptyList())
    val uselessItems: State<List<Litentity>> = _uselessItems

    private val _failEvent = MutableStateFlow(AccessEvent(Eventype.OTHERS))
    val failEvent: StateFlow<AccessEvent> = _failEvent

    init {
        viewModelScope.launch {
            try {
                _categories = reposit.loadCategories()
                resetItems()
            } catch (e: Throwable) {
                _failEvent.value = AccessEvent(Eventype.LOADING_FAILED)
            }
        }
    }

    private fun resetItems() {
        _usableItems.value =
            _categories.filter { it.isUsable }.sortedByDescending { it.millitime }
        _uselessItems.value = _categories.filter { !it.isUsable }.sortedBy { it.millitime }
    }

    fun updateOneAsUseless(item: Litentity) {
        viewModelScope.launch {
            try {
                if (reposit.updateCategoryUsability(item.nId, false)) {
                    item.millitime = 0 - item.millitime
                    resetItems()
                }
            } catch (e: Throwable) {
                _failEvent.value = AccessEvent(Eventype.SAVING_FAILED)
            }
        }
    }

    fun updateOneAsUsable(item: Litentity) {
        viewModelScope.launch {
            try {
                if (reposit.updateCategoryUsability(item.nId, true)) {
                    item.millitime = 0 - item.millitime
                    resetItems()
                }
            } catch (e: Throwable) {
                _failEvent.value = AccessEvent(Eventype.SAVING_FAILED)
            }
        }
    }

}