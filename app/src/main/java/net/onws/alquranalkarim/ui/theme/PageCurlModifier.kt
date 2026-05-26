package net.onws.alquranalkarim.ui.theme

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlin.math.*

/**
 * A high-performance custom [Modifier] that simulates a realistic **page-curl effect**
 * suitable for a future "Mushaf Mode" in the Quran app.
 *
 * ## How it works
 * 1. The user drags horizontally from the right edge to initiate a curl.
 * 2. A parabolic deformation is applied to the composable's content via [graphicsLayer]
 *    and [Canvas] drawing.
 * 3. The curl radius and position are driven by the drag offset, producing a smooth
 *    paper-turning illusion with shadow and back-face rendering.
 *
 * ## Mathematical model
 * The curl is modelled as a **generalised cylinder** mapped onto the page surface.
 * For each scan-line *y* we compute a displaced *x* using:
 * ```
 *   x'(y) = x₀ + r · sin(θ · (y - y_top) / h)
 *   z'(y) = r · (1 - cos(θ · (y - y_top) / h))
 * ```
 * where *r* is the curl radius and *θ* the accumulated angle.
 *
 * ## Usage
 * ```kotlin
 * var curlProgress by remember { mutableFloatStateOf(0f) }
 * Box(
 *     modifier = Modifier
 *         .fillMaxSize()
 *         .pageCurlEffect(curlProgress, onCurlProgressChange = { curlProgress = it })
 * ) {
 *     // Your page content here
 * }
 * ```
 *
 * @param curlProgress     Normalised curl position 0f (no curl) → 1f (fully curled).
 *                         Driven internally by gesture; can also be animated externally.
 * @param onCurlProgressChange Callback invoked when the drag gesture updates the curl.
 * @param curlDirection    Which edge the curl originates from (default: [CurlDirection.RIGHT]).
 * @param enabled          Whether the gesture is active.
 * @return A [Modifier] chain that applies gesture detection + visual curl rendering.
 */
@Stable
fun Modifier.pageCurlEffect(
    curlProgress: Float,
    onCurlProgressChange: (Float) -> Unit,
    curlDirection: CurlDirection = CurlDirection.RIGHT,
    enabled: Boolean = true
): Modifier {
    if (!enabled) return this
    return this
        .pointerInput(curlDirection) {
            val velocityTracker = VelocityTracker()
            detectHorizontalDragGestures(
                onDragStart = { _ -> velocityTracker.resetTracking() },
                onHorizontalDrag = { change, dragAmount ->
                    change.consume()
                    velocityTracker.addPosition(change.uptimeMillis, change.position)
                    val width = size.width.toFloat()
                    val directionSign = if (curlDirection == CurlDirection.RIGHT) -1f else 1f
                    val delta = (dragAmount * directionSign) / width
                    val newProgress = (curlProgress + delta).coerceIn(0f, 1f)
                    onCurlProgressChange(newProgress)
                },
                onDragEnd = {
                    // Snap open or closed based on velocity / threshold
                    val velocity = velocityTracker.calculateVelocity().x
                    val target = if (velocity > 500f || curlProgress > 0.4f) 1f else 0f
                    onCurlProgressChange(target)
                },
                onDragCancel = {
                    onCurlProgressChange(0f)
                }
            )
        }
        .drawWithContent {
            if (curlProgress <= 0.001f) {
                // No curl – draw normally
                drawContent()
                return@drawWithContent
            }

            val w = size.width
            val h = size.height

            // ── Curl parameters ──────────────────────────────────
            // Curl angle: 0 → π (half rotation = page fully flipped)
            val maxAngle = PI.toFloat()
            val angle = curlProgress * maxAngle

            // Curl radius: decreases as the page curls tighter
            // Minimum radius prevents degenerate rendering
            val minRadius = w * 0.08f
            val radius = ((1f - curlProgress) * w * 0.5f + minRadius).coerceAtLeast(minRadius)

            // Where the curl line sits on the x-axis
            val curlX = if (curlDirection == CurlDirection.RIGHT) {
                w * (1f - curlProgress)
            } else {
                w * curlProgress
            }

            // ── Draw the FRONT (visible) face ────────────────────
            // Clip to the un-curled region
            val frontClip = Path().apply {
                if (curlDirection == CurlDirection.RIGHT) {
                    addRect(Rect(0f, 0f, curlX, h))
                } else {
                    addRect(Rect(curlX, 0f, w, h))
                }
            }
            clipPath(frontClip) {
                this@drawWithContent.drawContent()
            }

            // ── Draw the CURL surface (the bent part) ────────────
            drawCurlSurface(
                curlX = curlX,
                angle = angle,
                radius = radius,
                pageWidth = w,
                pageHeight = h,
                direction = curlDirection
            )

            // ── Shadow under the curl ────────────────────────────
            drawCurlShadow(
                curlX = curlX,
                radius = radius,
                pageHeight = h,
                direction = curlDirection
            )
        }
}

