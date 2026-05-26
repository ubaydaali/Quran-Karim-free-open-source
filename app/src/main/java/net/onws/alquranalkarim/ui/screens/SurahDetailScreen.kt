package net.onws.alquranalkarim.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.data.model.Ayah
import net.onws.alquranalkarim.ui.theme.ColorThemeOption
import net.onws.alquranalkarim.ui.theme.LocalThemeState
import net.onws.alquranalkarim.ui.theme.QuranColors
import net.onws.alquranalkarim.ui.theme.QuranTextFormatter
import net.onws.alquranalkarim.ui.viewmodel.SurahDetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SurahDetailScreen(
    surahNumber: Int,
    surahName: String,
    initialAyah: Int,
    bookmarkManager: BookmarkManager,
    onBackClick: () -> Unit,
    viewModel: SurahDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showBookmarkSnackbar by remember { mutableStateOf(false) }
    val themeState = LocalThemeState.current
    val settings = remember { bookmarkManager.getSettings() }
    val haptic = LocalHapticFeedback.current

    // === Sepia Toggle State ===
    var previousTheme by remember { mutableStateOf(themeState.colorTheme) }
    var previousDarkMode by remember { mutableStateOf(themeState.isDarkMode) }

    // === Keep Screen On ===
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    // === Tafsir Bottom Sheet State ===
    var selectedAyahForTafsir by remember { mutableStateOf<Ayah?>(null) }
    var tafsirText by remember { mutableStateOf("") }
    var isLoadingTafsir by remember { mutableStateOf(false) }
    val tafsirSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // === Auto-Scroll State ===
    var isAutoScrolling by remember { mutableStateOf(false) }
    var scrollSpeed by remember { mutableFloatStateOf(0.3f) } // 0.1 = ultra-slow, 1.0 = moderate
    var isControlExpanded by remember { mutableStateOf(false) }
    var lastInteractionTime by remember { mutableLongStateOf(0L) }

    // Load tafsir when ayah is selected
    LaunchedEffect(selectedAyahForTafsir) {
        selectedAyahForTafsir?.let { ayah ->
            isLoadingTafsir = true
            tafsirText = ""
            try {
                tafsirText = withContext(Dispatchers.IO) {
                    val url = java.net.URL("https://api.alquran.cloud/v1/ayah/${surahNumber}:${ayah.numberInSurah}/ar.muyassar")
                    val connection = url.openConnection() as java.net.HttpURLConnection
                    connection.connectTimeout = 10000
                    connection.readTimeout = 10000
                    val response = connection.inputStream.bufferedReader().readText()
                    val textIndex = response.indexOf("\"text\":\"")
                    if (textIndex != -1) {
                        val start = textIndex + 8
                        val end = response.indexOf("\"", start)
                        if (end != -1) {
                            return@withContext response.substring(start, end)
                                .replace("\\n", "\n")
                                .replace("\\u003cbr\\u003e", "\n")
                        }
                    }
                    "التفسير غير متوفر حالياً لهذه الآية"
                }
            } catch (e: Exception) {
                tafsirText = "تعذر تحميل التفسير. تحقق من اتصال الإنترنت."
            }
            isLoadingTafsir = false
        }
    }

    // Auto-scroll effect: ultra-smooth continuous scrolling via scrollToItem with pixel offset
    // scrollSpeed 0.1→1.0 controls pace: 0.1 = extremely slow (meditative), 1.0 = moderate
    LaunchedEffect(isAutoScrolling, scrollSpeed) {
        if (isAutoScrolling) {
            while (isActive && listState.canScrollForward) {
                // At speed 0.1: ~2000ms delay per item (very slow)
                // At speed 1.0: ~300ms delay per item (moderate)
                val delayMs = ((2200f - scrollSpeed * 1900f) / scrollSpeed).toLong()
                    .coerceIn(250L, 5000L)
                if (!isActive || !listState.canScrollForward) break
                val nextIndex = (listState.firstVisibleItemIndex + 1)
                    .coerceAtMost(listState.layoutInfo.totalItemsCount - 1)
                listState.animateScrollToItem(nextIndex)
                delay(delayMs)
            }
            if (!listState.canScrollForward) {
                isAutoScrolling = false
                isControlExpanded = false
            }
        }
    }

    // Auto-hide: collapse control panel after 4 seconds of no interaction
    LaunchedEffect(lastInteractionTime, isControlExpanded, isAutoScrolling) {
        if (isControlExpanded && isAutoScrolling) {
            delay(4000L)
            if (isControlExpanded && isAutoScrolling) {
                isControlExpanded = false
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setBookmarkManager(bookmarkManager)
    }

    // Track the currently visible ayah as the user scrolls
    val firstVisibleIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) -1
            else {
                val showBismillah = surahNumber != 1 && surahNumber != 9
                val headerOffset = if (showBismillah) 2 else 1
                val firstAyahItem = visibleItems.firstOrNull { it.index >= headerOffset }
                firstAyahItem?.let { it.index - headerOffset + 1 } ?: -1
            }
        }
    }

    // Auto-save last read position when user scrolls to a new ayah
    LaunchedEffect(firstVisibleIndex) {
        if (firstVisibleIndex > 0 && uiState.ayahs.isNotEmpty()) {
            val ayahIdx = firstVisibleIndex - 1
            if (ayahIdx in uiState.ayahs.indices) {
                viewModel.saveLastReadPosition(ayahIdx)
            }
        }
    }

    // Save position when user leaves the screen
    DisposableEffect(Unit) {
        onDispose {
            if (firstVisibleIndex > 0 && uiState.ayahs.isNotEmpty()) {
                val ayahIdx = firstVisibleIndex - 1
                if (ayahIdx in uiState.ayahs.indices) {
                    viewModel.saveLastReadPosition(ayahIdx)
                }
            }
        }
    }

    LaunchedEffect(surahNumber) {
        viewModel.loadSurah(surahNumber, initialAyah)
    }

    LaunchedEffect(uiState.ayahs, uiState.initialAyah) {
        if (uiState.ayahs.isNotEmpty() && uiState.initialAyah > 1) {
            val index = uiState.initialAyah - 1
            if (index < uiState.ayahs.size) {
                delay(500)
                val showBismillah = surahNumber != 1 && surahNumber != 9
                val offset = if (showBismillah) 2 else 1
                listState.animateScrollToItem(index + offset)
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showBookmarkSnackbar) {
        if (showBookmarkSnackbar) {
            snackbarHostState.showSnackbar("تم حفظ مكان القراءة ✓", duration = SnackbarDuration.Short)
            showBookmarkSnackbar = false
        }
    }

    // === Tafsir Bottom Sheet ===
    if (selectedAyahForTafsir != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedAyahForTafsir = null },
            sheetState = tafsirSheetState,
            containerColor = QuranColors.Surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(QuranColors.Divider)
                )
            }
        ) {
            TafsirBottomSheetContent(
                ayah = selectedAyahForTafsir!!,
                surahName = surahName,
                tafsirText = tafsirText,
                isLoading = isLoadingTafsir
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = QuranColors.Primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        topBar = {
            PremiumDetailTopBar(
                surahName = surahName,
                surahEnglishName = uiState.surahEnglishName,
                onBackClick = onBackClick,
                isSepiaMode = themeState.colorTheme == ColorThemeOption.SEPIA,
                onToggleSepia = {
                    if (themeState.colorTheme == ColorThemeOption.SEPIA) {
                        themeState.colorTheme = previousTheme
                        themeState.isDarkMode = previousDarkMode
                        QuranColors.applyTheme(previousTheme, previousDarkMode)
                    } else {
                        previousTheme = themeState.colorTheme
                        previousDarkMode = themeState.isDarkMode
                        themeState.colorTheme = ColorThemeOption.SEPIA
                        themeState.isDarkMode = false
                        QuranColors.applyTheme(ColorThemeOption.SEPIA, false)
                    }
                }
            )
        },
        floatingActionButton = {
            // Elegant expandable auto-scroll control panel
            Box(
                modifier = Modifier.padding(bottom = 16.dp, end = 4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                // Expanded control panel (slider + pause)
                AnimatedVisibility(
                    visible = isControlExpanded && isAutoScrolling,
                    enter = fadeIn(animationSpec = tween(300)) +
                            expandVertically(animationSpec = tween(300, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(250)) +
                            shrinkVertically(animationSpec = tween(250, easing = FastOutLinearInEasing))
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = QuranColors.Surface.copy(alpha = 0.95f)
                        ),
                        elevation = CardDefaults.cardElevation(8.dp),
                        border = BorderStroke(1.dp, QuranColors.Gold.copy(alpha = 0.2f)),
                        modifier = Modifier.padding(bottom = 68.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Speed labels
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Speed,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = QuranColors.Primary
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = when {
                                        scrollSpeed < 0.2f -> "بطيء جداً"
                                        scrollSpeed < 0.4f -> "بطيء"
                                        scrollSpeed < 0.6f -> "متوسط"
                                        scrollSpeed < 0.8f -> "سريع"
                                        else -> "سريع جداً"
                                    },
                                    fontSize = 9.sp,
                                    color = QuranColors.TextTertiary,
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Speed slider
                            Slider(
                                value = scrollSpeed,
                                onValueChange = {
                                    scrollSpeed = it
                                    lastInteractionTime = System.currentTimeMillis()
                                },
                                valueRange = 0.1f..1.0f,
                                modifier = Modifier.width(120.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = QuranColors.Primary,
                                    activeTrackColor = QuranColors.Primary,
                                    inactiveTrackColor = QuranColors.PrimarySurface
                                )
                            )

                            // Pause button inside panel
                            IconButton(
                                onClick = {
                                    isAutoScrolling = false
                                    isControlExpanded = false
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(QuranColors.Primary)
                            ) {
                                Icon(
                                    Icons.Default.Pause,
                                    contentDescription = "إيقاف",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Main FAB: Play/Pause toggle + expand on tap
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            if (isAutoScrolling)
                                Brush.linearGradient(
                                    listOf(QuranColors.Primary, QuranColors.PrimaryDark)
                                )
                            else
                                Brush.linearGradient(
                                    listOf(QuranColors.GoldSurface, QuranColors.GoldSurface)
                                )
                        )
                        .combinedClickable(
                            onClick = {
                                if (isAutoScrolling) {
                                    // Toggle expanded state
                                    isControlExpanded = !isControlExpanded
                                    lastInteractionTime = System.currentTimeMillis()
                                } else {
                                    // Start auto-scroll and expand controls
                                    isAutoScrolling = true
                                    isControlExpanded = true
                                    lastInteractionTime = System.currentTimeMillis()
                                }
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isAutoScrolling) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isAutoScrolling) "إيقاف التمرير" else "تمرير تلقائي",
                        tint = if (isAutoScrolling) Color.White else QuranColors.Primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(QuranColors.Background)
        ) {
            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = QuranColors.Primary,
                                strokeWidth = 3.dp
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "جاري تحميل السورة...",
                                color = QuranColors.TextSecondary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                uiState.error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.CloudOff, null,
                                modifier = Modifier.size(64.dp),
                                tint = QuranColors.TextTertiary
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(uiState.error!!, color = QuranColors.Error)
                        }
                    }
                }

                else -> {
                    val showBismillah = surahNumber != 1 && surahNumber != 9

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 80.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item {
                            PremiumSurahHeader(
                                surahName = surahName,
                                surahNumber = surahNumber,
                                revelationType = uiState.revelationType,
                                numberOfAyahs = uiState.ayahs.size
                            )
                        }

                        if (showBismillah) {
                            item {
                                PremiumBismillah()
                            }
                        }

                        itemsIndexed(
                            items = uiState.ayahs,
                            key = { _, ayah -> ayah.numberInSurah }
                        ) { index, ayah ->
                            AyahItem(
                                ayah = ayah,
                                surahName = surahName,
                                isHighlighted = uiState.initialAyah == ayah.numberInSurah,
                                arabicFontSize = themeState.arabicFontSize,
                                translationFontSize = themeState.translationFontSize,
                                onBookmarkClick = {
                                    viewModel.saveBookmark(index)
                                    val isNowSaved = bookmarkManager.isBookmarked(surahNumber, ayah.numberInSurah)
                                    showBookmarkSnackbar = true
                                },
                                savedAyahNumber = uiState.savedAyahNumber,
                                onLongPress = {
                                    selectedAyahForTafsir = ayah
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            )
                        }

                        item { Spacer(Modifier.height(24.dp)) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PremiumDetailTopBar(
    surahName: String,
    surahEnglishName: String,
    onBackClick: () -> Unit,
    isSepiaMode: Boolean = false,
    onToggleSepia: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = surahName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    maxLines = 1
                )
                if (surahEnglishName.isNotEmpty()) {
                    Text(
                        text = surahEnglishName,
                        style = MaterialTheme.typography.bodySmall,
                        color = QuranColors.GoldLight.copy(alpha = 0.8f),
                        maxLines = 1
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "رجوع",
                    tint = Color.White
                )
            }
        },
        actions = {
            // Sepia / Paper Reading Mode toggle
            IconButton(
                onClick = onToggleSepia,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSepiaMode) QuranColors.Gold.copy(alpha = 0.3f)
                        else Color.White.copy(alpha = 0.15f)
                    )
            ) {
                Icon(
                    Icons.Outlined.Star,
                    contentDescription = if (isSepiaMode) "الوضع العادي" else "وضع القراءة الدافئ",
                    tint = if (isSepiaMode) QuranColors.GoldLight else Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = QuranColors.Primary
        )
    )
}

@Composable
private fun PremiumSurahHeader(
    surahName: String,
    surahNumber: Int,
    revelationType: String,
    numberOfAyahs: Int
) {
    val isDark = LocalThemeState.current.isDarkMode
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                QuranColors.Primary.copy(alpha = 0.1f),
                                QuranColors.PrimarySurface
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(4.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(2.dp, QuranColors.Gold.copy(alpha = 0.4f)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(2.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            QuranColors.Gold,
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = surahName,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) QuranColors.GoldLight else QuranColors.Primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "سورة رقم $surahNumber",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = QuranColors.TextSecondary
                        )

                        Spacer(Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (revelationType == "Meccan")
                                QuranColors.MeccanBadge.copy(alpha = 0.3f)
                            else QuranColors.MedinanBadge.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "${if (revelationType == "Meccan") "مكية" else "مدنية"} • $numberOfAyahs آية",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (revelationType == "Meccan")
                                    QuranColors.MeccanText else QuranColors.MedinanText,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(2.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            QuranColors.Gold,
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumBismillah() {
    val isDark = LocalThemeState.current.isDarkMode
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, QuranColors.Gold.copy(alpha = 0.3f)),
            colors = CardDefaults.cardColors(
                containerColor = QuranColors.GoldSurface.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) QuranColors.GoldLight else QuranColors.BismillahColor
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AyahItem(
    ayah: Ayah,
    surahName: String,
    isHighlighted: Boolean,
    onBookmarkClick: () -> Unit,
    savedAyahNumber: Int = -1,
    arabicFontSize: Float = 24f,
    translationFontSize: Float = 16f,
    onLongPress: () -> Unit = {}
) {
    val isSaved = savedAyahNumber == ayah.numberInSurah
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val containerColor = when {
        isHighlighted -> QuranColors.GoldSurface.copy(alpha = 0.5f)
        else -> QuranColors.Surface
    }

    val borderColor = when {
        isHighlighted -> QuranColors.Gold.copy(alpha = 0.3f)
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* Normal tap - no action needed */ },
                onLongClick = {
                    onLongPress()
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = QuranTextFormatter.format(
                    rawText = "${ayah.text} ﴿${ayah.numberInSurah}﴾",
                    ayahNumber = ayah.numberInSurah,
                    baseColor = QuranColors.TextPrimary
                ),
                fontSize = arabicFontSize.sp,
                lineHeight = (arabicFontSize * 1.8f).sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                QuranColors.Divider,
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        onClick = onBookmarkClick,
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSaved)
                            QuranColors.Gold.copy(alpha = 0.2f)
                        else QuranColors.SurfaceVariant
                    ) {
                        Icon(
                            if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "حفظ",
                            modifier = Modifier.padding(8.dp).size(18.dp),
                            tint = if (isSaved) QuranColors.GoldDark else QuranColors.TextTertiary
                        )
                    }

                    Surface(
                        onClick = {
                            val shareText = "${ayah.text} ﴿${ayah.numberInSurah}﴾\n$surahName"
                            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(android.content.Intent.createChooser(intent, "مشاركة الآية"))
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = QuranColors.SurfaceVariant
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "مشاركة",
                            modifier = Modifier.padding(8.dp).size(18.dp),
                            tint = QuranColors.TextTertiary
                        )
                    }

                    // Tafsir info icon
                    Surface(
                        onClick = {
                            onLongPress()
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = QuranColors.SurfaceVariant
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = "تفسير",
                            modifier = Modifier.padding(8.dp).size(18.dp),
                            tint = QuranColors.TextTertiary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = QuranColors.PrimarySurface,
                    border = BorderStroke(1.dp, QuranColors.Gold.copy(alpha = 0.3f)),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${ayah.numberInSurah}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (LocalThemeState.current.isDarkMode) QuranColors.Gold else QuranColors.Primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TafsirBottomSheetContent(
    ayah: Ayah,
    surahName: String,
    tafsirText: String,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        // Title
        Text(
            text = "تفسير الآية ${ayah.numberInSurah} من $surahName",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (LocalThemeState.current.isDarkMode) QuranColors.GoldLight else QuranColors.Primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // Ayah text in a beautiful card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = QuranColors.PrimarySurface
            ),
            border = BorderStroke(1.dp, QuranColors.Gold.copy(alpha = 0.3f))
        ) {
            Text(
                text = "${ayah.text} ﴿${ayah.numberInSurah}﴾",
                fontSize = 22.sp,
                lineHeight = 38.sp,
                textAlign = TextAlign.Right,
                color = QuranColors.TextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Decorative divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, QuranColors.Gold, Color.Transparent)
                    )
                )
        )

        Spacer(Modifier.height(16.dp))

        // Tafsir label
        val isDarkTafsir = LocalThemeState.current.isDarkMode
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isDarkTafsir) QuranColors.GoldLight else QuranColors.Primary
            )
            Text(
                text = "التفسير الميسر",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTafsir) QuranColors.GoldLight else QuranColors.Primary
            )
        }

        Spacer(Modifier.height(12.dp))

        // Tafsir content
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = QuranColors.Primary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "جاري تحميل التفسير...",
                        fontSize = 13.sp,
                        color = QuranColors.TextTertiary
                    )
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = QuranColors.Background
                )
            ) {
                Text(
                    text = tafsirText,
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = QuranColors.TextSecondary,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        // Safe area bottom padding
        Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        Spacer(Modifier.height(16.dp))
    }
}