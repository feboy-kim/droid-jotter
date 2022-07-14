package top.memore.droid_jotter.ui.compac

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MasterScreen() {
    val a = "A"
    val b = "B"
    var text by remember {
        mutableStateOf(a)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            text = if (text == a) b else a
        }) {
            Text(text = text)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMasterScreen() {
    MasterScreen()
}
