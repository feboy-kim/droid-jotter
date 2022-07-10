package top.memore.droid_jotter.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import top.memore.droid_jotter.datany.LocalRepository
import top.memore.droid_jotter.models.AccessResult
import top.memore.droid_jotter.models.Litentity
import javax.inject.Inject

@HiltViewModel
class CategoryListVimodel @Inject constructor(
    private val reposit: LocalRepository
) : ViewModel() {
    private val _items: MutableStateFlow<AccessResult<List<Litentity>>> =
        MutableStateFlow(AccessResult.Success(listOf()))
    val items: StateFlow<AccessResult<List<Litentity>>> = _items

    init {
        viewModelScope.launch {
            reposit.getCategories()
                .collect {
                    _items.value = it
                }
        }
    }

    fun updateOneAsUseless(id: Long) {

    }

}