package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Demonstrates Layout-Phase state read using `Modifier.offset { IntOffset(...) }`.
 *
 * Reading state inside the lambda defers execution to the Layout Phase,
 * allowing Compose to skip Composition entirely when the offset state changes!
 */
@Composable
fun OffsetDemo(modifier: Modifier = Modifier) {
    var offsetXPx by remember { mutableFloatStateOf(0f) }
    var recomposeCount by remember { mutableIntStateOf(0) }

    recomposeCount++

    SideEffect {
        Log.d("OffsetDemo", "Composition phase executed! Total Recompositions: $recomposeCount")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Layout-Phase State Read Demo",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Modifier.offset { IntOffset(...) } reads state inside a lambda during Layout Phase, skipping Composition!",
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
        ) {
            // Box moved via lambda-based offset modifier
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset { IntOffset(x = offsetXPx.roundToInt(), y = 0) } // <-- Layout-phase state read!
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Box", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = offsetXPx,
            onValueChange = { offsetXPx = it },
            valueRange = 0f..500f,
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { offsetXPx = if (offsetXPx >= 400f) 0f else offsetXPx + 100f }) {
            Text("Step Offset (+100px)")
        }
    }
}
