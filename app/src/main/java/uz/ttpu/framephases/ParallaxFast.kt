package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * FAST Parallax Implementation (OPTIMIZED):
 *
 * Reads `listState.firstVisibleItemScrollOffset` inside `Modifier.offset { IntOffset(...) }`.
 *
 * Result: The state read is DEFERRED to the Layout Phase lambda, completely skipping
 * Composition during scrolling! Recomposition count remains 1 while scrolling.
 */
@Composable
fun ParallaxFast(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    var recompositionCount by remember { mutableIntStateOf(0) }

    recompositionCount++

    SideEffect {
        Log.d("ParallaxFast", "Composition executed! Recomposition Count: $recompositionCount")
    }

    Column(modifier = modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Parallax FAST (Deferred Lambda Read)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Reads scroll offset inside Layout lambda. Recompositions: $recompositionCount",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Header with parallax offset applied via layout-phase IntOffset lambda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .offset {
                        val offset = if (listState.firstVisibleItemIndex == 0) {
                            listState.firstVisibleItemScrollOffset / 2
                        } else {
                            0
                        }
                        IntOffset(x = 0, y = offset) // <-- Lambda-deferred layout phase state read!
                    }
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Header Parallax (Fast)\nComposition Count: $recompositionCount",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(180.dp))
                }
                items(30) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "List Item #${index + 1}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
