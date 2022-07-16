package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.memore.droid_jotter.models.Litentity

@Composable
fun CategoriesCombox(
    items: State<List<Litentity>>,
    selectedItem: Litentity,
    onSelected: (Litentity) -> Unit
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = selectedItem.title.ifBlank { "未分类" }, onValueChange = {

        }, trailingIcon = {
            Icon(imageVector = icon, contentDescription = "item", Modifier.clickable {
                setExpanded(!expanded)
            })
        })
        DropdownMenu(expanded = expanded, onDismissRequest = { setExpanded(false) }) {
            items.value.forEach {
                DropdownMenuItem(onClick = {
                    setExpanded(false)
                    onSelected(it)
                }) {
                    Text(text = it.title.ifBlank { "未分类" })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesComboxPreview(
) {
    val items = remember {
        mutableStateOf(
            listOf(
                Litentity(0L, ""),
                Litentity(10L, "One"),
                Litentity(20L, "Two")
            )
        )
    }
    val (selected, setSelected) = remember {
        mutableStateOf(Litentity(0L, ""))
    }
    CategoriesCombox(items = items, selectedItem = selected, onSelected = {
        setSelected(it)
    })
}
