package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp

/**
 * FIXED Cyclic Phase Dependency Solution:
 *
 * Sizing/Layout adjustments are handled ENTIRELY inside `Modifier.layout`.
 *
 * Because layout logic executes during the Layout Phase without mutating Composition state,
 * there is zero feedback loop into Composition!
 */
@Composable
fun CyclicFixed(modifier: Modifier = Modifier) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    recompositionCount++

    SideEffect {
        Log.d("CyclicFixed", "Composition executed! Recomposition Count: $recompositionCount")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cyclic Phase Dependency (Fixed Solution)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sizing logic performed directly in Layout Modifier. Zero state mutations in layout callbacks!",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total Recompositions: $recompositionCount (Stays 1!)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Custom layout modifier adjusting size directly in Layout Phase!
                .layout { measurable, constraints ->
                    val desiredHeight = 100.dp.roundToPx()
                    val placeable = measurable.measure(
                        constraints.copy(
                            minHeight = desiredHeight,
                            maxHeight = desiredHeight
                        )
                    )

                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Fixed Layout Modifier (Single Pass)",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
