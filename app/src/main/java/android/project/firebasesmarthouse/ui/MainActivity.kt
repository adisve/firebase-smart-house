package android.project.firebasesmarthouse.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.project.firebasesmarthouse.ui.composables.App
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import android.project.firebasesmarthouse.ui.theme.FirebaseSmartHouseTheme
import androidx.compose.material3.Scaffold

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseSmartHouseTheme {
                Scaffold {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        App()
                    }
                }
            }
        }
    }
}
