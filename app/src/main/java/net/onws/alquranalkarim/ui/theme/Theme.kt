package net.onws.alquranalkarim.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import java.util.Calendar

// ==================== Color Theme Definitions ====================
enum class ColorThemeOption { GREEN, BLUE, BROWN, PURPLE, RED, TEAL, GOLD_THEME, NAVY, SEPIA }

// ==================== Premium Islamic Color Palette (Dynamic) ====================
object QuranColors {
    // These will be set dynamically based on theme
    var Primary = Color(0xFF0D5E3B)
        private set
    var PrimaryLight = Color(0xFF1B7A50)
        private set
    var PrimaryDark = Color(0xFF094A2E)
        private set
    var PrimarySurface = Color(0xFFE8F5EE)
        private set

    // Gold Accent
    val Gold = Color(0xFFC8A951)
    val GoldLight = Color(0xFFE8D48B)
    val GoldDark = Color(0xFF9E832E)
    val GoldSurface = Color(0xFFFFF8E1)

    // Background - Light Theme
    var Background = Color(0xFFFBF7F0)
        private set
    var BackgroundDark = Color(0xFFF0EDE4)
        private set
    var Surface = Color(0xFFFFFFFF)
        private set
    var SurfaceVariant = Color(0xFFF5F1EA)
        private set

    // Text Colors
    var TextPrimary = Color(0xFF1A1A1A)
        private set
    var TextSecondary = Color(0xFF5A5A5A)
        private set
    var TextTertiary = Color(0xFF8A8A8A)
        private set
    val TextOnPrimary = Color(0xFFFFFFFF)
    val TextOnGold = Color(0xFF3D2E00)

    // Surah Type Colors
    val MeccanBadge = Color(0xFFE8D48B)
    val MeccanText = Color(0xFF8B6914)
    val MedinanBadge = Color(0xFFB8E6CE)
    var MedinanText = Color(0xFF0D5E3B)
        private set

    // Audio Player
    var PlayerBackground = Color(0xFF0D3D2B)
        private set
    val PlayerProgress = Color(0xFFC8A951)
    var PlayerBuffered = Color(0xFF2D6B4F)
        private set

    // Dhikr Counter
    var DhikrButton = Color(0xFF1B7A50)
        private set
    var DhikrButtonPressed = Color(0xFF094A2E)
        private set
    val DhikrRing = Color(0xFFC8A951)

    // Gradient Colors
    var GradientStart = Color(0xFF0D5E3B)
        private set
    var GradientEnd = Color(0xFF1B8A5A)
        private set

    // Divider
    var Divider = Color(0xFFE0DDD6)
        private set

    // Error
    val Error = Color(0xFFBA1A1A)

    // Success
    val Success = Color(0xFF2E7D32)

    // Bismillah decoration
    var BismillahColor = Color(0xFF0D5E3B)
        private set
    val DecorativeBorder = Color(0xFFC8A951)

    // ===== Dark Mode Colors =====
    val DarkBackground = Color(0xFF0D1117)
    val DarkSurface = Color(0xFF161B22)
    val DarkSurfaceVariant = Color(0xFF21262D)
    val DarkTextPrimary = Color(0xFFE6EDF3)
    val DarkTextSecondary = Color(0xFF8B949E)
    val DarkTextTertiary = Color(0xFF6E7681)
    val DarkDivider = Color(0xFF30363D)
    val DarkPlayerBackground = Color(0xFF0D1117)
    val DarkPlayerBuffered = Color(0xFF1B7A50)
    val DarkMedinanBadge = Color(0xFFB8E6CE)
    val DarkMedinanText = Color(0xFF3FB950)

    // ===== Theme-specific primary colors =====
    private val GreenPrimary = Color(0xFF0D5E3B)
    private val GreenPrimaryLight = Color(0xFF1B7A50)
    private val GreenPrimaryDark = Color(0xFF094A2E)
    private val GreenPrimarySurface = Color(0xFFE8F5EE)
    private val GreenPlayerBackground = Color(0xFF0D3D2B)
    private val GreenPlayerBuffered = Color(0xFF2D6B4F)
    private val GreenGradientStart = Color(0xFF0D5E3B)
    private val GreenGradientEnd = Color(0xFF1B8A5A)

