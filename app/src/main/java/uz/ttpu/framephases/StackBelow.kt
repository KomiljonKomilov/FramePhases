package uz.ttpu.framephases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Custom `Layout` composable that stacks its children vertically one below the other with spacing.
 *
 * Implements the 3-Step Custom Layout Algorithm:
 * 1. Measure Children: Measure all measurables with loose constraints.
 * 2. Calculate Own Size: Total width = max width of children, Total height = sum of heights + spacing.
 * 3. Place Children: Sequentially place children vertically by updating yPosition.
 */
@Composable
fun StackBelow(
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // Step 1: Measure children with loose constraints
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { measurable ->
            measurable.measure(looseConstraints)
        }

        val spacingPx = spacing.roundToPx()

        // Step 2: Calculate own layout size
        var totalHeight = 0
        var maxWidth = 0

        placeables.forEachIndexed { index, placeable ->
            maxWidth = max(maxWidth, placeable.width)
            totalHeight += placeable.height
            if (index < placeables.size - 1) {
                totalHeight += spacingPx
            }
        }

        val layoutWidth = maxWidth.coerceIn(constraints.minWidth, constraints.maxWidth)
        val layoutHeight = totalHeight.coerceIn(constraints.minHeight, constraints.maxHeight)

        // Step 3: Place children sequentially vertically with spacing
        layout(layoutWidth, layoutHeight) {
            var yPosition = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height + spacingPx
            }
        }
    }
}

/**
 * Demo Composable illustrating `StackBelow` custom layout in action.
 */
@Composable
fun StackBelowDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Custom Layout: StackBelow",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = "Custom Layout implementing 3-step measurement & placement algorithm placing children vertically with 12dp spacing.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Testing custom StackBelow layout with 12.dp spacing
        StackBelow(
            spacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFE91E63), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text("Custom Child #1 (Height 50dp)", color = Color.White)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(70.dp)
                    .background(Color(0xFF2196F3), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text("Custom Child #2 (Height 70dp, 85% width)", color = Color.White)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(40.dp)
                    .background(Color(0xFF4CAF50), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text("Custom Child #3 (Height 40dp, 70% width)", color = Color.White)
            }
        }
    }
}
