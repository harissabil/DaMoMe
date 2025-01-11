package com.harissabil.damome.ui.screen.damommy_chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.harissabil.damome.domain.model.ChatMessage
import com.harissabil.damome.domain.model.Transaction
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.img_damommy_avatar
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = if (chatMessage.isUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        },
    ) {
        if (chatMessage.isUser) {
            Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.25f))
        }
        if (!chatMessage.isUser) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                painter = painterResource(Res.drawable.img_damommy_avatar),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Column(
            modifier = Modifier
                .background(
                    color = if (chatMessage.isUser) {
                        MiuixTheme.colorScheme.primaryContainer
                    } else {
                        MiuixTheme.colorScheme.surface
                    },
                    shape = RoundedCornerShape(
                        topStart = if (chatMessage.isUser) 20.dp else 0.dp,
                        topEnd = if (chatMessage.isUser) 0.dp else 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .padding(12.dp),
        ) {
            Text(
                text = chatMessage.message,
                color = if (chatMessage.isUser) {
                    MiuixTheme.colorScheme.onPrimaryContainer
                } else {
                    MiuixTheme.colorScheme.onSurface
                },
            )
            if (!chatMessage.isUser) {
                if (!chatMessage.relatedTransactions.isNullOrEmpty()) {
                    chatMessage.relatedTransactions.forEach { transaction: Transaction ->
                        Spacer(modifier = Modifier.height(8.dp))
                        RelatedTransactionItem(
                            transaction = transaction
                        )
                    }
                }
            }
        }
    }
}