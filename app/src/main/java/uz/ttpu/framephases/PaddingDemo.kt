package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Demonstrates a Composition-Phase state read using `Modifier.padding(padding)`.
 *
 * Because `padding(padding)` takes a direct `Dp` value (evaluated during composition),
 * reading the state variable here causes the composable body to recompose on EVERY state change.
 */
import androidx.compose.runtime.mutableIntStateOf

@Composable
fun PaddingDemo(modifier: Modifier = Modifier) {
    var paddingValue by remember { mutableFloatStateOf(16f) }
    var recomposeCount by remember { mutableIntStateOf(0) }

    // Increment recomposition counter on each recomposition
    recomposeCount++

    SideEffect {
        Log.d("PaddingDemo", "Composition phase executed! Total Recompositions: $recomposeCount")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Composition-Phase State Read Demo",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Modifier.padding(padding) takes a direct Dp value, forcing RECOMPOSITION on every value change.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recomposition Count: $recomposeCount",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Box whose padding changes dynamically
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(paddingValue.dp) // <-- Composition-phase state read!
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(
                text = "Padding: ${paddingValue.toInt()} dp",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = paddingValue,
            onValueChange = { paddingValue = it },
            valueRange = 4f..64f,
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { paddingValue = if (paddingValue >= 60f) 16f else paddingValue + 10f }) {
            Text("Step Padding (+10dp)")
        }
    }
}
