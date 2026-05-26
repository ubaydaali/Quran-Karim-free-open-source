package net.onws.alquranalkarim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import net.onws.alquranalkarim.data.local.QuranCacheManager
import net.onws.alquranalkarim.data.model.SurahInfo
import net.onws.alquranalkarim.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SurahListUiState(
    val surahs: List<SurahInfo> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val cachedCount: Int = 0,
    val isDownloadingAll: Boolean = false,
    val downloadProgress: Int = 0,
    val downloadTotal: Int = 114
)

class SurahListViewModel : ViewModel() {

    private var repository = QuranRepository()
    private var cacheManager: QuranCacheManager? = null
    private var initialized = false

    private val _uiState = MutableStateFlow(SurahListUiState())
    val uiState: StateFlow<SurahListUiState> = _uiState.asStateFlow()

    fun setCacheManager(manager: QuranCacheManager) {
        cacheManager = manager
        repository = QuranRepository(manager)
        _uiState.value = _uiState.value.copy(cachedCount = manager.getCachedSurahCount())
        if (!initialized) {
            initialized = true
            loadSurahs()
        }
    }

    fun loadSurahs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = repository.getSurahList()
                if (response.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        surahs = response.data,
                        isLoading = false,
                        cachedCount = cacheManager?.getCachedSurahCount() ?: 0
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "فشل تحميل السور"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "تحقق من اتصال الإنترنت"
                )
            }
        }
    }

    fun downloadAllSurahs() {
        val manager = cacheManager ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDownloadingAll = true, downloadProgress = 0)

            // First ensure we have the surah list cached
            if (_uiState.value.surahs.isEmpty()) return@launch

            var successCount = 0
            for (i in 1..114) {
                if (!manager.isSurahCached(i)) {
                    try {
                        val response = repository.getSurahDetail(i)
                        if (response.code == 200) {
                            manager.saveSurahDetail(i, response.data)
                            successCount++
                        }
                    } catch (_: Exception) {
                        // Continue with next surah on error
                    }
                } else {
                    successCount++
                }
                _uiState.value = _uiState.value.copy(downloadProgress = i)
            }

            manager.setAllCached(successCount == 114)
            _uiState.value = _uiState.value.copy(
                isDownloadingAll = false,
                cachedCount = manager.getCachedSurahCount()
            )
        }
    }

    fun refreshCacheStatus() {
        _uiState.value = _uiState.value.copy(
            cachedCount = cacheManager?.getCachedSurahCount() ?: 0
        )
    }
}