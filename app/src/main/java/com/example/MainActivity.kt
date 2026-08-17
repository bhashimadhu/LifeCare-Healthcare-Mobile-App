package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.LifeCareRepository
import com.example.ui.MainAppContainer
import com.example.ui.theme.LifeCareBackground
import com.example.ui.theme.LifeCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = remember { LifeCareRepository(context = applicationContext) }
            LifeCareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LifeCareBackground
                ) {
                    MainAppContainer(repository = repository)
                }
            }
        }
    }
}
