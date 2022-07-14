package top.memore.droid_jotter.ui.compac

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryListTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setup() {
        composeRule.setContent {
//            CategoryList()
            MasterScreen()
        }
    }

    @Test
    fun loadataInitially_categoriesLoaded() {
        composeRule.onNodeWithText("A").assertExists()
    }

}