package fr.paf.todaysmission.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState

        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(12.dp)) {
                Text(title)
                OutlinedTextField(
                    value = nameValue,
                    singleLine = true,
                    onValueChange = { nameValue = it},
                    placeholder = { Text(name, color = Color.Gray) },
                    modifier = Modifier.height(75.dp).padding(0.dp, 10.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray
                    )
                );
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Description", color = Color.Gray) },
                    modifier = Modifier.height(150.dp).padding(0.dp, 10.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray
                    )
                );

                if (isDefi){
                    var expanded by remember { mutableStateOf(false) }
                    var selectedIndex by remember { mutableStateOf(0) }

                    Box(){
                         OutlinedTextField(
                            value = if (selectedIndex >= 0) participants_test[selectedIndex] else "",
                            onValueChange = { },
                            readOnly = true,
                            placeholder = { Text("Sélectionner un participant", color = Color.Gray) },
                            modifier = Modifier
                                .height(75.dp)
                                .padding(0.dp, 10.dp)
                                .fillMaxWidth()
                                .clickable { expanded = true },  // ← Déclenche l'ouverture
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}, modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color.Gray,
                                shape = RoundedCornerShape(16.dp)
                            )) {
                            participants_test.forEachIndexed { index, participant ->
                                if (selectedIndex == index){
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {  },
                                        text = {
                                            Text(participant,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        },
                                    )
                                }
                            }
                        }
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
                TextButton(onClick = { onSend(nameValue) }, shape = RoundedCornerShape(24.dp),
                    colors =
                        ButtonColors(
                            containerColor = Color(0xFF4F46E5),
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color.Gray
                        ),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Créer", fontWeight = FontWeight.Bold, fontSize = 20.sp) };
            }
        }

}