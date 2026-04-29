package fr.paf.todaysmission

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import fr.paf.todaysmission.views.ListGroupScreen
import org.junit.Rule
import org.junit.Test

class CreateGroupUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun create_group() {
        val groupName = "test"

        composeRule.setContent {
            val navController = rememberNavController()
            ListGroupScreen(navController)
        }

        composeRule.onNodeWithTag("openCreateGroup").performClick()

        composeRule.onNodeWithTag("groupNameField").performTextInput(groupName)
        composeRule.onNodeWithTag("createGroupButton").performClick()
        composeRule.onNodeWithText(groupName).assertIsDisplayed()
    }
}

