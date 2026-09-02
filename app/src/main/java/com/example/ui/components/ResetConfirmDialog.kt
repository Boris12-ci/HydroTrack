package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CardSurface
import com.example.ui.theme.ResetRed
import com.example.ui.theme.TurquoiseSecondary

@Composable
fun ResetConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("reset_dialog"),
        shape = RoundedCornerShape(20.dp),
        containerColor = CardSurface,
        icon = {
            Icon(
                imageVector = Icons.Default.WarningAmber,
                contentDescription = null,
                tint = ResetRed
            )
        },
        title = {
            Text(
                text = "Réinitialiser la journée ?",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        },
        text = {
            Text(
                text = "Cette action remettra votre niveau d'eau actuel à 0 ml pour la journée.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.75f)
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.testTag("confirm_reset_button"),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ResetRed
                )
            ) {
                Text(
                    text = "Réinitialiser",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_reset_button"),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TurquoiseSecondary
                )
            ) {
                Text("Annuler")
            }
        }
    )
}
