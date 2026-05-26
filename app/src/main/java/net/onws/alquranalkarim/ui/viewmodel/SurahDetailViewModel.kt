package net.onws.alquranalkarim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.data.local.LastRead
import net.onws.alquranalkarim.data.local.QuranCacheManager
import net.onws.alquranalkarim.data.local.SavedBookmark
import net.onws.alquranalkarim.data.model.Ayah
import net.onws.alquranalkarim.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SurahDetailUiState(
    val surahName: String = "",
    val surahEnglishName: String = "",
    val surahNumber: Int = 0,
    val revelationType: String = "",
    val ayahs: List<Ayah> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val initialAyah: Int = 1,
    val savedAyahNumber: Int = -1
)

class SurahDetailViewModel : ViewModel() {

    private var repository = QuranRepository()
    private var bookmarkManager: BookmarkManager? = null
    private var cacheManager: QuranCacheManager? = null

    private val _uiState = MutableStateFlow(SurahDetailUiState())
    val uiState: StateFlow<SurahDetailUiState> = _uiState.asStateFlow()

    fun setBookmarkManager(manager: BookmarkManager) {
        bookmarkManager = manager
    }

    fun setCacheManager(manager: QuranCacheManager) {
        cacheManager = manager
        repository = QuranRepository(manager)
    }

    fun loadSurah(surahNumber: Int, initialAyah: Int = 1) {
        _uiState.value = _uiState.value.copy(initialAyah = initialAyah)
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = repository.getSurahDetail(surahNumber)
                if (response.code == 200) {
                    val savedBookmarkAyah = bookmarkManager?.getBookmarks()
                        ?.firstOrNull { it.surahNumber == surahNumber }
                        ?.ayahNumber ?: -1

                    _uiState.value = _uiState.value.copy(
                        surahName = response.data.name,
                        surahEnglishName = response.data.englishName,
                        surahNumber = response.data.number,
                        revelationType = response.data.revelationType,
                        ayahs = response.data.ayahs,
                        isLoading = false,
                        savedAyahNumber = savedBookmarkAyah
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "فشل تحميل السورة")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "تحقق من اتصال الإنترنت"
                )
            }
        }
    }

    fun saveBookmark(ayahIndex: Int) {
        val state = _uiState.value
        if (ayahIndex in state.ayahs.indices) {
            val ayahNumber = state.ayahs[ayahIndex].numberInSurah
            val isAlreadyBookmarked = bookmarkManager?.isBookmarked(state.surahNumber, ayahNumber) ?: false

            if (isAlreadyBookmarked) {
                bookmarkManager?.removeBookmark(state.surahNumber, ayahNumber)
                _uiState.value = _uiState.value.copy(savedAyahNumber = -1)
            } else {
                bookmarkManager?.saveBookmark(
                    SavedBookmark(
                        surahNumber = state.surahNumber,
                        surahName = state.surahName,
                        ayahNumber = ayahNumber,
                        ayahText = state.ayahs[ayahIndex].text.take(100)
                    )
                )
                _uiState.value = _uiState.value.copy(savedAyahNumber = ayahNumber)
            }

            val lastRead = LastRead(
                surahNumber = state.surahNumber,
                surahName = state.surahName,
                ayahNumber = ayahNumber,
                ayahText = state.ayahs[ayahIndex].text.take(50)
            )
            bookmarkManager?.saveLastRead(lastRead)
            bookmarkManager?.updateKhatmahFromLastRead(lastRead)
        }
    }

    fun saveLastReadPosition(ayahIndex: Int) {
        val state = _uiState.value
        if (ayahIndex in state.ayahs.indices) {
            val ayahNumber = state.ayahs[ayahIndex].numberInSurah
            val lastRead = LastRead(
                surahNumber = state.surahNumber,
                surahName = state.surahName,
                ayahNumber = ayahNumber,
                ayahText = state.ayahs[ayahIndex].text.take(50)
            )
            bookmarkManager?.saveLastRead(lastRead)
            bookmarkManager?.updateKhatmahFromLastRead(lastRead)
        }
    }
}