package uz.ttpu.framephases

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.layout

/**
 * Extension function to log execution across the 3 Compose Frame Phases:
 * 1. Composition Phase: Evaluated when the modifier is composed/recomposed.
 * 2. Layout Phase:
 *    a) Measuring Phase: Measurable is measured against parent constraints.
 *    b) Placing Phase: Placeable position is set inside layout(w, h) block.
 * 3. Drawing Phase: Evaluated inside drawBehind when pixels are rendered on canvas.
 *
 * @param name Tag/Identifier for the composable node being tracked.
 */
fun Modifier.logPhases(name: String): Modifier {
    Log.d("PhaseLog", "[$name] 1. Composition Phase")
    println("[$name] 1. Composition Phase")

    return this
        .layout { measurable, constraints ->
            Log.d("PhaseLog", "[$name] 2a. Layout Phase - Measuring")
            println("[$name] 2a. Layout Phase - Measuring")

            val placeable = measurable.measure(constraints)

            layout(placeable.width, placeable.height) {
                Log.d("PhaseLog", "[$name] 2b. Layout Phase - Placing")
                println("[$name] 2b. Layout Phase - Placing")
                placeable.placeRelative(0, 0)
            }
        }
        .drawBehind {
            Log.d("PhaseLog", "[$name] 3. Draw Phase")
            println("[$name] 3. Draw Phase")
        }
}
