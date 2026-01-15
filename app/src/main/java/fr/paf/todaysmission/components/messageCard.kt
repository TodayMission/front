package fr.paf.todaysmission.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.paf.todaysmission.models.Messages

@Composable
fun MessageCard(msg: Messages) {
    Column {
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(5.dp, 10.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFB0B0B0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = msg.id,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = msg.nom,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F7EFF)
                )
                Text(
                    text = msg.msg,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}