package net.onws.alquranalkarim.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.data.local.KhatmahProgress
import net.onws.alquranalkarim.ui.theme.QuranColors

// Surah count per juz (para) for progress calculation
private val surahsInQuran = 114
private val totalAyahsInQuran = 6236

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhatmahScreen(
    bookmarkManager: BookmarkManager,
    onBackClick: () -> Unit,
    onNavigateToRead: (Int, String, Int) -> Unit
) {
    var khatmah by remember { mutableStateOf(bookmarkManager.getKhatmahProgress()) }
    var showCompleteDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "متابعة الختمة",
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Current Khatmah Number
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(QuranColors.GradientStart.copy(alpha = 0.1f), QuranColors.Surface)
                            )
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = QuranColors.Primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "الختمة رقم ${khatmah.currentKhatmah}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranColors.Primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "آخر قراءة: سورة ${khatmah.lastSurah} - الآية ${khatmah.lastAyah}",
                        fontSize = 16.sp,
                        color = QuranColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Completed Khatmahs
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuranColors.GoldSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = QuranColors.Gold,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "عدد الختمات المكتملة: ${khatmah.completedKhatmahs}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranColors.GoldDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Button(
                onClick = {
                    onNavigateToRead(khatmah.lastSurah, "", khatmah.lastAyah)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = QuranColors.Primary)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "متابعة القراءة من حيث توقفت",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuranColors.Primary)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "بدء ختمة جديدة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showCompleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuranColors.Gold)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "تسجيل إكمال ختمة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Complete Khatmah Dialog
    if (showCompleteDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = QuranColors.Gold,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "ما شاء الله! 🎉",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "هل أكملت ختمة القرآن الكريم؟\nبارك الله فيك!",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookmarkManager.completeKhatmah()
                        khatmah = bookmarkManager.getKhatmahProgress()
                        showCompleteDialog = false
                    }
                ) {
                    Text("نعم، الحمد لله", color = QuranColors.Success, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteDialog = false }) {
                    Text("ليس بعد")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = QuranColors.Surface
        )
    }

    // Reset Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("بدء ختمة جديدة", fontWeight = FontWeight.Bold) },
            text = { Text("هل تريد البدء من سورة الفاتحة؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newProgress = khatmah.copy(
                            lastSurah = 1,
                            lastAyah = 1,
                            startDate = System.currentTimeMillis()
                        )
                        bookmarkManager.saveKhatmahProgress(newProgress)
                        khatmah = newProgress
                        showResetDialog = false
                    }
                ) {
                    Text("نعم", color = QuranColors.Primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("إلغاء")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = QuranColors.Surface
        )
    }
}