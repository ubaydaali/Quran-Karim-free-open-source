package net.onws.alquranalkarim.data.api

import net.onws.alquranalkarim.data.model.AudioSurahResponse
import net.onws.alquranalkarim.data.model.EditionListResponse
import net.onws.alquranalkarim.data.model.SurahDetailResponse
import net.onws.alquranalkarim.data.model.SurahListResponse
import net.onws.alquranalkarim.data.model.TafsirResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {

    @GET("surah")
    suspend fun getSurahList(): SurahListResponse

    @GET("surah/{surahNumber}")
    suspend fun getSurahDetail(@Path("surahNumber") surahNumber: Int): SurahDetailResponse

    @GET("surah/{surahNumber}/{edition}")
    suspend fun getSurahByEdition(
        @Path("surahNumber") surahNumber: Int,
        @Path("edition") edition: String
    ): AudioSurahResponse

    @GET("edition")
    suspend fun getAllEditions(): EditionListResponse

    @GET("edition/format/audio")
    suspend fun getAudioEditions(): EditionListResponse

    @GET("edition/type/translation")
    suspend fun getTranslationEditions(): EditionListResponse

    // Tafsir endpoint (e.g. ar.muyassar for simplified Arabic tafsir)
    @GET("surah/{surahNumber}/{edition}")
    suspend fun getTafsir(
        @Path("surahNumber") surahNumber: Int,
        @Path("edition") edition: String
    ): TafsirResponse
}