package net.onws.alquranalkarim.data.local

import android.content.Context
import android.content.SharedPreferences
import net.onws.alquranalkarim.data.model.SurahDetail
import net.onws.alquranalkarim.data.model.SurahInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuranCacheManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("quran_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_SURAH_LIST = "surah_list"
        private const val KEY_SURAH_PREFIX = "surah_detail_"
        private const val KEY_ALL_CACHED = "all_surahs_cached"
    }

    // ---- Surah List ----

    fun saveSurahList(surahs: List<SurahInfo>) {
        prefs.edit().putString(KEY_SURAH_LIST, gson.toJson(surahs)).apply()
    }

    fun getSurahList(): List<SurahInfo>? {
        val json = prefs.getString(KEY_SURAH_LIST, null) ?: return null
        return try {
            val type = object : TypeToken<List<SurahInfo>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    // ---- Surah Detail (Arabic Text) ----

    fun saveSurahDetail(surahNumber: Int, detail: SurahDetail) {
        prefs.edit().putString("$KEY_SURAH_PREFIX$surahNumber", gson.toJson(detail)).apply()
    }

    fun getSurahDetail(surahNumber: Int): SurahDetail? {
        val json = prefs.getString("$KEY_SURAH_PREFIX$surahNumber", null) ?: return null
        return try {
            gson.fromJson(json, SurahDetail::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun isSurahCached(surahNumber: Int): Boolean {
        return prefs.contains("$KEY_SURAH_PREFIX$surahNumber")
    }

    // ---- Bulk cache status ----

    fun setAllCached(cached: Boolean) {
        prefs.edit().putBoolean(KEY_ALL_CACHED, cached).apply()
    }

    fun isAllCached(): Boolean {
        return prefs.getBoolean(KEY_ALL_CACHED, false)
    }

    fun getCachedSurahCount(): Int {
        var count = 0
        for (i in 1..114) {
            if (isSurahCached(i)) count++
        }
        return count
    }

    fun clearCache() {
        prefs.edit().clear().apply()
    }
}