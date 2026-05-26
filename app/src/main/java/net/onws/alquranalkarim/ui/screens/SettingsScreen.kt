package net.onws.alquranalkarim.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import net.onws.alquranalkarim.data.local.AppSettings
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.notification.AlarmScheduler
import net.onws.alquranalkarim.ui.theme.ColorThemeOption
import net.onws.alquranalkarim.ui.theme.QuranColors
import net.onws.alquranalkarim.ui.theme.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    bookmarkManager: BookmarkManager,
    themeState: ThemeState,
    onBackClick: () -> Unit,
    onAboutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var settings by remember { mutableStateOf(bookmarkManager.getSettings()) }

    // Permission launcher for notifications (Android 13+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val newSettings = settings.copy(reminderEnabled = true)
            settings = newSettings
            bookmarkManager.saveSettings(newSettings)
            AlarmScheduler.scheduleDailyReminder(context, newSettings.reminderHour, newSettings.reminderMinute)
        }
        // If not granted, do nothing (reminder stays off)
    }

    fun updateSettings(newSettings: AppSettings) {
        val oldSettings = settings
        settings = newSettings
        bookmarkManager.saveSettings(newSettings)
        themeState.applySettings(
            isDark = newSettings.isDarkMode,
            theme = ColorThemeOption.entries.getOrElse(newSettings.colorTheme) { ColorThemeOption.GREEN },
            arFont = newSettings.arabicFontSize,
            trFont = newSettings.translationFontSize
        )

        // Handle alarm scheduling when reminder settings change
        if (newSettings.reminderEnabled != oldSettings.reminderEnabled ||
            newSettings.reminderHour != oldSettings.reminderHour ||
            newSettings.reminderMinute != oldSettings.reminderMinute) {
            if (newSettings.reminderEnabled) {
                AlarmScheduler.scheduleDailyReminder(context, newSettings.reminderHour, newSettings.reminderMinute)
            } else {
                AlarmScheduler.cancelReminder(context)
            }
        }
    }

    fun handleReminderToggle(enabled: Boolean) {
        if (enabled) {
            // Check notification permission on Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    return
                }
            }
            // Permission granted or pre-Android 13
            val newSettings = settings.copy(reminderEnabled = true)
            settings = newSettings
            bookmarkManager.saveSettings(newSettings)
            AlarmScheduler.scheduleDailyReminder(context, newSettings.reminderHour, newSettings.reminderMinute)
        } else {
            val newSettings = settings.copy(reminderEnabled = false)
            settings = newSettings
            bookmarkManager.saveSettings(newSettings)
            AlarmScheduler.cancelReminder(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "الإعدادات",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ===== Appearance Section =====
            SectionHeader("المظهر")

            // Dark Mode Toggle
            SettingToggleCard(
                icon = if (settings.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                title = "الوضع الليلي",
                subtitle = if (settings.isDarkMode) "مفعّل" else "معطّل",
                checked = settings.isDarkMode,
                onCheckedChange = { updateSettings(settings.copy(isDarkMode = it)) }
            )

            // Color Theme
            SettingCard(
                icon = Icons.Default.Palette,
                title = "لون السمة",
                subtitle = when (settings.colorTheme) {
                    0 -> "أخضر إسلامي"
                    1 -> "أزرق"
                    2 -> "بني كلاسيكي"
                    3 -> "بنفسجي"
                    4 -> "أحمر"
                    5 -> "أزرق مخضر"
                    6 -> "ذهبي"
                    7 -> "كحلي"
                    else -> "أخضر إسلامي"
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorThemeOption.entries.forEachIndexed { index, option ->
                        val color = when (option) {
                            ColorThemeOption.GREEN -> androidx.compose.ui.graphics.Color(0xFF0D5E3B).copy(alpha = if (settings.colorTheme == 0) 1f else 0.4f)
                            ColorThemeOption.BLUE -> androidx.compose.ui.graphics.Color(0xFF1565C0).copy(alpha = if (settings.colorTheme == 1) 1f else 0.4f)
                            ColorThemeOption.BROWN -> androidx.compose.ui.graphics.Color(0xFF6D4C41).copy(alpha = if (settings.colorTheme == 2) 1f else 0.4f)
                            ColorThemeOption.PURPLE -> androidx.compose.ui.graphics.Color(0xFF6A1B9A).copy(alpha = if (settings.colorTheme == 3) 1f else 0.4f)
                            ColorThemeOption.RED -> androidx.compose.ui.graphics.Color(0xFFC62828).copy(alpha = if (settings.colorTheme == 4) 1f else 0.4f)
                            ColorThemeOption.TEAL -> androidx.compose.ui.graphics.Color(0xFF00695C).copy(alpha = if (settings.colorTheme == 5) 1f else 0.4f)
                            ColorThemeOption.GOLD_THEME -> androidx.compose.ui.graphics.Color(0xFF8B6914).copy(alpha = if (settings.colorTheme == 6) 1f else 0.4f)
                            ColorThemeOption.NAVY -> androidx.compose.ui.graphics.Color(0xFF1A237E).copy(alpha = if (settings.colorTheme == 7) 1f else 0.4f)
                            ColorThemeOption.SEPIA -> androidx.compose.ui.graphics.Color(0xFFF4ECD8).copy(alpha = if (settings.colorTheme == 8) 1f else 0.4f)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(color)
                                .then(
                                    if (settings.colorTheme == index) Modifier.border(3.dp, QuranColors.Gold, CircleShape)
                                    else Modifier
                                )
                                .clickable { updateSettings(settings.copy(colorTheme = index)) }
                        )
                    }
                }
            }

            // Arabic Font Size
            SettingCard(
                icon = Icons.Default.FormatSize,
                title = "حجم الخط العربي",
                subtitle = "${settings.arabicFontSize.toInt()} نقطة"
            ) {
                Slider(
                    value = settings.arabicFontSize,
                    onValueChange = { updateSettings(settings.copy(arabicFontSize = it)) },
                    valueRange = 18f..40f,
                    steps = 21,
                    colors = SliderDefaults.colors(
                        thumbColor = QuranColors.Primary,
                        activeTrackColor = QuranColors.Primary
                    )
                )
            }

            // Translation Font Size
            SettingCard(
                icon = Icons.Default.TextFields,
                title = "حجم خط الترجمة",
                subtitle = "${settings.translationFontSize.toInt()} نقطة"
            ) {
                Slider(
                    value = settings.translationFontSize,
                    onValueChange = { updateSettings(settings.copy(translationFontSize = it)) },
                    valueRange = 12f..28f,
                    steps = 15,
                    colors = SliderDefaults.colors(
                        thumbColor = QuranColors.Primary,
                        activeTrackColor = QuranColors.Primary
                    )
                )
            }

            // ===== Reminder Section =====
            SectionHeader("التذكير")

            SettingToggleCard(
                icon = Icons.Default.Notifications,
                title = "تذكير يومي بقراءة القرآن",
                subtitle = if (settings.reminderEnabled) "مفعّل - ${String.format("%02d:%02d", settings.reminderHour, settings.reminderMinute)}" else "معطّل",
                checked = settings.reminderEnabled,
                onCheckedChange = { handleReminderToggle(it) }
            )

            if (settings.reminderEnabled) {
                // Time picker simplified
                SettingCard(
                    icon = Icons.Default.Schedule,
                    title = "وقت التذكير",
                    subtitle = "${String.format("%02d:%02d", settings.reminderHour, settings.reminderMinute)}"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Hour
                        IconButton(onClick = {
                            val newHour = (settings.reminderHour - 1 + 24) % 24
                            updateSettings(settings.copy(reminderHour = newHour))
                        }) {
                            Icon(Icons.Default.Remove, "minus", tint = QuranColors.Primary)
                        }
                        Text(
                            String.format("%02d", settings.reminderHour),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.TextPrimary
                        )
                        Text(" : ", fontSize = 24.sp, color = QuranColors.TextSecondary)
                        IconButton(onClick = {
                            val newHour = (settings.reminderHour + 1) % 24
                            updateSettings(settings.copy(reminderHour = newHour))
                        }) {
                            Icon(Icons.Default.Add, "plus", tint = QuranColors.Primary)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        IconButton(onClick = {
                            val newMin = (settings.reminderMinute - 15 + 60) % 60
                            updateSettings(settings.copy(reminderMinute = newMin))
                        }) {
                            Icon(Icons.Default.Remove, "minus", tint = QuranColors.Primary)
                        }
                        Text(
                            String.format("%02d", settings.reminderMinute),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.TextPrimary
                        )
                        IconButton(onClick = {
                            val newMin = (settings.reminderMinute + 15) % 60
                            updateSettings(settings.copy(reminderMinute = newMin))
                        }) {
                            Icon(Icons.Default.Add, "plus", tint = QuranColors.Primary)
                        }
                    }
                }
            }

            // ===== About Section =====
            SectionHeader("حول التطبيق")

            // About App
            SettingCard(
                icon = Icons.Default.Info,
                title = "حول التطبيق ومصادر الحقوق",
                subtitle = "معلومات المطور والمشروع والمصادر"
            ) {
                Button(
                    onClick = onAboutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuranColors.Primary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("عرض معلومات التطبيق", fontWeight = FontWeight.Bold)
                }
            }

            // Share App
            SettingCard(
                icon = Icons.Default.Share,
                title = "مشاركة التطبيق",
                subtitle = "شارك التطبيق مع أصدقائك وعائلتك"
            ) {
                Button(
                    onClick = {
                        val shareText = buildString {
                            append("تطبيق القرآن الكريم 📱\n\n")
                            append("تطبيق مجاني ومفتوح المصدر لقراءة القرآن الكريم\n\n")
                            append("حمّله من Google Play:\n")
                            append("https://play.google.com/store/apps/details?id=net.onws.alquranalkarim")
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "مشاركة التطبيق"))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuranColors.Primary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("مشاركة التطبيق", fontWeight = FontWeight.Bold)
                }
            }

            // Rate App
            SettingCard(
                icon = Icons.Default.Star,
                title = "تقييم التطبيق",
                subtitle = "قيّم التطبيق على Google Play لمساعدتنا"
            ) {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.onws.alquranalkarim"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=net.onws.alquranalkarim"))
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuranColors.GoldDark
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp), tint = QuranColors.TextOnPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تقييم على Google Play", fontWeight = FontWeight.Bold, color = QuranColors.TextOnPrimary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = QuranColors.Primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingToggleCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
                .clickable { onCheckedChange(!checked) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = QuranColors.Primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium, color = QuranColors.TextPrimary)
                Text(subtitle, fontSize = 13.sp, color = QuranColors.TextSecondary)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = QuranColors.TextOnPrimary,
                    checkedTrackColor = QuranColors.Primary,
                    uncheckedThumbColor = QuranColors.TextTertiary,
                    uncheckedTrackColor = QuranColors.SurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun SettingCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    content: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = QuranColors.Primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Medium, color = QuranColors.TextPrimary)
                    Text(subtitle, fontSize = 13.sp, color = QuranColors.TextSecondary)
                }
            }
            content?.invoke()
        }
    }
}