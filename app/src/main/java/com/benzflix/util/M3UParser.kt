package com.benzflix.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

object M3UParser {
    data class Channel(
        val name: String,
        val url: String,
        val group: String = "All"
    )

    suspend fun parse(url: String): List<Channel> = withContext(Dispatchers.IO) {
        val lines = URL(url).readText().lines()
        val channels = mutableListOf<Channel>()
        var name = ""
        var group = "All"
        for (line in lines) {
            when {
                line.startsWith("#EXTINF:") -> {
                    name = line.substringAfter(",").trim()
                    // Extract group-title="XYZ"
                    val groupMatch = Regex("group-title=\"([^"]+)\"").find(line)
                    group = groupMatch?.groupValues?.get(1)?.trim() ?: "All"
                }
                line.startsWith("http") -> {
                    channels.add(Channel(name, line, group))
                }
            }
        }
        channels
    }

    fun categories(channels: List<Channel>): List<String> {
        val cats = channels.map { it.group }
            .toMutableSet()
        cats.add("All")
        return cats.sorted()
    }
}