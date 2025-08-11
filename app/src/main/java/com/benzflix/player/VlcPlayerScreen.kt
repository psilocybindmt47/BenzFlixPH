package com.benzflix.player

import android.view.SurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

@Composable
fun VlcPlayerScreen(url: String, onExit: () -> Unit) {
    val context = LocalContext.current
    val libVLC = remember { LibVLC(context, arrayListOf("--no-drop-late-frames", "--no-skip-frames")) }
    val mediaPlayer = remember { MediaPlayer(libVLC) }
    var surfaceView: SurfaceView? = remember { null }

    DisposableEffect(Unit) {
        val media = Media(libVLC, url)
        mediaPlayer.media = media
        mediaPlayer.play()
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
            libVLC.release()
        }
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = {
            SurfaceView(it).also { sv ->
                surfaceView = sv
                mediaPlayer.attachViews(sv, null, false, false)
            }
        })
    }
}