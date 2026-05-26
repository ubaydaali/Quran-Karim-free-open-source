package net.onws.alquranalkarim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.data.local.QuranCacheManager
import net.onws.alquranalkarim.data.local.QuranPageMapper
import net.onws.alquranalkarim.notification.NotificationHelper
import net.onws.alquranalkarim.ui.screens.*
import net.onws.alquranalkarim.ui.theme.*
import net.onws.alquranalkarim.ui.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Create notification channel
        NotificationHelper.createNotificationChannel(this)
        val bookmarkManager = BookmarkManager(applicationContext)
        val cacheManager = QuranCacheManager(applicationContext)
        setContent {
            // Initialize theme state from saved settings
            val themeState = remember {
                val settings = bookmarkManager.getSettings()
                ThemeState().apply {
                    applySettings(
                        isDark = settings.isDarkMode,
                        theme = ColorThemeOption.entries.getOrElse(settings.colorTheme) { ColorThemeOption.GREEN },
                        arFont = settings.arabicFontSize,
                        trFont = settings.translationFontSize
                    )
                    // If no user-chosen theme preference, enable auto-time theming on first launch
                    if (settings.colorTheme == 0) {
                        setAutoTimeTheme(true)
                    }
                }
            }

            QuranTheme(themeState = themeState) {
                QuranApp(bookmarkManager, cacheManager, themeState)
            }
        }
    }
}

data class StartupDhikr(val text: String, val description: String)

val startupAdhkar = listOf(
    StartupDhikr("أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ", "أذكار الصباح"),
    StartupDhikr("أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ", "أذكار المساء"),
    StartupDhikr("أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", "من أذكار الاستغفار"),
    StartupDhikr("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ", "كلمتان حبيبتان إلى الرحمن"),
    StartupDhikr("لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", "من أذكار الصباح"),
    StartupDhikr("اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالآخِرَةِ", "من دعاء النبي ﷺ"),
    StartupDhikr("يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ", "من أذكار الهم والحزن"),
    StartupDhikr("أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ", "من أذكار المساء"),
    StartupDhikr("اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ", "من الصلاة على النبي ﷺ"),
    StartupDhikr("حَسْبِيَ اللَّهُ لاَ إِلَٰهَ إِلاَّ هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ", "من أذكار الصباح"),
    StartupDhikr("بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ", "من أذكار الصباح"),
    StartupDhikr("رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ", "من الاستغفار"),
    StartupDhikr("اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ", "من أذكار الاستعاذة"),
    StartupDhikr("لاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ", "كنز من كنوز الجنة"),
    StartupDhikr("اللَّهُمَّ بَارِكْ لَنَا فِيمَا رَزَقْتَنَا وَقِنَا عَذَابَ النَّارِ", "من أذكار الطعام"),
    StartupDhikr("يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ", "من دعاء النبي ﷺ"),
    StartupDhikr("اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ", "من أذكار بعد الصلاة"),
    StartupDhikr("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",  "كلمة طيبة"),
    StartupDhikr("اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي", "من أذكار الصباح"),
    StartupDhikr("أَسْأَلُكَ نَعِيمًا لاَ يَنْفَدُ، وَمُرَافَقَةَ نَبِيِّكَ مُحَمَّدٍ ﷺ", "من أجمل الدعاء")
)