    private val BluePrimary = Color(0xFF1565C0)
    private val BluePrimaryLight = Color(0xFF1E88E5)
    private val BluePrimaryDark = Color(0xFF0D47A1)
    private val BluePrimarySurface = Color(0xFFE3F2FD)
    private val BluePlayerBackground = Color(0xFF0D2C5C)
    private val BluePlayerBuffered = Color(0xFF1565C0)
    private val BlueGradientStart = Color(0xFF1565C0)
    private val BlueGradientEnd = Color(0xFF1E88E5)

    private val BrownPrimary = Color(0xFF6D4C41)
    private val BrownPrimaryLight = Color(0xFF8D6E63)
    private val BrownPrimaryDark = Color(0xFF4E342E)
    private val BrownPrimarySurface = Color(0xFFEFEBE9)
    private val BrownPlayerBackground = Color(0xFF3E2723)
    private val BrownPlayerBuffered = Color(0xFF6D4C41)
    private val BrownGradientStart = Color(0xFF6D4C41)
    private val BrownGradientEnd = Color(0xFF8D6E63)

    private val PurplePrimary = Color(0xFF6A1B9A)
    private val PurplePrimaryLight = Color(0xFF8E24AA)
    private val PurplePrimaryDark = Color(0xFF4A148C)
    private val PurplePrimarySurface = Color(0xFFF3E5F5)
    private val PurplePlayerBackground = Color(0xFF2A0845)
    private val PurplePlayerBuffered = Color(0xFF6A1B9A)
    private val PurpleGradientStart = Color(0xFF6A1B9A)
    private val PurpleGradientEnd = Color(0xFF8E24AA)

    private val RedPrimary = Color(0xFFC62828)
    private val RedPrimaryLight = Color(0xFFE53935)
    private val RedPrimaryDark = Color(0xFF8E0000)
    private val RedPrimarySurface = Color(0xFFFFEBEE)
    private val RedPlayerBackground = Color(0xFF4A0000)
    private val RedPlayerBuffered = Color(0xFFC62828)
    private val RedGradientStart = Color(0xFFC62828)
    private val RedGradientEnd = Color(0xFFE53935)

    private val TealPrimary = Color(0xFF00695C)
    private val TealPrimaryLight = Color(0xFF00897B)
    private val TealPrimaryDark = Color(0xFF004D40)
    private val TealPrimarySurface = Color(0xFFE0F2F1)
    private val TealPlayerBackground = Color(0xFF00352F)
    private val TealPlayerBuffered = Color(0xFF00695C)
    private val TealGradientStart = Color(0xFF00695C)
    private val TealGradientEnd = Color(0xFF00897B)

    private val GoldThemePrimary = Color(0xFF8B6914)
    private val GoldThemePrimaryLight = Color(0xFFA67C1A)
    private val GoldThemePrimaryDark = Color(0xFF6B4F0E)
    private val GoldThemePrimarySurface = Color(0xFFFFF8E1)
    private val GoldThemePlayerBackground = Color(0xFF3D2E00)
    private val GoldThemePlayerBuffered = Color(0xFF8B6914)
    private val GoldThemeGradientStart = Color(0xFF8B6914)
    private val GoldThemeGradientEnd = Color(0xFFA67C1A)

    private val NavyPrimary = Color(0xFF1A237E)
    private val NavyPrimaryLight = Color(0xFF283593)
    private val NavyPrimaryDark = Color(0xFF0D1147)
    private val NavyPrimarySurface = Color(0xFFE8EAF6)
    private val NavyPlayerBackground = Color(0xFF0A0E3A)
    private val NavyPlayerBuffered = Color(0xFF1A237E)
    private val NavyGradientStart = Color(0xFF1A237E)
    private val NavyGradientEnd = Color(0xFF283593)

    // Sepia / Paper reading mode colors
    private val SepiaPrimary = Color(0xFF4A3B32)
    private val SepiaPrimaryLight = Color(0xFF6B5A4E)
    private val SepiaPrimaryDark = Color(0xFF2E2520)
    private val SepiaPrimarySurface = Color(0xFFF4ECD8)
    private val SepiaPlayerBackground = Color(0xFF3E3229)
    private val SepiaPlayerBuffered = Color(0xFF6B5A4E)
    private val SepiaGradientStart = Color(0xFF4A3B32)
    private val SepiaGradientEnd = Color(0xFF6B5A4E)

