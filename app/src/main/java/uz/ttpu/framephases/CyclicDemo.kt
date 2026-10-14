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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * Demonstrates Cyclic Phase Dependency (Phase Loop Anti-Pattern):
 *
 * Sequence of the bug:
 * 1. Composition phase reads `componentHeightPx` state to calculate `Modifier.height()`.
 * 2. Layout phase measures the box and triggers `Modifier.onSizeChanged { size -> componentHeightPx = size.height }`.
 * 3. Mutating `componentHeightPx` state invalidates Composition phase.
 * 4. Step 1 repeats -> Loop repeats every frame!
 */
@Composable
fun CyclicDemo(modifier: Modifier = Modifier) {
    var componentHeightPx by remember { mutableIntStateOf(100) }
    var loopIterationCount by remember { mutableIntStateOf(0) }
    var isLoopActive by remember { androidx.compose.runtime.mutableStateOf(false) }

    val density = LocalDensity.current
    val calculatedHeightDp = with(density) { componentHeightPx.toDp() }

    SideEffect {
        Log.d("CyclicDemo", "Composition triggered! Loop iteration count: $loopIterationCount")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cyclic Phase Dependency (Anti-Pattern)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Composition Reads State → Layout triggers onSizeChanged → Updates State → Triggers Composition Loop!",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total Loop Iterations / Recompositions: $loopIterationCount",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(calculatedHeightDp) // <-- Step 1: Composition reads state
                .background(MaterialTheme.colorScheme.error)
                .onSizeChanged { newSize ->
                    // <-- Step 2: Layout callback mutates state!
                    if (isLoopActive && loopIterationCount < 50) {
                        loopIterationCount++
                        // Oscillate size slightly to trigger endless layout feedback loop
                        componentHeightPx = if (newSize.height % 2 == 0) 120 else 100
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isLoopActive) "Cyclic Loop Running ($loopIterationCount)" else "Loop Paused",
                color = MaterialTheme.colorScheme.onError
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoopActive = !isLoopActive
                if (!isLoopActive) {
                    loopIterationCount = 0
                    componentHeightPx = 100
                }
            }
        ) {
            Text(if (isLoopActive) "Pause Cyclic Loop" else "Simulate Cyclic Loop")
        }
    }
}
