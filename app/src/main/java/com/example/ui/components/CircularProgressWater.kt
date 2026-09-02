package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HydrationState
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.SuccessTurquoise
import com.example.ui.theme.TurquoisePrimary
import com.example.ui.theme.TurquoiseSecondary
import com.example.ui.theme.WaterGradientEnd
import com.example.ui.theme.WaterGradientMiddle
import com.example.ui.theme.WaterGradientStart
import kotlin.math.sin

@Composable
fun CircularProgressWater(
    state: HydrationState,
    modifier: Modifier = Modifier
) {
    // Smooth progress animation
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "water_progress"
    )

    // Wave animation inside the circle
    val infiniteTransition = rememberInfiniteTransition(label = "wave_motion")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )

    val progressBrush = Brush.sweepGradient(
        colors = listOf(
            WaterGradientStart,
            WaterGradientMiddle,
            WaterGradientEnd,
            WaterGradientStart
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(270.dp)
            .testTag("hydration_progress_circle")
    ) {
        // Background canvas for subtle glow and inner wave
        Canvas(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerOffset = Offset(canvasWidth / 2f, canvasHeight / 2f)

            // Inner dark circular background
            drawCircle(
                color = Color(0xFF0F1A1E),
                radius = canvasWidth / 2f,
                center = centerOffset
            )

            // Animated wave fill representing fluid level
            if (animatedProgress > 0.01f) {
                val waterLevelY = canvasHeight * (1f - animatedProgress.coerceIn(0f, 1f))
                val wavePath = Path().apply {
                    moveTo(0f, canvasHeight)
                    lineTo(0f, waterLevelY)

                    val waveAmplitude = 10f * (1f - (animatedProgress - 0.5f).let { it * it * 2 })
                    val waveFrequency = 1.5f

                    var x = 0f
                    while (x <= canvasWidth) {
                        val y = waterLevelY + waveAmplitude * sin(
                            (x / canvasWidth * waveFrequency * 2 * Math.PI + waveOffset).toDouble()
                        ).toFloat()
                        lineTo(x, y)
                        x += 8f
                    }

                    lineTo(canvasWidth, canvasHeight)
                    close()
                }

                drawPath(
                    path = wavePath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            TurquoisePrimary.copy(alpha = 0.22f),
                            WaterGradientEnd.copy(alpha = 0.40f)
                        ),
                        startY = waterLevelY,
                        endY = canvasHeight
                    )
                )
            }
        }

        // Circular progress ring track and stroke
        Canvas(modifier = Modifier.size(255.dp)) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

            // Background Track
            drawArc(
                color = DarkSurfaceVariant.copy(alpha = 0.6f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Outer subtle track border
            drawArc(
                color = DarkOutline.copy(alpha = 0.3f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2f - 2.dp.toPx(), strokeWidth / 2f - 2.dp.toPx()),
                size = Size(diameter + 4.dp.toPx(), diameter + 4.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )

            // Foreground Progress Arc
            if (animatedProgress > 0f) {
                drawArc(
                    brush = progressBrush,
                    startAngle = -90f,
                    sweepAngle = (animatedProgress * 360f).coerceIn(0f, 360f),
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Center Content & Metrics
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Icon / Goal indicator
            if (state.isGoalReached) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SuccessTurquoise.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Objectif atteint",
                        tint = SuccessTurquoise,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Objectif atteint !",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SuccessTurquoise
                        )
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(TurquoisePrimary.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Eau",
                        tint = TurquoisePrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Objectif ${state.goalLitersFormatted} L",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TurquoiseSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Current Intake Display
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${state.currentIntakeMl}",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    ),
                    modifier = Modifier.testTag("current_intake_text")
                )
                Text(
                    text = " ml",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TurquoisePrimary
                    ),
                    modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                )
            }

            // Subtitle Goal
            Text(
                text = "/ ${state.goalMl} ml (2.0L)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.65f),
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Percentage Badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = TurquoisePrimary.copy(alpha = 0.18f),
                modifier = Modifier.testTag("percentage_badge")
            ) {
                Text(
                    text = "${state.progressPercent}%",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TurquoisePrimary
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
    }
}
