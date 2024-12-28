package com.harissabil.damome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.harissabil.damome.core.theme.DaMoMeTheme
import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.model.ChatMessage
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.TransactionType
import com.harissabil.damome.domain.repository.ChatRepository
import com.harissabil.damome.domain.repository.CurrencyRepository
import com.harissabil.damome.domain.repository.TextEmbeddingRepository
import com.harissabil.damome.domain.repository.TransactionRepository
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.Surface
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
@Preview
fun App() {
    KoinContext {
        DaMoMeTheme {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                val textEmbeddingRepository = koinInject<TextEmbeddingRepository>()
                val transactionRepository = koinInject<TransactionRepository>()
                val currencyRepository = koinInject<CurrencyRepository>()
                val chatRepository = koinInject<ChatRepository>()

                LaunchedEffect(key1 = Unit) {
                    currencyRepository.setCurrency(Uuid.random().toString())
                    val embedding =
                        textEmbeddingRepository.getEmbedding("Nintendo Switch for holiday gaming")
//                    when (embedding) {
//                        is Result.Error -> println("Errorrrrrrrrrrrrrrrrrrrrrrrrrrr")
//                        is Result.Success -> {
//                            val result = embedding.data.values
//                            for (i in result.indices) {
//                                println("${i + 1} ${result[i]}")
//                            }
//                            delay(2000)
//                            val toInsert = Transaction(
//                                type = TransactionType.INCOME,
//                                timestamp = Clock.System.now(),
//                                amount = 100000.00,
//                                currency = "IDR",
//                                category = "Salary",
//                                description = "Monthly salary",
//                                textToEmbed = "Salary - Monthly salary",
//                                embedding = result
//                            )
//                            transactionRepository.createTransaction(transaction = toInsert)
//                            println(toInsert)
//                            println("Inserting...")
//                            delay(3000)
//                            val all = transactionRepository.getAllTransactions()
//                            val currency = currencyRepository.getCurrency()
//                            println(all)
//                            println(currency)
//                        }
//                    }
                    when (embedding) {
                        is Result.Error -> {
                            println("Error")
                        }

                        is Result.Success -> {
                            println("Chat Test")
                            delay(1000)
                            println("Saving chat group...")
                            val chatGroupId = chatRepository.saveChatGroup(
                                chatGroup = ChatGroup(
                                    name = "Group 222",
                                    timestamp = Clock.System.now(),
                                )
                            )
                            chatRepository.saveChatMessage(
                                chatGroupId = chatGroupId,
                                messages = ChatMessage(
                                    isUser = true,
                                    message = "Hello",
                                    timestamp = Clock.System.now(),
                                    relatedTransactions = listOf(
                                        Transaction(
                                            type = TransactionType.INCOME,
                                            timestamp = Clock.System.now(),
                                            amount = 100000.00,
                                            currency = "IDR",
                                            category = "Salary",
                                            description = "Monthly salary",
                                            textToEmbed = "playing until late at night",
                                            embedding = embedding.data.values
                                        )
                                    ),
                                    order = 0
                                )
                            )
                            println("Chat group saved")
                            val chatHistory = chatRepository.getChatHistory().first()
                            println(chatHistory)
                            val chatMessage =
                                chatRepository.getChatMessage(chatGroupId)

                            println("Chat messages:")
                            println(chatMessage)

                            delay(1000)
                            println("Vector Search Test")
                            val query = textEmbeddingRepository.getEmbedding("game console")
                            when (query) {
                                is Result.Error -> {
                                    println("Error")
                                }

                                is Result.Success -> {
                                    try {
                                        val resultSearch =
                                            transactionRepository.retrieveSimilarTransactions(
                                                textEmbedding = query.data.values,
                                                neighbors = 5
                                            )
                                        println(resultSearch.map { it.copy(embedding = null) })
                                    } catch (e: Exception) {
                                        println(e)
                                    }
                                }
                            }

//                            println("Deleting chat group...")
//                            chatRepository.deleteChatGroup(chatGroupId)
//                            println("Chat group deleted")
                        }
                    }
                }

                var showContent by remember { mutableStateOf(false) }

                top.yukonga.miuix.kmp.basic.Scaffold(
                    floatingActionButton = {
                        top.yukonga.miuix.kmp.basic.FloatingActionButton(
                            onClick = { showContent = !showContent },
                        ) {
                            top.yukonga.miuix.kmp.basic.Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    },
                    bottomBar = {
                        top.yukonga.miuix.kmp.basic.NavigationBar(
                            items = listOf(
                                NavigationItem(
                                    label = "Home",
                                    icon = Icons.Default.Check,
                                ),
                                NavigationItem(
                                    label = "Profile",
                                    icon = Icons.Default.Check,
                                ),
                                NavigationItem(
                                    label = "Settings",
                                    icon = Icons.Default.Check,
                                ),
                            ),
                            selected = 0,
                            onClick = {}
                        )
                    },
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
                                top.yukonga.miuix.kmp.basic.Text("Compose: $greeting")
                            }
                        }

                        top.yukonga.miuix.kmp.basic.Button(
                            onClick = {},
                            text = "Click me"
                        )

                        top.yukonga.miuix.kmp.basic.Button(
                            onClick = {},
                            text = "Click me"
                        )
                    }
                }
            }
        }
    }
}