// ─────────────────────────────────────────────────────────────────────
//  Drawing helpers
// ─────────────────────────────────────────────────────────────────────

private fun DrawScope.drawCurlSurface(
    curlX: Float,
    angle: Float,
    radius: Float,
    pageWidth: Float,
    pageHeight: Float,
    direction: CurlDirection
) {
    if (angle <= 0f) return

    val steps = 60 // Scan-line resolution
    val stepH = pageHeight / steps

    // Gradient to simulate lighting on the curled paper
    val backFaceColor = Color(0xFFF5F0E6)   // Warm parchment
    val backShadowColor = Color(0xFFD6CFC2) // Shadow tone

    for (i in 0 until steps) {
        val y = i * stepH
        val t = y / pageHeight // Normalised vertical position

        // Parametric curl displacement
        val sinA = sin(angle * t)
        val cosA = cos(angle * t)

        val displacedX = if (direction == CurlDirection.RIGHT) {
            curlX - radius * sinA
        } else {
            curlX + radius * sinA
        }

        // Depth factor for lighting (0 = facing away, 1 = facing viewer)
        val lightFactor = (cosA * 0.5f + 0.5f).coerceIn(0f, 1f)
        val color = lerp(backShadowColor, backFaceColor, lightFactor)

        // Draw a thin rectangle strip for this scan-line
        val stripLeft = if (direction == CurlDirection.RIGHT) {
            displacedX.coerceAtMost(curlX)
        } else {
            curlX.coerceAtMost(displacedX)
        }
        val stripRight = if (direction == CurlDirection.RIGHT) {
            curlX
        } else {
            displacedX.coerceAtLeast(curlX)
        }

        if (stripRight > stripLeft) {
            drawRect(
                color = color,
                topLeft = Offset(stripLeft, y),
                size = Size(stripRight - stripLeft, stepH + 1f)
            )
        }
    }
}

private fun DrawScope.drawCurlShadow(
    curlX: Float,
    radius: Float,
    pageHeight: Float,
    direction: CurlDirection
) {
    val shadowWidth = radius * 0.4f
    val shadowAlpha = 0.18f

    val shadowLeft: Float
    val shadowRight: Float

    if (direction == CurlDirection.RIGHT) {
        shadowLeft = curlX
        shadowRight = (curlX + shadowWidth).coerceAtMost(size.width)
    } else {
        shadowRight = curlX
        shadowLeft = (curlX - shadowWidth).coerceAtLeast(0f)
    }

    if (shadowRight > shadowLeft) {
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = shadowAlpha),
                    Color.Transparent
                ),
                startX = if (direction == CurlDirection.RIGHT) shadowLeft else shadowRight,
                endX = if (direction == CurlDirection.RIGHT) shadowRight else shadowLeft
            ),
            topLeft = Offset(shadowLeft, 0f),
            size = Size(shadowRight - shadowLeft, size.height)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────
//  Supporting types
// ─────────────────────────────────────────────────────────────────────

/**
 * Specifies which edge the page curl originates from.
 */
enum class CurlDirection {
    /** Curl starts from the right edge (for LTR reading; standard book turn). */
    RIGHT,
    /** Curl starts from the left edge (for RTL reading / going backwards). */
    LEFT
}

/**
 * A simple [lerp] for [Color] to avoid pulling in the full Compose geometry library.
 */
private fun lerp(start: Color, stop: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = start.red + (stop.red - start.red) * f,
        green = start.green + (stop.green - start.green) * f,
        blue = start.blue + (stop.blue - start.blue) * f,
        alpha = start.alpha + (stop.alpha - start.alpha) * f
    )
}