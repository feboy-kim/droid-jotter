package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import top.memore.droid_jotter.R
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.ui.models.LiteNoteListVimodel

@Composable
fun LiteNoteList(
    vm: LiteNoteListVimodel = viewModel(),
    pId: Long = 0L
) {
    val marksShown = rememberSaveable {
        mutableStateOf(false)
    }
    val usableNotes = remember(vm.notes) {
        vm.notes.filter {
            it.millitime > 0
        }
    }
    val uselessNotes = remember(vm.notes) {
        vm.notes.filter {
            it.millitime < 0
        }
    }
    LaunchedEffect(key1 = pId) {
        vm.loadNotes(pId)
        vm.event.collect {

        }
    }

    Column {
        Box(modifier = Modifier.padding(4.dp)) {
            val listState = rememberLazyListState()
            LazyColumn(state = listState) {
                items(usableNotes, key = {
                    it.nId
                }) { item ->
                    LiteNoteItem(
                        liteNote = item,
                        CheckboxShown = marksShown,
                        onClicked = {
                        },
                        onMarkShownChanged = {
                            marksShown.value = it
                        },
                        onMarkChanged = {
                            item.isMarked = it
                        })
                }
            }
        }
        Box(modifier = Modifier.padding(4.dp)) {
            val listState = rememberLazyListState()
            LazyColumn(state = listState) {
                items(uselessNotes, key = {
                    it.nId
                }) { item ->
                    LiteNoteItem(
                        liteNote = item,
                        CheckboxShown = marksShown,
                        onClicked = {
                        },
                        onMarkShownChanged = {
                            marksShown.value = it
                        },
                        onMarkChanged = {
                            item.isMarked = it
                        })
                }
            }
        }
    }

}

@Composable
private fun LiteNoteItem(
    liteNote: Litentity,
    CheckboxShown: State<Boolean>,
    onClicked: () -> Unit,
    onMarkShownChanged: (Boolean) -> Unit,
    onMarkChanged: (Boolean) -> Unit
) {
    val isMarked = remember {
        mutableStateOf(liteNote.isMarked)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = liteNote.title, modifier = Modifier
            .padding(12.dp)
            .weight(1.0f)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { onClicked() },
                    onDoubleTap = {},
                    onLongPress = {
                        onMarkShownChanged(!CheckboxShown.value)
                    },
                    onTap = {}
                )
            }
        )
        Spacer(modifier = Modifier.width(2.dp))
        if (CheckboxShown.value) {
            Checkbox(checked = isMarked.value, onCheckedChange = {
                isMarked.value = it
                onMarkChanged(it)
            })
        } else {
            if (isMarked.value) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_check_24),
                    contentDescription = "",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLiteNoteItem() {
    val n = Litentity(nId = 100L, title = "My LiteNote")
    val t = remember {
        mutableStateOf(true)
    }
    LiteNoteItem(
        liteNote = n,
        CheckboxShown = t,
        onClicked = {},
        onMarkShownChanged = {},
        onMarkChanged = {
            n.isMarked = it
        })
}