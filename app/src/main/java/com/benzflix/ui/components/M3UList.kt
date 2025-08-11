package com.benzflix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.benzflix.util.M3UParser
import com.benzflix.player.ExoPlayerScreen
import com.benzflix.player.VlcPlayerScreen

enum class PlayerType { EXOPLAYER, VLC }

@Composable
fun M3UList() {
    var m3uUrl by remember { mutableStateOf("https://iptv-org.github.io/iptv/index.m3u") }
    var channels by remember { mutableStateOf(listOf<M3UParser.Channel>()) }
    var selectedChannel by remember { mutableStateOf<M3UParser.Channel?>(null) }
    var selectedPlayer by remember { mutableStateOf(PlayerType.EXOPLAYER) }
    var playerDialogOpen by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var categories by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(m3uUrl) {
        channels = M3UParser.parse(m3uUrl)
        categories = M3UParser.categories(channels)
    }

    val visibleChannels = if (selectedCategory == "All") channels else channels.filter { it.group == selectedCategory }

    if (selectedChannel == null) {
        Column(Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {
            // Categories row
            Row(
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                categories.forEach { cat ->
                    Box(
                        Modifier
                            .padding(end = 8.dp)
                            .background(
                                if (cat == selectedCategory) Color(0xFFFFD700).copy(alpha = 0.3f)
                                else Color.Transparent
                            )
                            .clickable { selectedCategory = cat }
                            .focusable()
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFFFD700),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            // Channel list
            Box(Modifier.weight(1f)) {
                LazyColumn {
                    items(visibleChannels.size) { idx ->
                        val ch = visibleChannels[idx]
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.Transparent)
                                .clickable {
                                    selectedChannel = ch
                                    playerDialogOpen = true
                                }
                                .focusable()
                        ) {
                            Text(text = ch.name, color = Color(0xFFFFD700))
                        }
                    }
                }
            }

            if (playerDialogOpen && selectedChannel != null) {
                AlertDialog(
                    onDismissRequest = { playerDialogOpen = false; selectedChannel = null },
                    title = { Text("Choose Player", color = Color(0xFFFFD700)) },
                    text = {
                        Column {
                            Button(
                                onClick = { selectedPlayer = PlayerType.EXOPLAYER; playerDialogOpen = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282828))
                            ) { Text("ExoPlayer", color = Color(0xFFFFD700)) }
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { selectedPlayer = PlayerType.VLC; playerDialogOpen = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282828))
                            ) { Text("VLC Player", color = Color(0xFFFFD700)) }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {}
                )
            }
        }
    } else {
        if (selectedPlayer == PlayerType.EXOPLAYER) {
            ExoPlayerScreen(
                url = selectedChannel!!.url,
                onExit = { selectedChannel = null }
            )
        } else {
            VlcPlayerScreen(
                url = selectedChannel!!.url,
                onExit = { selectedChannel = null }
            )
        }
    }
}