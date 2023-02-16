package com.example.greedygameassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.greedygameassignment.navigation.Navigation
import com.example.greedygameassignment.ui.theme.GreedyGameAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreedyGameAssignmentTheme {
                Navigation()
            }
        }
    }
}



