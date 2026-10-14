package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Sibling composable that receives no state parameters.
 * Used to verify that updating parent state (passed via lambda to Child)
 * does NOT cause Sibling to recompose.
 */
@Composable
fun Sibling(
    modifier: Modifier = Modifier
) {
    var siblingRecomposeCount by remember { mutableIntStateOf(0) }
    siblingRecomposeCount++

    SideEffect {
        Log.d("LambdaReadDemo", "Sibling Composition executed! Total Recompositions: $siblingRecomposeCount")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Sibling Composable",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = "Sibling Recomposition Count: $siblingRecomposeCount",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sibling receives no state and stays completely unchanged when count increments.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}
