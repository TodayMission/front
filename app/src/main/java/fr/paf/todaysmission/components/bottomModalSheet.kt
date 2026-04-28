package fr.paf.todaysmission.components

import android.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.paf.todaysmission.models.Group
import fr.paf.todaysmission.models.superGroups
import fr.paf.todaysmission.views.clickHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModalSheet(showBottomSheet: Boolean, onDismiss: () -> Unit, sheetState: SheetState, isDefi: Boolean, onSend: (text: String) -> Unit){
        val participants_test = listOf(
            "Theo",
            "To",
            "Téo",
            "Tèo",
            "T"
        );

        val title = if (isDefi) "Création d'un défi" else  "Création d'un groupe";
        val name = if (isDefi) "Nom du défi" else  "Nom du groupe";
        var nameValue by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableIntStateOf(-1) }

        val canSubmit = if (isDefi) {
            nameValue.isNotBlank() && selectedIndex >= 0
        } else {
            nameValue.isNotBlank()
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.95f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                Text(title)
                OutlinedTextField(
                    value = nameValue,
                    singleLine = true,
                    onValueChange = { nameValue = it},
                    placeholder = { Text(name, color = Color.Gray) },
                    modifier = Modifier
                        .height(75.dp)
                        .padding(0.dp, 10.dp)
                        .fillMaxWidth()
                        .testTag(if (isDefi) "challengeNameField" else "groupNameField"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray
                    )
                );
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Description", color = Color.Gray) },
                    modifier = Modifier.height(150.dp).padding(0.dp, 10.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray
                    )
                );

                if (isDefi){
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = if (selectedIndex >= 0) participants_test[selectedIndex] else "",
                            onValueChange = { },
                            readOnly = true,
                            placeholder = { Text("Sélectionner un participant", color = Color.Gray) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .testTag("participantDropdownField")
                                .menuAnchor(),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .height(300.dp)
                                .background(
                                    color = Color.White,
                                )
                        ) {
                            participants_test.forEachIndexed { index, participant ->
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .testTag("participantItem_$index"),
                                    onClick = {
                                        selectedIndex = index
                                        expanded = false
                                    },
                                    text = {
                                        Text(
                                            participant,
                                            color = Color.Black,
                                            textAlign = TextAlign.Left,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                )
                            }
                        }
                    }
                    if (selectedIndex < 0) {
                        Text(
                            "Veuillez sélectionner au moins un participant.",
                            color = Color(0xFFB00020),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                        )
                    }
                }else {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { Text("Avatar du groupe", color = Color.Gray) },
                        modifier = Modifier.height(75.dp).padding(0.dp, 10.dp).fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray
                        )
                    );
                }
                TextButton(
                    onClick = { onSend(nameValue) },
                    enabled = canSubmit,
                    shape = RoundedCornerShape(24.dp),
                    colors =
                        ButtonColors(
                            containerColor = Color(0xFF4F46E5),
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color.Gray
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag(if (isDefi) "createChallengeButton" else "createGroupButton")
                ) { Text("Créer", fontWeight = FontWeight.Bold, fontSize = 20.sp) };
            }
        }

}