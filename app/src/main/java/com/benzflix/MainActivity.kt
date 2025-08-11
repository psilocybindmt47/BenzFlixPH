package com.benzflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.benzflix.ui.BenzFlixTheme
import com.benzflix.ui.components.M3UList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BenzFlixTheme {
                M3UList()
            }
        }
    }
}