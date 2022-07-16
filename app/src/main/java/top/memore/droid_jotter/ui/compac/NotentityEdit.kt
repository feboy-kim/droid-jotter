package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NotentityEdit() {
    Column {
        OutlinedTextField(value = "", onValueChange = {

        }, label = {
            Text(text = "")
        })

    }
}

@Preview
@Composable
fun PreviewNotentityEdit() {

}