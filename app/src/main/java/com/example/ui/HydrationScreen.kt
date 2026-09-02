package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CircularProgressWater
import com.example.ui.components.HydrationHistory
import com.example.ui.components.HydrationTipCard
import com.example.ui.components.QuickAddButtons
import com.example.ui.components.ResetConfirmDialog
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.SuccessTurquoise
import com.example.ui.theme.TurquoisePrimary
import com.example.ui.theme.TurquoiseSecondary
import com.example.viewmodel.HydrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationScreen(
    viewModel: HydrationViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TurquoisePrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "HydroTrack Logo",
                                tint = TurquoisePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "HydroTrack",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Suivi d'hydratation",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TurquoiseSecondary.copy(alpha = 0.8f)
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Daily Streak Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CardSurface,
                        border = BorderStroke(1.dp, DarkOutline),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .testTag("streak_badge")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Série",
                                tint = Color(0xFFFFB74D),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${state.streakDays} j",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Stat Summary Cards Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatSummaryCard(
                        title = "Consommé",
                        value = "${state.currentIntakeMl} ml",
                        accentColor = TurquoisePrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatSummaryCard(
                        title = "Objectif",
                        value = "2 000 ml",
                        accentColor = TurquoiseSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    StatSummaryCard(
                        title = if (state.isGoalReached) "Statut" else "Restant",
                        value = if (state.isGoalReached) "Atteint ✓" else "${state.remainingMl} ml",
                        accentColor = if (state.isGoalReached) SuccessTurquoise else Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Main Circular Progress View
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressWater(state = state)
                }
            }

            // Motivational Banner
            item {
                MotivationalBanner(state = state)
            }

            // Quick Add Buttons & Reset (+250 ml hero button)
            item {
                QuickAddButtons(
                    onAddWater = { amount, label -> viewModel.addWater(amount, label) },
                    onResetClick = { showResetDialog = true },
                    onUndoClick = { viewModel.undoLast() },
                    hasLogs = state.logs.isNotEmpty()
                )
            }

            // Tip of the day
            item {
                HydrationTipCard()
            }

            // Daily drink logs history
            item {
                HydrationHistory(
                    logs = state.logs,
                    onDeleteLog = { logId -> viewModel.removeLog(logId) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Reset confirmation dialog
        if (showResetDialog) {
            ResetConfirmDialog(
                onConfirm = {
                    viewModel.resetIntake()
                    showResetDialog = false
                },
                onDismiss = {
                    showResetDialog = false
                }
            )
        }
    }
}

@Composable
fun StatSummaryCard(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, DarkOutline.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            )
        }
    }
}

@Composable
fun MotivationalBanner(
    state: com.example.model.HydrationState,
    modifier: Modifier = Modifier
) {
    val (message, icon) = when {
        state.isGoalReached -> Pair("Bravo ! Vous avez atteint votre objectif de 2 Litres pour aujourd'hui 🎉", Icons.Default.EmojiEvents)
        state.progress >= 0.75f -> Pair("Presque au but ! Plus qu'un ou deux verres pour finaliser vos 2L 💪", Icons.Default.WaterDrop)
        state.progress >= 0.5f -> Pair("À mi-parcours ! Continuez sur cette belle lancée 💧", Icons.Default.WaterDrop)
        state.progress > 0f -> Pair("Bon début de journée ! Pensez à boire à intervalles réguliers ✨", Icons.Default.WaterDrop)
        else -> Pair("Commencez votre journée en buvant un premier verre de 250 ml ☀️", Icons.Default.WaterDrop)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = if (state.isGoalReached) SuccessTurquoise.copy(alpha = 0.12f) else TurquoisePrimary.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, if (state.isGoalReached) SuccessTurquoise.copy(alpha = 0.3f) else TurquoisePrimary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (state.isGoalReached) SuccessTurquoise else TurquoisePrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            )
        }
    }
}