    fun applyTheme(colorTheme: ColorThemeOption, isDark: Boolean) {
        // Set primary colors based on theme
        when (colorTheme) {
            ColorThemeOption.GREEN -> {
                Primary = GreenPrimary; PrimaryLight = GreenPrimaryLight
                PrimaryDark = GreenPrimaryDark; PrimarySurface = GreenPrimarySurface
                PlayerBackground = GreenPlayerBackground; PlayerBuffered = GreenPlayerBuffered
                GradientStart = GreenGradientStart; GradientEnd = GreenGradientEnd
                BismillahColor = GreenPrimary; MedinanText = GreenPrimary
                DhikrButton = GreenPrimaryLight; DhikrButtonPressed = GreenPrimaryDark
            }
            ColorThemeOption.BLUE -> {
                Primary = BluePrimary; PrimaryLight = BluePrimaryLight
                PrimaryDark = BluePrimaryDark; PrimarySurface = BluePrimarySurface
                PlayerBackground = BluePlayerBackground; PlayerBuffered = BluePlayerBuffered
                GradientStart = BlueGradientStart; GradientEnd = BlueGradientEnd
                BismillahColor = BluePrimary; MedinanText = BluePrimary
                DhikrButton = BluePrimaryLight; DhikrButtonPressed = BluePrimaryDark
            }
            ColorThemeOption.BROWN -> {
                Primary = BrownPrimary; PrimaryLight = BrownPrimaryLight
                PrimaryDark = BrownPrimaryDark; PrimarySurface = BrownPrimarySurface
                PlayerBackground = BrownPlayerBackground; PlayerBuffered = BrownPlayerBuffered
                GradientStart = BrownGradientStart; GradientEnd = BrownGradientEnd
                BismillahColor = BrownPrimary; MedinanText = BrownPrimary
                DhikrButton = BrownPrimaryLight; DhikrButtonPressed = BrownPrimaryDark
            }
            ColorThemeOption.PURPLE -> {
                Primary = PurplePrimary; PrimaryLight = PurplePrimaryLight
                PrimaryDark = PurplePrimaryDark; PrimarySurface = PurplePrimarySurface
                PlayerBackground = PurplePlayerBackground; PlayerBuffered = PurplePlayerBuffered
                GradientStart = PurpleGradientStart; GradientEnd = PurpleGradientEnd
                BismillahColor = PurplePrimary; MedinanText = PurplePrimary
                DhikrButton = PurplePrimaryLight; DhikrButtonPressed = PurplePrimaryDark
            }
            ColorThemeOption.RED -> {
                Primary = RedPrimary; PrimaryLight = RedPrimaryLight
                PrimaryDark = RedPrimaryDark; PrimarySurface = RedPrimarySurface
                PlayerBackground = RedPlayerBackground; PlayerBuffered = RedPlayerBuffered
                GradientStart = RedGradientStart; GradientEnd = RedGradientEnd
                BismillahColor = RedPrimary; MedinanText = RedPrimary
                DhikrButton = RedPrimaryLight; DhikrButtonPressed = RedPrimaryDark
            }
            ColorThemeOption.TEAL -> {
                Primary = TealPrimary; PrimaryLight = TealPrimaryLight
                PrimaryDark = TealPrimaryDark; PrimarySurface = TealPrimarySurface
                PlayerBackground = TealPlayerBackground; PlayerBuffered = TealPlayerBuffered
                GradientStart = TealGradientStart; GradientEnd = TealGradientEnd
                BismillahColor = TealPrimary; MedinanText = TealPrimary
                DhikrButton = TealPrimaryLight; DhikrButtonPressed = TealPrimaryDark
            }
            ColorThemeOption.GOLD_THEME -> {
                Primary = GoldThemePrimary; PrimaryLight = GoldThemePrimaryLight
                PrimaryDark = GoldThemePrimaryDark; PrimarySurface = GoldThemePrimarySurface
                PlayerBackground = GoldThemePlayerBackground; PlayerBuffered = GoldThemePlayerBuffered
                GradientStart = GoldThemeGradientStart; GradientEnd = GoldThemeGradientEnd
                BismillahColor = GoldThemePrimary; MedinanText = GoldThemePrimary
                DhikrButton = GoldThemePrimaryLight; DhikrButtonPressed = GoldThemePrimaryDark
            }
            ColorThemeOption.NAVY -> {
                Primary = NavyPrimary; PrimaryLight = NavyPrimaryLight
                PrimaryDark = NavyPrimaryDark; PrimarySurface = NavyPrimarySurface
                PlayerBackground = NavyPlayerBackground; PlayerBuffered = NavyPlayerBuffered
                GradientStart = NavyGradientStart; GradientEnd = NavyGradientEnd
                BismillahColor = NavyPrimary; MedinanText = NavyPrimary
                DhikrButton = NavyPrimaryLight; DhikrButtonPressed = NavyPrimaryDark
            }
            ColorThemeOption.SEPIA -> {
                Primary = SepiaPrimary; PrimaryLight = SepiaPrimaryLight
                PrimaryDark = SepiaPrimaryDark; PrimarySurface = SepiaPrimarySurface
                PlayerBackground = SepiaPlayerBackground; PlayerBuffered = SepiaPlayerBuffered
                GradientStart = SepiaGradientStart; GradientEnd = SepiaGradientEnd
                BismillahColor = SepiaPrimary; MedinanText = SepiaPrimary
                DhikrButton = SepiaPrimaryLight; DhikrButtonPressed = SepiaPrimaryDark
            }
        }

        // Sepia overrides background/text regardless of dark mode for paper feel
        if (colorTheme == ColorThemeOption.SEPIA) {
            Background = Color(0xFFF4ECD8)
            BackgroundDark = Color(0xFFEDE5D0)
            Surface = Color(0xFFFAF0E6)
            SurfaceVariant = Color(0xFFF0E8D4)
            TextPrimary = Color(0xFF4A3B32)
            TextSecondary = Color(0xFF6B5A4E)
            TextTertiary = Color(0xFF8A7A6E)
            Divider = Color(0xFFD8CFBC)
            PlayerBackground = SepiaPlayerBackground
            PlayerBuffered = SepiaPlayerBuffered
        } else if (isDark) {
            Background = DarkBackground
            BackgroundDark = DarkBackground
            Surface = DarkSurface
            SurfaceVariant = DarkSurfaceVariant
            TextPrimary = DarkTextPrimary
            TextSecondary = DarkTextSecondary
            TextTertiary = DarkTextTertiary
            Divider = DarkDivider
            PlayerBackground = DarkPlayerBackground
            PlayerBuffered = DarkPlayerBuffered
        } else {
            Background = Color(0xFFFBF7F0)
            BackgroundDark = Color(0xFFF0EDE4)
            Surface = Color(0xFFFFFFFF)
            SurfaceVariant = Color(0xFFF5F1EA)
            TextPrimary = Color(0xFF1A1A1A)
            TextSecondary = Color(0xFF5A5A5A)
            TextTertiary = Color(0xFF8A8A8A)
            Divider = Color(0xFFE0DDD6)
            // Reset player colors to theme-specific light versions
            when (colorTheme) {
                ColorThemeOption.GREEN -> {
                    PlayerBackground = GreenPlayerBackground
                    PlayerBuffered = GreenPlayerBuffered
                }
                ColorThemeOption.BLUE -> {
                    PlayerBackground = BluePlayerBackground
                    PlayerBuffered = BluePlayerBuffered
                }
                ColorThemeOption.BROWN -> {
                    PlayerBackground = BrownPlayerBackground
                    PlayerBuffered = BrownPlayerBuffered
                }
                ColorThemeOption.PURPLE -> {
                    PlayerBackground = PurplePlayerBackground
                    PlayerBuffered = PurplePlayerBuffered
                }
                ColorThemeOption.RED -> {
                    PlayerBackground = RedPlayerBackground
                    PlayerBuffered = RedPlayerBuffered
                }
                ColorThemeOption.TEAL -> {
                    PlayerBackground = TealPlayerBackground
                    PlayerBuffered = TealPlayerBuffered
                }
                ColorThemeOption.GOLD_THEME -> {
                    PlayerBackground = GoldThemePlayerBackground
                    PlayerBuffered = GoldThemePlayerBuffered
                }
            ColorThemeOption.NAVY -> {
                    PlayerBackground = NavyPlayerBackground
                    PlayerBuffered = NavyPlayerBuffered
                }
                ColorThemeOption.SEPIA -> {
                    PlayerBackground = SepiaPlayerBackground
                    PlayerBuffered = SepiaPlayerBuffered
                }
            }
        }
    }
}

