package net.onws.alquranalkarim.data.repository

import net.onws.alquranalkarim.data.api.RetrofitInstance
import net.onws.alquranalkarim.data.local.QuranCacheManager
import net.onws.alquranalkarim.data.model.AudioSurahResponse
import net.onws.alquranalkarim.data.model.EditionListResponse
import net.onws.alquranalkarim.data.model.SurahDetailResponse
import net.onws.alquranalkarim.data.model.SurahListResponse
import net.onws.alquranalkarim.data.model.TafsirResponse

class QuranRepository(private val cacheManager: QuranCacheManager? = null) {

    private val api = RetrofitInstance.api

    suspend fun getSurahList(): SurahListResponse {
        // Try cache first
        cacheManager?.getSurahList()?.let { cachedList ->
            if (cachedList.isNotEmpty()) {
                return SurahListResponse(
                    code = 200,
                    status = "OK",
                    data = cachedList
                )
            }
        }
        // Fetch from API and cache
        try {
            val response = api.getSurahList()
            if (response.code == 200) {
                cacheManager?.saveSurahList(response.data)
            }
            return response
        } catch (e: Exception) {
            // API failed, try cache as fallback
            cacheManager?.getSurahList()?.let { cachedList ->
                if (cachedList.isNotEmpty()) {
                    return SurahListResponse(
                        code = 200,
                        status = "OK",
                        data = cachedList
                    )
                }
            }
            throw e
        }
    }

    suspend fun getSurahDetail(surahNumber: Int): SurahDetailResponse {
        // Try cache first
        cacheManager?.getSurahDetail(surahNumber)?.let { cachedDetail ->
            return SurahDetailResponse(
                code = 200,
                status = "OK",
                data = cachedDetail
            )
        }
        // Fetch from API and cache
        try {
            val response = api.getSurahDetail(surahNumber)
            if (response.code == 200) {
                cacheManager?.saveSurahDetail(surahNumber, response.data)
            }
            return response
        } catch (e: Exception) {
            // API failed, try cache as fallback
            cacheManager?.getSurahDetail(surahNumber)?.let { cachedDetail ->
                return SurahDetailResponse(
                    code = 200,
                    status = "OK",
                    data = cachedDetail
                )
            }
            throw e
        }
    }

    suspend fun getSurahByEdition(surahNumber: Int, edition: String): AudioSurahResponse {
        return api.getSurahByEdition(surahNumber, edition)
    }

    suspend fun getAudioEditions(): EditionListResponse {
        return api.getAudioEditions()
    }

    suspend fun getTranslationEditions(): EditionListResponse {
        return api.getTranslationEditions()
    }

    suspend fun getAllEditions(): EditionListResponse {
        return api.getAllEditions()
    }

    suspend fun getTafsir(surahNumber: Int, edition: String = "ar.muyassar"): TafsirResponse {
        return api.getTafsir(surahNumber, edition)
    }
}
