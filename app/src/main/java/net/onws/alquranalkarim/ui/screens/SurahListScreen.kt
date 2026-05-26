package net.onws.alquranalkarim.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.data.local.LastRead
import net.onws.alquranalkarim.data.model.SurahInfo
import net.onws.alquranalkarim.ui.theme.QuranColors
import net.onws.alquranalkarim.ui.viewmodel.SurahListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(
    viewModel: SurahListViewModel,
    bookmarkManager: BookmarkManager,
    onSurahClick: (Int, String) -> Unit,
    onContinueReading: (Int, String, Int) -> Unit,
    onAdhkarClick: () -> Unit,
    onBookmarksClick: () -> Unit = {},
    onKhatmahClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNamesOfAllahClick: () -> Unit = {},
    onDuaQunutClick: () -> Unit = {},
    onAchievementsClick: () -> Unit = {},
    onTopicIndexClick: () -> Unit = {},
    onPageNavigatorClick: () -> Unit = {},
    onProphetStoriesClick: () -> Unit = {},
    onFiqhClick: () -> Unit = {},
    onMemorizationClick: () -> Unit = {},
    onHizbClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) }
    var searchExpanded by remember { mutableStateOf(false) }
    val lastRead = remember { mutableStateOf(bookmarkManager.getLastRead()) }

    // Refresh last read when screen is resumed
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                lastRead.value = bookmarkManager.getLastRead()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showDownloadDialog by remember { mutableStateOf(false) }

    // Download confirmation dialog
    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = QuranColors.Surface,
            icon = {
                Surface(
                    shape = CircleShape,
                    color = QuranColors.PrimarySurface,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.CloudDownload,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = QuranColors.Primary
                        )
                    }
                }
            },
            title = {
                Text(
                    text = "تحميل للقراءة بدون إنترنت",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = QuranColors.Primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column {
                    Text(
                        text = "هل تريد تحميل نصوص سور القرآن الكريم بالكامل لقراءتها بدون اتصال بالإنترنت؟",
                        fontSize = 14.sp,
                        color = QuranColors.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (uiState.isDownloadingAll) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            LinearProgressIndicator(
                                progress = { uiState.downloadProgress.toFloat() / uiState.downloadTotal },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = QuranColors.Gold,
                                trackColor = QuranColors.PrimarySurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "جاري التحميل... ${uiState.downloadProgress} / ${uiState.downloadTotal}",
                                fontSize = 13.sp,
                                color = QuranColors.TextTertiary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else if (uiState.cachedCount >= 114) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = QuranColors.Success,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تم تحميل جميع السور بالفعل ✓",
                                fontSize = 14.sp,
                                color = QuranColors.Success,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (!uiState.isDownloadingAll && uiState.cachedCount < 114) {
                    Button(
                        onClick = {
                            viewModel.downloadAllSurahs()
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = QuranColors.Primary
                        )
                    ) {
                        Text("تحميل الكل", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDownloadDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = QuranColors.TextSecondary
                    )
                ) {
                    Text(
                        if (uiState.isDownloadingAll) "إغلاق" else "إلغاء",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }

    val filteredSurahs = remember(uiState.surahs, searchQuery, selectedFilter) {
        val normalizedQuery = removeArabicDiacritics(searchQuery)
        uiState.surahs.filter { surah ->
            val matchesSearch = searchQuery.isEmpty() ||
                    surah.englishName.contains(searchQuery, ignoreCase = true) ||
                    removeArabicDiacritics(surah.name).contains(normalizedQuery) ||
                    removeArabicDiacritics(surah.englishName).contains(normalizedQuery, ignoreCase = true) ||
                    surah.number.toString() == searchQuery
            val matchesFilter = when (selectedFilter) {
                1 -> surah.revelationType == "Meccan"
                2 -> surah.revelationType == "Medinan"
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuranColors.Background)
    ) {
        // Loading
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = QuranColors.Primary,
                    strokeWidth = 3.dp
                )
            }
        }

        // Error
        uiState.error?.let { error ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.CloudOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = QuranColors.TextTertiary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        color = QuranColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Unified scrollable list - entire screen scrolls as one
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header with search
            item(key = "header") {
                PremiumHeader(
                    searchExpanded = searchExpanded,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onSearchToggle = {
                        searchExpanded = !searchExpanded
                        if (!searchExpanded) searchQuery = ""
                    }
                )
            }

            // Action Bar
            item(key = "action_bar") {
                ActionBar(
                    onAdhkarClick = onAdhkarClick,
                    onDownloadAllClick = { showDownloadDialog = true },
                    onBookmarksClick = onBookmarksClick,
                    onKhatmahClick = onKhatmahClick,
                    onStatsClick = onStatsClick,
                    onNamesOfAllahClick = onNamesOfAllahClick,
                    onSettingsClick = onSettingsClick,
                    onTopicIndexClick = onTopicIndexClick,
                    onPageNavigatorClick = onPageNavigatorClick,
                    onProphetStoriesClick = onProphetStoriesClick,
                    onFiqhClick = onFiqhClick,
                    onMemorizationClick = onMemorizationClick,
                    onHizbClick = onHizbClick,
                    cachedCount = uiState.cachedCount,
                    isDownloadingAll = uiState.isDownloadingAll,
                    downloadProgress = uiState.downloadProgress,
                    downloadTotal = uiState.downloadTotal
                )
            }

            // Filter Chips
            item(key = "filter_chips") {
                PremiumFilterChips(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
            }

            // Continue Reading Card
            lastRead.value?.let { read ->
                item(key = "continue_reading") {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        ContinueReadingCard(
                            lastRead = read,
                            onClick = {
                                onContinueReading(read.surahNumber, read.surahName, read.ayahNumber)
                            }
                        )
                    }
                }
            }

            // Surah Cards
            items(
                items = filteredSurahs,
                key = { it.number }
            ) { surah ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
                    SurahCard(
                        surah = surah,
                        onClick = { onSurahClick(surah.number, surah.name) }
                    )
                }
            }

            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

// ==================== Action Bar ====================
@Composable
private fun ActionBar(
    onAdhkarClick: () -> Unit,
    onDownloadAllClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onKhatmahClick: () -> Unit,
    onStatsClick: () -> Unit,
    onNamesOfAllahClick: () -> Unit = {},
    onSettingsClick: () -> Unit,
    onTopicIndexClick: () -> Unit = {},
    onPageNavigatorClick: () -> Unit = {},
    onProphetStoriesClick: () -> Unit = {},
    onFiqhClick: () -> Unit = {},
    onMemorizationClick: () -> Unit = {},
    onHizbClick: () -> Unit = {},
    cachedCount: Int,
    isDownloadingAll: Boolean,
    downloadProgress: Int,
    downloadTotal: Int
) {
    // Row 1: Main actions
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(QuranColors.Surface)
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // الأذكار
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onAdhkarClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = "الأذكار",
                    tint = QuranColors.GoldDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الأذكار",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // الإشارات المرجعية
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onBookmarksClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = "الإشارات المرجعية",
                    tint = QuranColors.GoldDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الإشارات",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // تحميل
            if (isDownloadingAll) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { downloadProgress.toFloat() / downloadTotal },
                        modifier = Modifier.size(22.dp),
                        color = QuranColors.Primary,
                        strokeWidth = 2.dp,
                        trackColor = QuranColors.PrimarySurface
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text("تحميل...", fontSize = 10.sp, color = QuranColors.TextTertiary)
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onDownloadAllClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (cachedCount >= 114) Icons.Filled.CloudDone else Icons.Filled.CloudDownload,
                        contentDescription = "تحميل",
                        tint = if (cachedCount >= 114) QuranColors.Success else QuranColors.Primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (cachedCount >= 114) "محفوظ" else "تحميل",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = QuranColors.TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

    // Row 2: Secondary actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(QuranColors.Surface)
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // الختمة
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onKhatmahClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MenuBook,
                    contentDescription = "الختمة",
                    tint = QuranColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الختمة",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // الإحصائيات
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onStatsClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.BarChart,
                    contentDescription = "الإحصائيات",
                    tint = QuranColors.GoldDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الإحصائيات",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // أسماء الله الحسنى
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNamesOfAllahClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "أسماء الله الحسنى",
                    tint = QuranColors.GoldDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الحسنى",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // الإعدادات
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSettingsClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "الإعدادات",
                    tint = QuranColors.TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الإعدادات",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 3: Religious & Educational features
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(QuranColors.Surface)
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // فهرس الموضوعات
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onTopicIndexClick() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Topic,
                    contentDescription = "فهرس الموضوعات",
                    tint = QuranColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الموضوعات",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // الأحزاب
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onHizbClick() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatListNumbered,
                    contentDescription = "الأحزاب",
                    tint = QuranColors.GoldDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الأحزاب",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // قصص الأنبياء
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onProphetStoriesClick() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoStories,
                    contentDescription = "قصص الأنبياء",
                    tint = QuranColors.MeccanText,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الأنبياء",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // فقه العبادات
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onFiqhClick() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Gavel,
                    contentDescription = "فقه العبادات",
                    tint = QuranColors.MedinanText,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الفقه",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // الحفظ
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onMemorizationClick() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Psychology,
                    contentDescription = "مركز الحفظ",
                    tint = QuranColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "الحفظ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }

            // الانتقال لصفحة
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onPageNavigatorClick() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FindInPage,
                    contentDescription = "الانتقال لصفحة",
                    tint = QuranColors.GoldDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "صفحة",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranColors.TextSecondary
                )
            }
        }
    }
}

// ==================== Continue Reading Card ====================
@Composable
private fun ContinueReadingCard(
    lastRead: LastRead,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = QuranColors.Primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "متابعة القراءة",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${lastRead.surahName} - الآية ${lastRead.ayahNumber}",
                    fontSize = 13.sp,
                    color = QuranColors.GoldLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (lastRead.ayahText.isNotEmpty()) {
                    Text(
                        text = lastRead.ayahText,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Icon(
                Icons.Filled.ChevronLeft,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

// ==================== Premium Header ====================
@Composable
private fun PremiumHeader(
    searchExpanded: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        QuranColors.GradientStart,
                        QuranColors.GradientEnd,
                        QuranColors.PrimaryLight
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "القرآن الكريم",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranColors.TextOnPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "بسم الله الرحمن الرحيم",
                        fontSize = 14.sp,
                        color = QuranColors.GoldLight,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(
                    onClick = onSearchToggle,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(
                        if (searchExpanded) Icons.Filled.Close else Icons.Filled.Search,
                        contentDescription = "بحث",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = searchExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    placeholder = {
                        Text("ابحث عن سورة...", color = Color.White.copy(alpha = 0.6f))
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = QuranColors.Gold,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = QuranColors.Gold
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("١١٤", "سورة")
                StatItem("٣٠", "جزء")
                StatItem("٦٢٣٦", "آية")
            }
        }
    }
}

@Composable
private fun StatItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(number, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = QuranColors.GoldLight)
        Text(label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
    }
}

// ==================== Filter Chips ====================
@Composable
private fun PremiumFilterChips(
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val icon0 = Icons.Filled.MenuBook
        val icon1 = Icons.Filled.Star
        val icon2 = Icons.Filled.LocationOn
        val filters = listOf("الكل", "مكية", "مدنية")

        filters.forEachIndexed { index, label ->
            val icon = when (index) { 0 -> icon0; 1 -> icon1; else -> icon2 }
            val isSelected = selectedFilter == index

            Surface(
                onClick = { onFilterSelected(index) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) QuranColors.Primary else QuranColors.Surface,
                shadowElevation = if (isSelected) 4.dp else 0.dp,
                border = if (!isSelected) ButtonDefaults.outlinedButtonBorder else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (isSelected) Color.White else QuranColors.TextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else QuranColors.TextSecondary
                    )
                }
            }
        }
    }
}

// ==================== Surah Card ====================
@Composable
private fun SurahCard(
    surah: SurahInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = QuranColors.Primary.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = QuranColors.PrimarySurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        QuranColors.Gold.copy(alpha = 0.4f)
                    )
                ) {}
                Text(
                    text = "${surah.number}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = QuranColors.Primary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.englishName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = QuranColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (surah.revelationType == "Meccan")
                            QuranColors.MeccanBadge.copy(alpha = 0.4f)
                        else QuranColors.MedinanBadge.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = if (surah.revelationType == "Meccan") "مكية" else "مدنية",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (surah.revelationType == "Meccan")
                                QuranColors.MeccanText else QuranColors.MedinanText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${surah.numberOfAyahs} آية",
                        fontSize = 12.sp,
                        color = QuranColors.TextTertiary
                    )
                }
            }

            Text(
                text = surah.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = QuranColors.Primary,
                textAlign = TextAlign.End
            )
        }
    }
}