@Composable
fun StartupDhikrDialog(onDismiss: () -> Unit) {
    val dhikr = remember { startupAdhkar.random() }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = QuranColors.PrimarySurface,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = QuranColors.Primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        title = {
            Text(
                "ذكر اليوم",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = QuranColors.TextPrimary
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dhikr.text,
                    fontSize = 20.sp,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Center,
                    color = QuranColors.TextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = QuranColors.GoldSurface
                ) {
                    Text(
                        text = dhikr.description,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = QuranColors.GoldDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = QuranColors.Primary
                )
            ) {
                Text("حسناً", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = QuranColors.Surface
    )
}

@Composable
fun QuranApp(bookmarkManager: BookmarkManager, cacheManager: QuranCacheManager, themeState: ThemeState) {
    val navController = rememberNavController()
    var showStartupDhikr by remember { mutableStateOf(true) }

    // Startup dhikr dialog
    if (showStartupDhikr) {
        StartupDhikrDialog(
            onDismiss = { showStartupDhikr = false }
        )
    }

    NavHost(
        navController = navController,
        startDestination = "surah_list"
    ) {
        // Surah List Screen
        composable("surah_list") {
            val viewModel: SurahListViewModel = viewModel()

            viewModel.setCacheManager(cacheManager)
            SurahListScreen(
                viewModel = viewModel,
                bookmarkManager = bookmarkManager,
                onSurahClick = { surahNumber, surahName ->
                    // Check if there's a saved last read position for this surah
                    val lastRead = bookmarkManager.getLastRead()
                    val savedAyah = if (lastRead != null && lastRead.surahNumber == surahNumber) {
                        lastRead.ayahNumber
                    } else {
                        // Check bookmarks for this surah
                        val bookmark = bookmarkManager.getBookmarks().firstOrNull { it.surahNumber == surahNumber }
                        bookmark?.ayahNumber ?: 1
                    }
                    navController.navigate("surah_detail/$surahNumber/$surahName/$savedAyah")
                },
                onContinueReading = { surahNumber, surahName, ayahNumber ->
                    navController.navigate("surah_detail/$surahNumber/$surahName/$ayahNumber")
                },
                onAdhkarClick = {
                    navController.navigate("adhkar")
                },
                onBookmarksClick = {
                    navController.navigate("bookmarks")
                },
                onKhatmahClick = {
                    navController.navigate("khatmah")
                },
                onStatsClick = {
                    navController.navigate("stats")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                },
                onNamesOfAllahClick = {
                    navController.navigate("names_of_allah")
                },
                onDuaQunutClick = {
                    navController.navigate("dua_qunut")
                },
                onAchievementsClick = {
                    navController.navigate("achievements")
                },
                onTopicIndexClick = {
                    navController.navigate("topic_index")
                },
                onPageNavigatorClick = {
                    navController.navigate("page_navigator")
                },
                onProphetStoriesClick = {
                    navController.navigate("prophet_stories")
                },
                onFiqhClick = {
                    navController.navigate("fiqh")
                },
                onMemorizationClick = {
                    navController.navigate("memorization_hub")
                },
                onHizbClick = {
                    navController.navigate("hizb")
                }
            )
        }

        // Surah Detail Screen
        composable(
            route = "surah_detail/{surahNumber}/{surahName}/{initialAyah}",
            arguments = listOf(
                navArgument("surahNumber") { type = NavType.IntType },
                navArgument("surahName") { type = NavType.StringType },
                navArgument("initialAyah") { type = NavType.IntType; defaultValue = 1 }
            )
        ) { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
            val surahName = backStackEntry.arguments?.getString("surahName") ?: ""
            val initialAyah = backStackEntry.arguments?.getInt("initialAyah") ?: 1
            val viewModel: SurahDetailViewModel = viewModel()
            viewModel.setCacheManager(cacheManager)

            SurahDetailScreen(
                surahNumber = surahNumber,
                surahName = surahName,
                initialAyah = initialAyah,
                bookmarkManager = bookmarkManager,
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        // Adhkar Screen
        composable(
            route = "adhkar",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            AdhkarScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Bookmarks Screen
        composable(
            route = "bookmarks",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            BookmarksScreen(
                bookmarkManager = bookmarkManager,
                onBackClick = { navController.popBackStack() },
                onBookmarkClick = { surahNumber, surahName, ayahNumber ->
                    navController.navigate("surah_detail/$surahNumber/$surahName/$ayahNumber")
                }
            )
        }

        // Khatmah Screen
        composable(
            route = "khatmah",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            KhatmahScreen(
                bookmarkManager = bookmarkManager,
                onBackClick = { navController.popBackStack() },
                onNavigateToRead = { surahNumber, surahName, ayahNumber ->
                    navController.navigate("surah_detail/$surahNumber/$surahName/$ayahNumber")
                }
            )
        }

        // Reading Stats Screen
        composable(
            route = "stats",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            StatsScreen(
                bookmarkManager = bookmarkManager,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Settings Screen
        composable(
            route = "settings",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            SettingsScreen(
                bookmarkManager = bookmarkManager,
                themeState = themeState,
                onBackClick = { navController.popBackStack() },
                onAboutClick = { navController.navigate("about") }
            )
        }

        // About Screen
        composable(
            route = "about",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Names of Allah Screen
        composable(
            route = "names_of_allah",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            NamesOfAllahScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Dua Qunut Screen
        composable(
            route = "dua_qunut",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            DuaQunutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Achievements Screen
        composable(
            route = "achievements",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            AchievementsScreen(
                bookmarkManager = bookmarkManager,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Topic Index Screen
        composable(
            route = "topic_index",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            TopicIndexScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToAyah = { surahNumber, surahName, ayahNumber ->
                    navController.navigate("surah_detail/$surahNumber/$surahName/$ayahNumber")
                }
            )
        }

        // Page Navigator Screen
        composable(
            route = "page_navigator",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            PageNavigatorScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToPage = { page ->
                    val pageInfo = QuranPageMapper.getPageStartingAyah(page)
                    navController.navigate("surah_detail/${pageInfo.first}/${pageInfo.second}/${pageInfo.third}")
                }
            )
        }

        // Prophet Stories Screen
        composable(
            route = "prophet_stories",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            ProphetStoriesScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToAyah = { surahNumber, surahName, ayahNumber ->
                    navController.navigate("surah_detail/$surahNumber/$surahName/$ayahNumber")
                }
            )
        }

        // Fiqh Screen
        composable(
            route = "fiqh",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            FiqhScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Memorization Hub Screen
        composable(
            route = "memorization_hub",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            MemorizationHubScreen(
                onBackClick = { navController.popBackStack() },
                onPlanClick = { navController.navigate("memorization_plan") },
                onTestClick = { navController.navigate("memorization_test") },
                onReviewClick = { navController.navigate("memorization_review") },
                onProgressClick = { navController.navigate("memorization_progress") }
            )
        }

        // Memorization Plan Screen
        composable(
            route = "memorization_plan",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            MemorizationPlanScreen(
                onBackClick = { navController.popBackStack() },
                bookmarkManager = bookmarkManager,
                onNavigateToAyah = { surahNumber, surahName, ayahNumber ->
                    navController.navigate("surah_detail/$surahNumber/$surahName/$ayahNumber")
                }
            )
        }

        // Memorization Test Screen
        composable(
            route = "memorization_test",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            MemorizationTestScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Memorization Review (reuse progress for now)
        composable(
            route = "memorization_review",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            MemorizationProgressScreen(
                onBackClick = { navController.popBackStack() },
                bookmarkManager = bookmarkManager
            )
        }

        // Memorization Progress Screen
        composable(
            route = "memorization_progress",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            MemorizationProgressScreen(
                onBackClick = { navController.popBackStack() },
                bookmarkManager = bookmarkManager
            )
        }

        // Hizb Screen
        composable(
            route = "hizb",
            enterTransition = { null },
            exitTransition = { null },
            popEnterTransition = { null },
            popExitTransition = { null }
        ) {
            HizbScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToAyah = { surahNumber, surahName, ayahNumber ->
                    navController.navigate("surah_detail/$surahNumber/$surahName/$ayahNumber")
                }
            )
        }
    }
}
