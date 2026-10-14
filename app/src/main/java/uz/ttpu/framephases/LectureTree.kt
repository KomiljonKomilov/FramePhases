package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Composable demonstrating the Jetpack Compose layout tree hierarchy:
 * Row (Parent)
 *  ├── Box / Image (Child 1)
 *  └── Column (Child 2)
 *       ├── Text A (Grandchild 1 - state bound)
 *       └── Text B (Grandchild 2)
 *
 * Utilizes `SideEffect` and `logPhases` modifier to demonstrate phase execution order
 * during initial composition and recompositions.
 */
@Composable
fun LectureTreeDemo(modifier: Modifier = Modifier) {
    var counter by remember { mutableIntStateOf(0) }

    SideEffect {
        Log.d("PhaseLog", "[LectureTreeDemo] SideEffect executed during Composition (counter = $counter)")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Part 2: Lecture Tree Hierarchy Phase Logs",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Check Logcat (tag: 'PhaseLog') to observe phase logs across Parent, Child, and Grandchild nodes.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .logPhases("Card Container"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .logPhases("Row (Parent)"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image / Box Child
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF6200EE), RoundedCornerShape(12.dp))
                        .logPhases("Box (Image Child)"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "IMG", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Column Child
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .logPhases("Column (Text Container)")
                ) {
                    Text(
                        text = "Text A (Counter: $counter)",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.logPhases("Text A")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Text B (Static text)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.logPhases("Text B")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { counter++ }) {
            Text(text = "Trigger Recomposition (Counter: $counter)")
        }
    }
}
