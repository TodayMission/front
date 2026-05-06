package fr.paf.todaysmission.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.paf.todaysmission.viewmodels.LoginViewModel
import fr.paf.todaysmission.utils.State

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onLogin: ((String, String, (String) -> Unit) -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
) {
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var uiError by remember { mutableStateOf<String?>(null) }
    val state by loginViewModel.state.collectAsState()
    val error by loginViewModel.error.collectAsState()
    val session by loginViewModel.session.collectAsState()

    LaunchedEffect(session) {
        if (session != null) {
            onSuccess?.invoke()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = mail,
            onValueChange = { mail = it },
            label = { Text("E-mail") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF3498db),
                focusedLabelColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF3498db),
                focusedLabelColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                loginViewModel.login(mail, password)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498db)),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Sign In")
        }

        val displayedError = uiError ?: error
        if (!displayedError.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = displayedError,
                color = Color(0xFFE53935),
            )
        }
    }
}
