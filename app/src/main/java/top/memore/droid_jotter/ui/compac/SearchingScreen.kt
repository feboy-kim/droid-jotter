package top.memore.droid_jotter.ui.compac

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.memore.droid_jotter.ui.models.SearchingVimodel

@Composable
fun SearchingScreen(vm: SearchingVimodel = viewModel(), kw: String) {
    LaunchedEffect(key1 = kw, block = {
        launch {
            vm.findata(kw)
        }
    })
    FoundLiteNotes(keyWord = kw)
}