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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * SLOW Parallax Implementation (UNOPTIMIZED):
 *
 * Reads `listState.firstVisibleItemScrollOffset` directly in the Composition Phase,
 * converting it to Dp and passing to `Modifier.offset(y = scrollOffsetDp)`.
 *
 * Result: Forces RECOMPOSITION on EVERY single pixel scrolled!
 */
@Composable
fun ParallaxSlow(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    var recompositionCount by remember { mutableIntStateOf(0) }

    recompositionCount++

    SideEffect {
        Log.d("ParallaxSlow", "Composition executed! Recomposition Count: $recompositionCount")
    }

    // Direct state read in Composition phase!
    val firstItemOffset = if (listState.firstVisibleItemIndex == 0) {
        listState.firstVisibleItemScrollOffset
    } else {
        0
    }
    val density = LocalDensity.current
    val parallaxOffsetDp = with(density) { (firstItemOffset / 2).toDp() }

    Column(modifier = modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Parallax SLOW (Composition Read)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "Reads scroll offset during Composition. Recompositions: $recompositionCount",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Header with parallax offset applied via composition-phase Dp parameter
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .offset(y = parallaxOffsetDp) // <-- Unoptimized direct Dp read
                    .background(Color(0xFFE91E63)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Header Parallax (Slow)\nOffset: ${parallaxOffsetDp.value.toInt()}dp",
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
