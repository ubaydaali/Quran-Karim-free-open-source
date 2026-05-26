package net.onws.alquranalkarim.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// ==================== Data Classes ====================
data class LastRead(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val ayahText: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SavedBookmark(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val ayahText: String,
    val note: String = "",
    val category: String = "عام",
    val timestamp: Long = System.currentTimeMillis()
)

data class BookmarkCategory(
    val name: String,
    val color: Long = 0xFF4CAF50,
    val icon: String = "bookmark"
)

data class KhatmahProgress(
    val currentKhatmah: Int = 1,
    val lastSurah: Int = 1,
    val lastAyah: Int = 1,
    val startDate: Long = System.currentTimeMillis(),
    val completedKhatmahs: Int = 0
)

data class ReadingStats(
    val totalAyahsRead: Int = 0,
    val dailyAyahsRead: Int = 0,
    val lastReadDate: String = "",
    val streak: Int = 0,
    val longestStreak: Int = 0,
    val lastStreakDate: String = "",
    val dailyGoal: Int = 20,
    val ramadanKhatmahStart: Long = 0L,
    val ramadanAyahsRead: Int = 0,
    val ramadanDaysRead: Int = 0,
    val weeklyReading: List<Int> = listOf(0, 0, 0, 0, 0, 0, 0)
)

data class AppSettings(
    val isDarkMode: Boolean = false,
    val arabicFontSize: Float = 24f,
    val translationFontSize: Float = 16f,
    val sleepTimerMinutes: Int = 0,
    val repeatCount: Int = 1,
    val playbackSpeed: Float = 1.0f,
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 5,
    val reminderMinute: Int = 0,
    val colorTheme: Int = 0,
    val arabicFontStyle: Int = 0 // 0=default, 1=uthmani, 2=naskh
)

data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long = 0L
)

// ==================== Memorization Data Classes ====================
data class MemorizationPlan(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val surahNumber: Int = 1,
    val startAyah: Int = 1,
    val endAyah: Int = 7,
    val dailyAyahCount: Int = 3,
    val startDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val completedAyahs: List<Int> = emptyList(), // list of ayah numbers that are memorized
    val reviewDates: List<Long> = emptyList() // timestamps of review sessions
)

data class MemorizationSession(
    val id: String = java.util.UUID.randomUUID().toString(),
    val planId: String,
    val ayahNumber: Int,
    val date: Long = System.currentTimeMillis(),
    val quality: Int = 0, // 0-5 (spaced repetition quality)
    val nextReviewDate: Long = 0L,
    val repetitions: Int = 0,
    val easeFactor: Float = 2.5f,
    val interval: Int = 0 // days until next review
)

class BookmarkManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_MEMORIZATION_PLANS = "memorization_plans"
        private const val KEY_MEMORIZATION_SESSIONS = "memorization_sessions"
        private const val KEY_LAST_SURAH = "last_surah"
        private const val KEY_LAST_AYAH = "last_ayah"
        private const val KEY_LAST_SURAH_NAME = "last_surah_name"
        private const val KEY_LAST_AYAH_TEXT = "last_ayah_text"
        private const val KEY_LAST_TIMESTAMP = "last_timestamp"
        private const val KEY_SELECTED_RECITER = "selected_reciter"
        private const val KEY_SELECTED_RECITER_NAME = "selected_reciter_name"
        private const val KEY_BOOKMARKS = "saved_bookmarks"
        private const val KEY_KHATMAH = "khatmah_progress"
        private const val KEY_READING_STATS = "reading_stats"
        private const val KEY_APP_SETTINGS = "app_settings"
        private const val KEY_CATEGORIES = "bookmark_categories"
        private const val KEY_ACHIEVEMENTS = "achievements"
        private const val KEY_VERSE_OF_DAY = "verse_of_day"
        private const val KEY_VERSE_OF_DAY_DATE = "verse_of_day_date"
    }

    // ==================== Last Read ====================
    fun saveLastRead(lastRead: LastRead) {
        prefs.edit().apply {
            putInt(KEY_LAST_SURAH, lastRead.surahNumber)
            putInt(KEY_LAST_AYAH, lastRead.ayahNumber)
            putString(KEY_LAST_SURAH_NAME, lastRead.surahName)
            putString(KEY_LAST_AYAH_TEXT, lastRead.ayahText)
            putLong(KEY_LAST_TIMESTAMP, lastRead.timestamp)
            apply()
        }
    }

    fun getLastRead(): LastRead? {
        val surah = prefs.getInt(KEY_LAST_SURAH, -1)
        if (surah == -1) return null
        return LastRead(
            surahNumber = surah,
            surahName = prefs.getString(KEY_LAST_SURAH_NAME, "") ?: "",
            ayahNumber = prefs.getInt(KEY_LAST_AYAH, 1),
            ayahText = prefs.getString(KEY_LAST_AYAH_TEXT, "") ?: "",
            timestamp = prefs.getLong(KEY_LAST_TIMESTAMP, 0L)
        )
    }

    // ==================== Reciter ====================
    fun saveSelectedReciter(identifier: String, name: String) {
        prefs.edit().apply {
            putString(KEY_SELECTED_RECITER, identifier)
            putString(KEY_SELECTED_RECITER_NAME, name)
            apply()
        }
    }

    fun getSelectedReciter(): Pair<String, String> {
        return Pair(
            prefs.getString(KEY_SELECTED_RECITER, "ar.alafasy") ?: "ar.alafasy",
            prefs.getString(KEY_SELECTED_RECITER_NAME, "مشاري راشد العفاسي") ?: "مشاري راشد العفاسي"
        )
    }

    // ==================== Bookmark Categories ====================
    fun getCategories(): List<BookmarkCategory> {
        val json = prefs.getString(KEY_CATEGORIES, null)
        return try {
            if (json != null) {
                val type = object : TypeToken<List<BookmarkCategory>>() {}.type
                gson.fromJson(json, type) ?: getDefaultCategories()
            } else getDefaultCategories()
        } catch (_: Exception) {
            getDefaultCategories()
        }
    }

    private fun getDefaultCategories(): List<BookmarkCategory> {
        return listOf(
            BookmarkCategory("عام", 0xFF4CAF50, "bookmark"),
            BookmarkCategory("أدعية", 0xFF2196F3, "hand"),
            BookmarkCategory("آيات شفاء", 0xFFFF9800, "healing"),
            BookmarkCategory("آيات صبر", 0xFF9C27B0, "patience"),
            BookmarkCategory("توحيد", 0xFFF44336, "monotheism"),
            BookmarkCategory("حلو للحفظ", 0xFF00BCD4, "star")
        )
    }

    fun saveCategories(categories: List<BookmarkCategory>) {
        prefs.edit().putString(KEY_CATEGORIES, gson.toJson(categories)).apply()
    }

    fun addCategory(category: BookmarkCategory) {
        val categories = getCategories().toMutableList()
        if (categories.none { it.name == category.name }) {
            categories.add(category)
            saveCategories(categories)
        }
    }

    fun deleteCategory(name: String) {
        if (name == "عام") return // Can't delete default
        val categories = getCategories().toMutableList()
        categories.removeAll { it.name == name }
        saveCategories(categories)
    }

    // ==================== Saved Bookmarks ====================
    fun saveBookmark(bookmark: SavedBookmark) {
        val bookmarks = getBookmarks().toMutableList()
        val existing = bookmarks.indexOfFirst {
            it.surahNumber == bookmark.surahNumber && it.ayahNumber == bookmark.ayahNumber
        }
        if (existing >= 0) {
            bookmarks[existing] = bookmark
        } else {
            bookmarks.add(0, bookmark)
        }
        prefs.edit().putString(KEY_BOOKMARKS, gson.toJson(bookmarks)).apply()
    }

    fun removeBookmark(surahNumber: Int, ayahNumber: Int) {
        val bookmarks = getBookmarks().toMutableList()
        bookmarks.removeAll { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
        prefs.edit().putString(KEY_BOOKMARKS, gson.toJson(bookmarks)).apply()
    }

    fun isBookmarked(surahNumber: Int, ayahNumber: Int): Boolean {
        return getBookmarks().any { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
    }

    fun getBookmarks(): List<SavedBookmark> {
        val json = prefs.getString(KEY_BOOKMARKS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<SavedBookmark>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getBookmarksByCategory(category: String): List<SavedBookmark> {
        return getBookmarks().filter { it.category == category }
    }

    // ==================== Khatmah ====================
    fun getKhatmahProgress(): KhatmahProgress {
        val json = prefs.getString(KEY_KHATMAH, null)
        return try {
            if (json != null) gson.fromJson(json, KhatmahProgress::class.java)
            else KhatmahProgress()
        } catch (_: Exception) {
            KhatmahProgress()
        }
    }

    fun saveKhatmahProgress(progress: KhatmahProgress) {
        prefs.edit().putString(KEY_KHATMAH, gson.toJson(progress)).apply()
    }

    fun updateKhatmahFromLastRead(lastRead: LastRead) {
        val progress = getKhatmahProgress().copy(
            lastSurah = lastRead.surahNumber,
            lastAyah = lastRead.ayahNumber
        )
        saveKhatmahProgress(progress)
    }

    fun completeKhatmah() {
        val current = getKhatmahProgress()
        val progress = current.copy(
            currentKhatmah = current.currentKhatmah + 1,
            completedKhatmahs = current.completedKhatmahs + 1,
            lastSurah = 1,
            lastAyah = 1,
            startDate = System.currentTimeMillis()
        )
        saveKhatmahProgress(progress)
    }

    // ==================== Reading Stats ====================
    fun getReadingStats(): ReadingStats {
        val json = prefs.getString(KEY_READING_STATS, null)
        return try {
            if (json != null) gson.fromJson(json, ReadingStats::class.java)
            else ReadingStats()
        } catch (_: Exception) {
            ReadingStats()
        }
    }

    fun saveReadingStats(stats: ReadingStats) {
        prefs.edit().putString(KEY_READING_STATS, gson.toJson(stats)).apply()
    }

    fun recordAyahRead() {
        val stats = getReadingStats()
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())

        val newDaily = if (stats.lastReadDate == today) stats.dailyAyahsRead + 1 else 1
        val newStreak = if (stats.lastReadDate == today) {
            stats.streak
        } else if (stats.lastReadDate == java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date(System.currentTimeMillis() - 86400000L))) {
            stats.streak + 1
        } else {
            1
        }

        // Update weekly reading
        val weeklyReading = stats.weeklyReading.toMutableList()
        val dayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) - 1
        if (dayOfWeek in 0..6) {
            weeklyReading[dayOfWeek] = if (stats.lastReadDate == today) weeklyReading.getOrElse(dayOfWeek) { 0 } + 1 else 1
        }

        val newStats = stats.copy(
            totalAyahsRead = stats.totalAyahsRead + 1,
            dailyAyahsRead = newDaily,
            lastReadDate = today,
            streak = newStreak,
            longestStreak = maxOf(stats.longestStreak, newStreak),
            lastStreakDate = today,
            weeklyReading = weeklyReading
        )
        saveReadingStats(newStats)

        // Check achievements after recording
        checkAndUnlockAchievements(newStats)
    }

    // ==================== Achievements ====================
    fun getAchievements(): List<Achievement> {
        val json = prefs.getString(KEY_ACHIEVEMENTS, null)
        return try {
            if (json != null) {
                val type = object : TypeToken<List<Achievement>>() {}.type
                gson.fromJson(json, type) ?: getDefaultAchievements()
            } else getDefaultAchievements()
        } catch (_: Exception) {
            getDefaultAchievements()
        }
    }

    private fun getDefaultAchievements(): List<Achievement> {
        return listOf(
            Achievement("first_ayah", "البداية", "اقرأ أول آية", "🌱"),
            Achievement("read_100", "قارئ", "اقرأ 100 آية", "📖"),
            Achievement("read_1000", "مجتهد", "اقرأ 1000 آية", "📚"),
            Achievement("read_6236", "خاتم القرآن", "اقرأ جميع آيات القرآن", "🏆"),
            Achievement("streak_3", "مداوم", "اقرأ 3 أيام متتالية", "🔥"),
            Achievement("streak_7", "منتظم", "اقرأ 7 أيام متتالية", "⭐"),
            Achievement("streak_30", "مثابر", "اقرأ 30 يوماً متتالياً", "💎"),
            Achievement("khatmah_1", "خاتم أول", "أكمل ختمة أولى", "🎉"),
            Achievement("khatmah_3", "خاتم ثلاث", "أكمل 3 ختمات", "🥇"),
            Achievement("khatmah_10", "خاتم عشر", "أكمل 10 ختمات", "👑"),
            Achievement("daily_goal_7", "هدف أسبوعي", "حقق هدفك اليومي 7 أيام", "✅"),
            Achievement("bookmark_10", "جامع", "احفظ 10 آيات في المفضلة", "⭐"),
            Achievement("bookmark_50", "محفظ", "احفظ 50 آية في المفضلة", "🌟"),
            Achievement("night_reader", "قارئ الليل", "اقرأ بعد منتصف الليل", "🌙"),
            Achievement("early_bird", "مبكر", "اقرأ قبل الفجر", "🌅")
        )
    }

    private fun saveAchievements(achievements: List<Achievement>) {
        prefs.edit().putString(KEY_ACHIEVEMENTS, gson.toJson(achievements)).apply()
    }

    private fun checkAndUnlockAchievements(stats: ReadingStats) {
        val achievements = getAchievements().toMutableList()
        var changed = false

        val now = System.currentTimeMillis()
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)

        fun unlock(id: String) {
            val idx = achievements.indexOfFirst { it.id == id && !it.isUnlocked }
            if (idx >= 0) {
                achievements[idx] = achievements[idx].copy(isUnlocked = true, unlockedAt = now)
                changed = true
            }
        }

        if (stats.totalAyahsRead >= 1) unlock("first_ayah")
        if (stats.totalAyahsRead >= 100) unlock("read_100")
        if (stats.totalAyahsRead >= 1000) unlock("read_1000")
        if (stats.totalAyahsRead >= 6236) unlock("read_6236")
        if (stats.streak >= 3) unlock("streak_3")
        if (stats.streak >= 7) unlock("streak_7")
        if (stats.streak >= 30) unlock("streak_30")
        if (getKhatmahProgress().completedKhatmahs >= 1) unlock("khatmah_1")
        if (getKhatmahProgress().completedKhatmahs >= 3) unlock("khatmah_3")
        if (getKhatmahProgress().completedKhatmahs >= 10) unlock("khatmah_10")
        if (getBookmarks().size >= 10) unlock("bookmark_10")
        if (getBookmarks().size >= 50) unlock("bookmark_50")
        if (hour in 0..4) unlock("night_reader")
        if (hour in 4..5) unlock("early_bird")

        if (changed) saveAchievements(achievements)
    }

    fun unlockAchievement(id: String) {
        val achievements = getAchievements().toMutableList()
        val idx = achievements.indexOfFirst { it.id == id && !it.isUnlocked }
        if (idx >= 0) {
            achievements[idx] = achievements[idx].copy(isUnlocked = true, unlockedAt = System.currentTimeMillis())
            saveAchievements(achievements)
        }
    }

    // ==================== Verse of the Day ====================
    fun getVerseOfDay(): String? {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())
        val savedDate = prefs.getString(KEY_VERSE_OF_DAY_DATE, "")
        return if (savedDate == today) {
            prefs.getString(KEY_VERSE_OF_DAY, null)
        } else null
    }

    fun saveVerseOfDay(json: String) {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())
        prefs.edit().apply {
            putString(KEY_VERSE_OF_DAY, json)
            putString(KEY_VERSE_OF_DAY_DATE, today)
            apply()
        }
    }

    // ==================== App Settings ====================
    fun getSettings(): AppSettings {
        val json = prefs.getString(KEY_APP_SETTINGS, null)
        return try {
            if (json != null) gson.fromJson(json, AppSettings::class.java)
            else AppSettings()
        } catch (_: Exception) {
            AppSettings()
        }
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit().putString(KEY_APP_SETTINGS, gson.toJson(settings)).apply()
    }

    // ==================== Memorization Plans ====================
    fun getMemorizationPlans(): List<MemorizationPlan> {
        val json = prefs.getString(KEY_MEMORIZATION_PLANS, null)
        return try {
            if (json != null) {
                val type = object : TypeToken<List<MemorizationPlan>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } else emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveMemorizationPlan(plan: MemorizationPlan) {
        val plans = getMemorizationPlans().toMutableList()
        val idx = plans.indexOfFirst { it.id == plan.id }
        if (idx >= 0) plans[idx] = plan else plans.add(plan)
        prefs.edit().putString(KEY_MEMORIZATION_PLANS, gson.toJson(plans)).apply()
    }

    fun deleteMemorizationPlan(planId: String) {
        val plans = getMemorizationPlans().toMutableList()
        plans.removeAll { it.id == planId }
        prefs.edit().putString(KEY_MEMORIZATION_PLANS, gson.toJson(plans)).apply()
    }

    // ==================== Memorization Sessions (Spaced Repetition) ====================
    fun getMemorizationSessions(planId: String): List<MemorizationSession> {
        return getAllMemorizationSessions().filter { it.planId == planId }
    }

    fun getAllMemorizationSessions(): List<MemorizationSession> {
        val json = prefs.getString(KEY_MEMORIZATION_SESSIONS, null)
        return try {
            if (json != null) {
                val type = object : TypeToken<List<MemorizationSession>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } else emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveMemorizationSession(session: MemorizationSession) {
        val sessions = getAllMemorizationSessions().toMutableList()
        sessions.removeAll { it.planId == session.planId && it.ayahNumber == session.ayahNumber }
        sessions.add(session)
        prefs.edit().putString(KEY_MEMORIZATION_SESSIONS, gson.toJson(sessions)).apply()
    }

    fun getDueReviews(): List<MemorizationSession> {
        val now = System.currentTimeMillis()
        return getAllMemorizationSessions().filter { it.nextReviewDate <= now && it.nextReviewDate > 0 }
    }

    // Spaced Repetition (SM-2 Algorithm)
    fun calculateNextReview(session: MemorizationSession, quality: Int): MemorizationSession {
        val newEaseFactor = maxOf(1.3f, session.easeFactor + (0.1f - (5 - quality) * (0.08f + (5 - quality) * 0.02f)))
        val newRepetitions = if (quality >= 3) session.repetitions + 1 else 0
        val newInterval = when {
            newRepetitions == 0 -> 1
            newRepetitions == 1 -> 1
            newRepetitions == 2 -> 6
            else -> (session.interval * newEaseFactor).toInt()
        }
        return session.copy(
            quality = quality,
            repetitions = newRepetitions,
            easeFactor = newEaseFactor,
            interval = newInterval,
            nextReviewDate = System.currentTimeMillis() + newInterval * 86400000L
        )
    }

    fun clearLastRead() {
        prefs.edit().remove(KEY_LAST_SURAH)
            .remove(KEY_LAST_AYAH)
            .remove(KEY_LAST_SURAH_NAME)
            .remove(KEY_LAST_AYAH_TEXT)
            .remove(KEY_LAST_TIMESTAMP)
            .apply()
    }
}