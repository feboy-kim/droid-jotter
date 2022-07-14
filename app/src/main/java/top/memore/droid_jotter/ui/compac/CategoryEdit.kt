package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.memore.droid_jotter.ui.models.CategoryEditVimodel

@Composable
fun CategoryEdit(vm: CategoryEditVimodel = viewModel()) {
    Column(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(value = "", onValueChange = {}, label = {
            Text(text = "")
        })
    }
}

@Preview
@Composable
fun PreviewCategoryEdit() {

}