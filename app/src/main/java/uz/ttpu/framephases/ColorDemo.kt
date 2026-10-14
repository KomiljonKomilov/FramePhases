package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Demonstrates Drawing-Phase state read using `Canvas` or `drawBehind`.
 *
 * Reading state directly inside `Canvas { drawRect(selectedColor) }` skips both
 * Composition Phase AND Layout Phase, executing strictly in the Drawing Phase!
 */
@Composable
fun ColorDemo(modifier: Modifier = Modifier) {
    val colors = remember {
        listOf(
            Color(0xFFE91E63), // Pink
            Color(0xFF2196F3), // Blue
            Color(0xFF4CAF50), // Green
            Color(0xFFFF9800), // Orange
            Color(0xFF9C27B0)  // Purple
        )
    }

    var colorIndex by remember { mutableIntStateOf(0) }
    // Color state to be read inside Draw Phase
    var currentColorState by remember { mutableStateOf(colors[0]) }
    var recomposeCount by remember { mutableIntStateOf(0) }

    recomposeCount++

    SideEffect {
        Log.d("ColorDemo", "Composition phase executed! Total Recompositions: $recomposeCount")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Draw-Phase State Read Demo",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Reading state inside Canvas / drawBehind scope skips Composition and Layout phases completely!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recomposition Count: $recomposeCount",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Canvas reading color state directly inside draw scope
        Canvas(
            modifier = Modifier.size(120.dp)
        ) {
            // <-- Draw-phase state read!
            drawRect(color = currentColorState)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                colorIndex = (colorIndex + 1) % colors.size
                currentColorState = colors[colorIndex]
            }) {
                Text("Change Color")
            }
        }
    }
}
