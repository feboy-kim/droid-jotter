package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.memore.droid_jotter.models.Litentity

@Composable
fun LiteNoteList(
    notes: State<List<Litentity>>,
    parent: State<Litentity>
) {
    val marksShown = rememberSaveable {
        mutableStateOf(false)
    }
    val usableNotes = remember(notes) {
        notes.value.filter {
            it.millitime > 0
        }
    }
    val uselessNotes = remember(notes) {
        notes.value.filter {
            it.millitime < 0
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
        modifier = Modifier.fillMaxWidth().pointerInput(Unit){
            detectTapGestures(
                onLongPress = {
                    onMarkShownChanged(!CheckboxShown.value)
                },
            )
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = liteNote.title, modifier = Modifier
            .padding(12.dp)
            .weight(1.0f)
            .clickable { onClicked() }
        )
        Spacer(modifier = Modifier.width(2.dp))
        if (CheckboxShown.value) {
            Checkbox(checked = isMarked.value, onCheckedChange = {
                isMarked.value = it
                onMarkChanged(it)
            })
        } else {
            if (isMarked.value) {
                Icon(Icons.Filled.Check, contentDescription = "marked")
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