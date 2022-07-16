package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.ui.models.MasterVimodel

@Composable
fun MasterScreen(vm: MasterVimodel = viewModel()) {

    val foldState = rememberScaffoldState()
    val asynScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = foldState,
        topBar = {
            TopAppBar {

            }
        },
        bottomBar = {
            BottomAppBar {

            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                asynScope.launch {
                    foldState.snackbarHostState.showSnackbar("Snack info")
                    foldState.drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {

            }
        },
        isFloatingActionButtonDocked = true,
        drawerContent = {
            Text(text = "Drawer title", modifier = Modifier.padding(16.dp))
        }
    ) {
//            LiteNoteList(parent = vm.selected)
    }

}

@Composable
fun MasterDetail() {
    val (searchWord, setSearchWord) = remember {
        mutableStateOf("")
    }

}
