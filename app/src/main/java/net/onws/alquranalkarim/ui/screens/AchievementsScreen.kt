package net.onws.alquranalkarim.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import net.onws.alquranalkarim.data.local.Achievement
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.ui.theme.QuranColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    bookmarkManager: BookmarkManager,
    onBackClick: () -> Unit
) {
    val achievements = remember { bookmarkManager.getAchievements() }
    val unlockedCount = achievements.count { it.isUnlocked }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("شارات الإنجاز", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = QuranColors.Primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(QuranColors.Background)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranColors.PrimarySurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏆",
                            fontSize = 40.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$unlockedCount / ${achievements.size}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.Primary
                        )
                        Text(
                            text = "شارة مفتوحة",
                            fontSize = 14.sp,
                            color = QuranColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = {
                                if (achievements.isNotEmpty())
                                    unlockedCount.toFloat() / achievements.size
                                else 0f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(
                                    QuranColors.Background,
                                    RoundedCornerShape(4.dp)
                                ),
                            color = QuranColors.Primary,
                            trackColor = Color.Transparent,
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    }
                }
            }

            // Unlocked
            if (achievements.any { it.isUnlocked }) {
                item {
                    Text(
                        text = "✅ مفتوحة",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranColors.TextPrimary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(achievements.filter { it.isUnlocked }) { achievement ->
                    AchievementCard(achievement = achievement, unlocked = true)
                }
            }

            // Locked
            if (achievements.any { !it.isUnlocked }) {
                item {
                    Text(
                        text = "🔒 مقفلة",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranColors.TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(achievements.filter { !it.isUnlocked }) { achievement ->
                    AchievementCard(achievement = achievement, unlocked = false)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun AchievementCard(
    achievement: Achievement,
    unlocked: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) QuranColors.Surface
            else QuranColors.Surface.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = achievement.icon,
                fontSize = 32.sp,
                modifier = Modifier.graphicsLayer { alpha = if (unlocked) 1f else 0.3f }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (unlocked) QuranColors.TextPrimary
                    else QuranColors.TextSecondary
                )
                Text(
                    text = achievement.description,
                    fontSize = 13.sp,
                    color = QuranColors.TextSecondary
                )
                if (unlocked && achievement.unlockedAt > 0) {
                    Text(
                        text = "تم الفتح: ${
                            java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault())
                                .format(java.util.Date(achievement.unlockedAt))
                        }",
                        fontSize = 11.sp,
                        color = QuranColors.Primary
                    )
                }
            }
        }
    }
}

