package top.memore.droid_jotter.ui.models

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import top.memore.droid_jotter.datany.LocalRepository
import javax.inject.Inject

@HiltViewModel
class NoteEditorVimodel @Inject constructor(
    private val reposit: LocalRepository
): ViewModel() {

}