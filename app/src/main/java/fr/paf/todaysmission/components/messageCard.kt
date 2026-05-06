package fr.paf.todaysmission.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.paf.todaysmission.models.Messages

@Composable
fun MessageCard(msg: Messages, system: Boolean, isCurrentUser: Boolean = false) {
    if (system) {
        SystemMessageCard(msg)
    } else {
        ChatMessageCard(msg, isCurrentUser)
    }
}

@Composable
private fun SystemMessageCard(msg: Messages) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = msg.msg,
            fontSize = 13.sp,
//            fontWeight = 12.dp,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ChatMessageCard(msg: Messages, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isCurrentUser) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF3498db), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = msg.nom.firstOrNull()?.uppercase().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    start = if (isCurrentUser) 0.dp else 8.dp,
                    end = if (isCurrentUser) 8.dp else 0.dp
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = if (isCurrentUser) 0.dp else 12.dp,
                        bottomStart = if (isCurrentUser) 12.dp else 0.dp
                    )
                )
                .background(if (isCurrentUser) Color(0xFF4F7EFF) else Color.White)
                .padding(top = 8.dp, start = 10.dp, bottom = 8.dp, end = 10.dp)
        ) {
            if (!isCurrentUser) {
                Text(
                    text = msg.nom.capitalize(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F7EFF)
                )
            }
            Text(
                text = msg.msg,
                fontSize = 15.sp,
                color = if (isCurrentUser) Color.White else Color.Black,
                modifier = Modifier.padding(top = if (isCurrentUser) 0.dp else 4.dp)
            )
        }
    }
}
