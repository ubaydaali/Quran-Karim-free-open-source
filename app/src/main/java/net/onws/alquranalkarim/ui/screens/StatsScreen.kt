package net.onws.alquranalkarim.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.ui.theme.QuranColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    bookmarkManager: BookmarkManager,
    onBackClick: () -> Unit
) {
    val stats = remember { bookmarkManager.getReadingStats() }
    val khatmah = remember { bookmarkManager.getKhatmahProgress() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "إحصائيات القراءة",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = QuranColors.Primary,
                    titleContentColor = QuranColors.TextOnPrimary,
                    navigationIconContentColor = QuranColors.TextOnPrimary
                )
            )
        },
        containerColor = QuranColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Daily Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        "📈 التقدم اليومي",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Daily progress bar
                    val dailyProgress = (stats.dailyAyahsRead.toFloat() / stats.dailyGoal).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { dailyProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        color = if (dailyProgress >= 1f) QuranColors.Success else QuranColors.Primary,
                        trackColor = QuranColors.PrimarySurface,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "${stats.dailyAyahsRead} / ${stats.dailyGoal} آية اليوم",
                        fontSize = 14.sp,
                        color = QuranColors.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (dailyProgress >= 1f) {
                        Text(
                            "ما شاء الله! لقد حققت هدفك اليومي 🎉",
                            fontSize = 14.sp,
                            color = QuranColors.Success,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.AutoStories,
                    label = "إجمالي الآيات",
                    value = "${stats.totalAyahsRead}",
                    color = QuranColors.Primary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalFireDepartment,
                    label = "أيام متتالية",
                    value = "${stats.streak}",
                    color = QuranColors.Gold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.EmojiEvents,
                    label = "أطول سلسلة",
                    value = "${stats.longestStreak} يوم",
                    color = QuranColors.Gold
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.MenuBook,
                    label = "الختمات",
                    value = "${khatmah.completedKhatmahs}",
                    color = QuranColors.Primary
                )
            }

            // Reading goal card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuranColors.GoldSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Flag,
                        contentDescription = null,
                        tint = QuranColors.Gold,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "الهدف اليومي",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.TextPrimary
                        )
                        Text(
                            "${stats.dailyGoal} آية في اليوم",
                            fontSize = 14.sp,
                            color = QuranColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                fontSize = 12.sp,
                color = QuranColors.TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}