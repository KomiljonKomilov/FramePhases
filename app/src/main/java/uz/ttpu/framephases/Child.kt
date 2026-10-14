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
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Child composable accepting state read deferred via a lambda `countProvider: () -> Int`.
 *
 * When `countProvider` is read inside the `Modifier.offset { IntOffset(...) }` lambda,
 * state resolution is DEFERRED to the Layout Phase.
 *
 * Consequently, when `count` increments:
 * 1. Parent does NOT recompose.
 * 2. Child does NOT recompose.
 * 3. Sibling does NOT recompose.
 * Only the Layout phase of Child runs to shift the position!
 */
@Composable
fun Child(
    countProvider: () -> Int,
    modifier: Modifier = Modifier
) {
    var childRecomposeCount by remember { mutableIntStateOf(0) }
    childRecomposeCount++

    SideEffect {
        Log.d("LambdaReadDemo", "Child Composition executed! Total Recompositions: $childRecomposeCount")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Child Composable",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Child Recomposition Count: $childRecomposeCount",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Box moving horizontal position driven by countProvider inside layout lambda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(x = (countProvider() * 10) % 300, y = 0) } // <-- Deferred lambda state read!
                        .padding(4.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Offset Box",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