// ==================== Arabic Diacritics Removal ====================
/**
 * Removes Arabic diacritics (tashkeel) and normalizes Arabic text for search comparison.
 * This allows searching for "الفاتحة" and matching "الفَاتِحَةِ".
 */
fun removeArabicDiacritics(text: String): String {
    val diacritics = charArrayOf(
        '\u064B', // Fathatan
        '\u064C', // Dammatan
        '\u064D', // Kasratan
        '\u064E', // Fatha
        '\u064F', // Damma
        '\u0650', // Kasra
        '\u0651', // Shadda
        '\u0652', // Sukun
        '\u0653', // Maddah
        '\u0654', // Hamza Above
        '\u0655', // Hamza Below
        '\u0670', // Dagger Alif
        '\u0610', // Sajdah
        '\u0611',
        '\u0612',
        '\u0613',
        '\u0614',
        '\u0615',
        '\u0616',
        '\u0617',
        '\u0618',
        '\u0619',
        '\u061A',
        '\u06D6', // Small High Sad
        '\u06D7',
        '\u06D8',
        '\u06D9',
        '\u06DA',
        '\u06DB',
        '\u06DC',
        '\u06DF',
        '\u06E0',
        '\u06E1',
        '\u06E2',
        '\u06E3',
        '\u06E4',
        '\u06E5',
        '\u06E6',
        '\u06E7',
        '\u06E8',
        '\u06E9',
        '\u06EA',
        '\u06EB',
        '\u06EC',
        '\u06ED',
        '\u0640'  // Tatweel (kashida)
    )
    var result = text
    for (c in diacritics) {
        result = result.replace(c.toString(), "")
    }
    // Normalize Alef variants
    result = result.replace('\u0622', '\u0627') // Alef with Madda -> Alef
    result = result.replace('\u0623', '\u0627') // Alef with Hamza Above -> Alef
    result = result.replace('\u0625', '\u0627') // Alef with Hamza Below -> Alef
    result = result.replace('\u0671', '\u0627') // Alef Wasla -> Alef
    // Normalize Ta Marbuta to Ha
    result = result.replace('\u0629', '\u0647')
    // Normalize Alef Maqsura to Ya
    result = result.replace('\u0649', '\u064A')
    return result
}
