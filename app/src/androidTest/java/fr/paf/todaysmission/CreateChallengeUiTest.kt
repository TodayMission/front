package fr.paf.todaysmission

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import fr.paf.todaysmission.components.BottomModalSheet
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CreateChallengeUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun create_challenge() {
        var sentName: String? = null

        composeRule.setContent {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            BottomModalSheet(
                showBottomSheet = true,
                onDismiss = {},
                sheetState = sheetState,
                isDefi = true,
                onSend = { sentName = it }
            )
        }

        val challengeName = "test"
        composeRule.onNodeWithTag("challengeNameField").performTextInput(challengeName)
        composeRule.onNodeWithTag("createChallengeButton").assertIsNotEnabled()
        composeRule.onNodeWithTag("participantDropdownField").performClick()
        composeRule.onNodeWithTag("participantItem_0").performClick()
        composeRule.onNodeWithTag("createChallengeButton").assertIsEnabled()
        composeRule.onNodeWithTag("createChallengeButton").performClick()
        composeRule.runOnIdle {
            assertEquals(challengeName, sentName)
        }
    }
}