// ==================== Time-Based Theme Logic ====================
/**
 * Determines the appropriate [ColorThemeOption] based on the device's local time.
 *
 * - Dawn/Morning (4 AM – 10 AM): Warm colors — BROWN
 * - Day (10 AM – 6 PM): Standard — GREEN
 * - Night (6 PM – 4 AM): Dark/eye-resting — NAVY
 */
fun getTimeBasedTheme(): ColorThemeOption {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour in 4..9  -> ColorThemeOption.BROWN   // Dawn/Morning warm tones
        hour in 10..17 -> ColorThemeOption.GREEN   // Daytime standard
        else           -> ColorThemeOption.NAVY    // Night – eye-resting
    }
}

/**
 * Returns whether dark mode should be auto-enabled based on local time.
 * Activates between 8 PM and 5 AM.
 */
fun isNightTime(): Boolean {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return hour >= 20 || hour < 5
}

// ==================== Dynamic Theme State ====================
class ThemeState {
    var isDarkMode by mutableStateOf(false)
    var colorTheme by mutableStateOf(ColorThemeOption.GREEN)
    var arabicFontSize by mutableStateOf(24f)
    var translationFontSize by mutableStateOf(16f)
    /** When true the app automatically switches theme & dark-mode based on time of day. */
    var isAutoTimeThemeEnabled by mutableStateOf(false)
        private set

