package uz.ttpu.framephases

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Demonstrates Scroll-to-Top button implementation comparing:
 * 1. Direct State Read (Naive): `listState.firstVisibleItemIndex > 0` causes recomposition on every pixel scroll.
 * 2. `derivedStateOf` (Optimized): Only triggers recomposition when the boolean result changes threshold (true/false).
 */
@Composable
fun ScrollToTopDemo(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var useDerivedState by remember { mutableStateOf(true) }
    var recompositionCount by remember { mutableIntStateOf(0) }

    recompositionCount++

    SideEffect {
        Log.d("ScrollToTopDemo", "Composition executed! Recomposition Count: $recompositionCount")
    }

    // High-frequency state read buffered through derivedStateOf
    val showScrollToTop by remember(useDerivedState) {
        if (useDerivedState) {
            derivedStateOf { listState.firstVisibleItemIndex > 0 }
        } else {
            // Direct state evaluation during composition (Naive approach)
            mutableStateOf(listState.firstVisibleItemIndex > 0)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (useDerivedState)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (useDerivedState) "Mode: derivedStateOf (Optimized)" else "Mode: Direct Read (Naive)",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Recomposition Count: $recompositionCount",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Switch(
                            checked = useDerivedState,
                            onCheckedChange = { useDerivedState = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (useDerivedState)
                            "derivedStateOf buffers high-frequency scroll state so recomposition only happens when boolean changes!"
                        else
                            "Direct read forces recomposition on every item index/scroll update!",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(50) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Item #${index + 1}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // Scroll-to-Top Floating Action Button
        AnimatedVisibility(
            visible = showScrollToTop,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("▲ Top", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
