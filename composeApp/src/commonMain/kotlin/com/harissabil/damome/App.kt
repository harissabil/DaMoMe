package com.harissabil.damome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.harissabil.damome.di.appModule
import com.harissabil.damome.core.theme.DaMoMeTheme
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        DaMoMeTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                var showContent by remember { mutableStateOf(false) }

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showContent = !showContent },
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { showContent = !showContent }) {
                            Text(text = "text")
                        }
                        AnimatedVisibility(showContent) {
                            val greeting = remember { Greeting().greet() }
                            Column(
                                Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(painterResource(Res.drawable.compose_multiplatform), null)
                                Text("Compose: $greeting")
                            }
                        }

                        Button(
                            onClick = {},
                        ) {
                            Text(text = "Click me!")
                        }

                        Button(
                            onClick = {},
                            content = { Text("Click me!") }
                        )
                    }
                }
            }
        }
    }
}