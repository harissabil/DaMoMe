package com.harissabil.damome.ui.screen.ask_ai

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.ui.components.BaseTopAppBar
import com.harissabil.damome.ui.components.CustomSnackbarHost
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.img_damommy
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AskAiScreen(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        topBar = { BaseTopAppBar(title = "Ask AI") },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                containerColor = MiuixTheme.colorScheme.primary,
                shape = FloatingActionButtonDefaults.shape,
                onClick = { /* TODO */ }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "Chat",
                    tint = Color.White
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(vertical = MaterialTheme.spacing.small)
                .padding(horizontal = MaterialTheme.spacing.medium),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(MaterialTheme.spacing.large),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(Res.drawable.img_damommy),
                    contentDescription = "DaMommy AI",
                )
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                Text(
                    text = "DaMommy AI",
                    style = MiuixTheme.textStyles.title3.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = "An AI that can help you manage your finances by analyzing your transaction data and providing insights based on it.",
                    style = MiuixTheme.textStyles.subtitle,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}