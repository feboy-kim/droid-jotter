package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import top.memore.droid_jotter.models.Litentity
import top.memore.droid_jotter.ui.models.CategoryListVimodel

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    vm: CategoryListVimodel = viewModel()
) {
    LaunchedEffect(key1 = true) {
        vm.event.collect {
            it.notHandledContent?.let {

            }
        }
    }
    val usableCategories = remember(vm.items.value) {
        vm.items.value.filter {
            it.millitime > 0
        }
    }
    val uselessCategories = remember(vm.items.value) {
        vm.items.value.filter {
            it.millitime < 0
        }
    }
    Column {
        LazyColumn(state = rememberLazyListState()) {
            items(usableCategories, key = {
                it.nId
            }) { item ->
                CategoryItem(
                    category = item,
                    onLongPressed = {
                        vm.updateOneAsUseless(it.nId)
                    }
                )
            }
        }
        LazyColumn(state = rememberLazyListState()) {
            items(uselessCategories, key = {
                it.nId
            }) { item ->
                CategoryItem(
                    category = item,
                    onLongPressed = {
                        vm.updateOneAsUseless(it.nId)
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Litentity,
    onLongPressed: (Litentity) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = category.title, modifier = modifier
            .padding(12.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPressed(category) }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val c = Litentity(nId = 100L, title = "My Category")
    CategoryItem(
        category = c,
        onLongPressed = {
        }
    )
}

@Preview
@Composable
fun PreviewCategoryList() {
    CategoryList()
}