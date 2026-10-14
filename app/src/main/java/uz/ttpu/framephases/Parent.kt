package uz.ttpu.framephases

import android.util.Log
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
import androidx.compose.ui.unit.dp

/**
 * Parent composable demonstrating Lambda-Deferred State Reads to skip recompositions.
 *
 * `Parent` holds state `var count by remember { mutableIntStateOf(0) }`.
 * `Parent` passes state to `Child` as a lambda function: `countProvider = { count }`.
 *
 * Because `count` is never read directly in `Parent`'s composition body,
 * updating `count` skips recomposing `Parent`, `Child`, AND `Sibling`!
 */
@Composable
fun ParentDemo(modifier: Modifier = Modifier) {
    var count by remember { mutableIntStateOf(0) }
    var parentRecomposeCount by remember { mutableIntStateOf(0) }

    parentRecomposeCount++

    SideEffect {
        Log.d("LambdaReadDemo", "Parent Composition executed! Total Recompositions: $parentRecomposeCount")
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
                    text = "Parent Composable (State Holder)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Parent Recomposition Count: $parentRecomposeCount",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "State 'count' is passed to Child via lambda: { count }. State is read strictly inside Child's layout modifier lambda.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Child component receiving lambda-deferred read
        Child(countProvider = { count })

        Spacer(modifier = Modifier.height(8.dp))

        // Sibling component receiving no state
        Sibling()

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { count++ }) {
            Text("Increment State 'count' (Current: $count)")
        }
    }
}