    fun applySettings(isDark: Boolean, theme: ColorThemeOption, arFont: Float, trFont: Float) {
        isDarkMode = isDark
        colorTheme = theme
        arabicFontSize = arFont
        translationFontSize = trFont
        QuranColors.applyTheme(theme, isDark)
    }

    /**
     * Enable or disable the automatic time-based theme.
     * When enabled the current time is evaluated immediately and the theme is applied.
     */
    fun setAutoTimeTheme(enabled: Boolean) {
        isAutoTimeThemeEnabled = enabled
        if (enabled) applyTimeBasedTheme()
    }

    /**
     * Evaluates the current local time and applies the matching theme + dark mode.
     * Safe to call repeatedly (e.g. from a periodic LaunchedEffect).
     */
    fun applyTimeBasedTheme() {
        val timeTheme = getTimeBasedTheme()
        val shouldBeDark = isNightTime()
        colorTheme = timeTheme
        isDarkMode = shouldBeDark
        QuranColors.applyTheme(timeTheme, shouldBeDark)
    }
}

val LocalThemeState = compositionLocalOf { ThemeState() }

// ==================== Theme Composable ====================
private fun getColorScheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = QuranColors.Primary,
            onPrimary = Color.White,
            primaryContainer = QuranColors.PrimaryDark,
            onPrimaryContainer = QuranColors.PrimaryLight,
            secondary = QuranColors.Gold,
            onSecondary = Color.White,
            secondaryContainer = QuranColors.GoldDark,
            onSecondaryContainer = QuranColors.GoldLight,
            background = QuranColors.Background,
            onBackground = QuranColors.TextPrimary,
            surface = QuranColors.Surface,
            onSurface = QuranColors.TextPrimary,
            surfaceVariant = QuranColors.SurfaceVariant,
            onSurfaceVariant = QuranColors.TextSecondary,
            error = QuranColors.Error,
            onError = Color.White,
            outline = QuranColors.Divider
        )
    } else {
        lightColorScheme(
            primary = QuranColors.Primary,
            onPrimary = Color.White,
            primaryContainer = QuranColors.PrimarySurface,
            onPrimaryContainer = QuranColors.Primary,
            secondary = QuranColors.Gold,
            onSecondary = Color.White,
            secondaryContainer = QuranColors.GoldSurface,
            onSecondaryContainer = QuranColors.GoldDark,
            background = QuranColors.Background,
            onBackground = QuranColors.TextPrimary,
            surface = QuranColors.Surface,
            onSurface = QuranColors.TextPrimary,
            surfaceVariant = QuranColors.SurfaceVariant,
            onSurfaceVariant = QuranColors.TextSecondary,
            error = QuranColors.Error,
            onError = Color.White,
            outline = QuranColors.Divider
        )
    }
}

@Composable
fun QuranTheme(
    themeState: ThemeState = ThemeState(),
    content: @Composable () -> Unit
) {
    // Apply colors whenever theme state changes
    LaunchedEffect(themeState.isDarkMode, themeState.colorTheme) {
        QuranColors.applyTheme(themeState.colorTheme, themeState.isDarkMode)
    }

    // Re-evaluate time-based theme every 5 minutes when auto-time is enabled
    if (themeState.isAutoTimeThemeEnabled) {
        LaunchedEffect(Unit) {
            while (true) {
                themeState.applyTimeBasedTheme()
                kotlinx.coroutines.delay(5 * 60 * 1000L) // 5 minutes
            }
        }
    }

    val colorScheme = getColorScheme(themeState.isDarkMode)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = QuranColors.Primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = themeState.isDarkMode
        }
    }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}