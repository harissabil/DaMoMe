package com.harissabil.damome.ui.screen.records

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.ui.components.BaseTopAppBar
import com.harissabil.damome.ui.components.CustomSnackbarHost
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.TextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        topBar = { BaseTopAppBar(title = "Records") }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = MaterialTheme.spacing.small)
        ) {
            var value by remember { mutableStateOf("") }

            SearchBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                inputField = {
                    TextField(
                        leadingIcon = {
                            Row {
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                )
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            }
                        },
                        label = if (value.isBlank()) "Search transactions" else "",
                        value = value,
                        onValueChange = { value = it },
                    )
                },
                expanded = false,
                onExpandedChange = {}
            ) {}
        }
    }
}