package net.onws.alquranalkarim.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import net.onws.alquranalkarim.ui.theme.QuranColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "حول التطبيق",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = QuranColors.PrimarySurface,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = QuranColors.Primary,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "القرآن الكريم",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranColors.TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        "الإصدار 1.0",
                        fontSize = 14.sp,
                        color = QuranColors.TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "تم تطويره بواسطة عبيدة علي",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = QuranColors.Primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Open source & description card
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            tint = QuranColors.Gold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "صدقة جارية",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "هذا التطبيق مفتوح المصدر يمكن لأي مطور أو مبرمج الاستفادة منه وإعادة استخدامه. وهو صدقة جارية نسأل الله القبول.",
                        fontSize = 15.sp,
                        lineHeight = 28.sp,
                        color = QuranColors.TextPrimary,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "عن أبي وأمي رحمهما الله، وعن موتانا وموتاكم جميعاً. نسأل الله قبول العمل.",
                        fontSize = 14.sp,
                        lineHeight = 26.sp,
                        color = QuranColors.TextSecondary,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // GitHub link card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuranColors.PrimarySurface),
                elevation = CardDefaults.cardElevation(2.dp),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ubaydaali/Quran-Karim-free-open-source"))
                    context.startActivity(intent)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = QuranColors.Primary,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Code,
                                contentDescription = null,
                                tint = QuranColors.TextOnPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "المشروع على GitHub",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.Primary
                        )
                        Text(
                            "لتحميل المشروع كاملاً وتشغيله على بيئة Android Studio",
                            fontSize = 13.sp,
                            color = QuranColors.TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            "https://github.com/ubaydaali/Quran-Karim-free-open-source",
                            fontSize = 12.sp,
                            color = QuranColors.Primary,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                    Icon(
                        Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = QuranColors.Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Sources section
            Text(
                "مصادر البيانات",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = QuranColors.Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp)
            )

            SourceItem(
                icon = Icons.Default.Book,
                title = "النص القرآني",
                description = "من مشروع Tanzil - tanzil.net (مرخص للاستخدام)"
            )

            SourceItem(
                icon = Icons.Default.Cloud,
                title = "واجهة برمجة التطبيقات",
                description = "alquran.cloud API - مفتوح ومجانياً للاستخدام"
            )

            SourceItem(
                icon = Icons.Default.Headphones,
                title = "الصوت والتلاوة",
                description = "من إذاعات القرآن الرسمية وقرّاء معتمدين"
            )

            SourceItem(
                icon = Icons.Default.Translate,
                title = "التراجم والتفاسير",
                description = "من مصادر مرخصة للاستخدام العام"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SourceItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = QuranColors.GoldSurface,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = QuranColors.GoldDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextPrimary,
                    fontSize = 15.sp
                )
                Text(
                    description,
                    fontSize = 13.sp,
                    color = QuranColors.TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}