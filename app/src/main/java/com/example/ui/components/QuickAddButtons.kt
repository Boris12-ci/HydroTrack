package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.ResetRed
import com.example.ui.theme.TurquoiseOnPrimary
import com.example.ui.theme.TurquoisePrimary
import com.example.ui.theme.TurquoiseSecondary

@Composable
fun QuickAddButtons(
    onAddWater: (amountMl: Int, label: String) -> Unit,
    onResetClick: () -> Unit,
    onUndoClick: () -> Unit,
    hasLogs: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Primary Button: + 250 ml
        Button(
            onClick = { onAddWater(250, "Verre d'eau") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .testTag("add_250ml_button"),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TurquoisePrimary,
                contentColor = TurquoiseOnPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 1.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.LocalDrink,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Ajouter 250 ml",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        letterSpacing = 0.3.sp
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TurquoiseOnPrimary.copy(alpha = 0.15f),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = "1 verre",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TurquoiseOnPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Secondary quick add row for extra convenience (+100ml, +500ml)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // +100 ml
            ElevatedButton(
                onClick = { onAddWater(100, "Gorgée d'eau") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("add_100ml_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = CardSurfaceElevated,
                    contentColor = TurquoiseSecondary
                ),
                border = BorderStroke(1.dp, DarkOutline.copy(alpha = 0.6f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TurquoiseSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ 100 ml",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // +500 ml
            ElevatedButton(
                onClick = { onAddWater(500, "Gourde d'eau") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("add_500ml_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = CardSurfaceElevated,
                    contentColor = TurquoiseSecondary
                ),
                border = BorderStroke(1.dp, DarkOutline.copy(alpha = 0.6f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalDrink,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TurquoiseSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ 500 ml",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // Action Row: Reset & Undo buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset Button
            OutlinedButton(
                onClick = onResetClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("reset_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ResetRed
                ),
                border = BorderStroke(1.dp, ResetRed.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "Réinitialiser",
                    modifier = Modifier.size(18.dp),
                    tint = ResetRed
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Réinitialiser",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = ResetRed
                    )
                )
            }

            // Undo Button (active only if there are logs)
            if (hasLogs) {
                OutlinedButton(
                    onClick = onUndoClick,
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("undo_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White.copy(alpha = 0.8f)
                    ),
                    border = BorderStroke(1.dp, DarkOutline)
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Annuler le dernier ajout",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Annuler",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}
