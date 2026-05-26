package net.onws.alquranalkarim.data.model

import com.google.gson.annotations.SerializedName

// Surah List Response
data class SurahListResponse(
    val code: Int,
    val status: String,
    val data: List<SurahInfo>
)

data class SurahInfo(
    val number: Int,
    val name: String,
    @SerializedName("englishName")
    val englishName: String,
    @SerializedName("englishNameTranslation")
    val englishNameTranslation: String,
    val revelationType: String,
    @SerializedName("numberOfAyahs")
    val numberOfAyahs: Int
)

// Surah Detail (Arabic Text) Response
data class SurahDetailResponse(
    val code: Int,
    val status: String,
    val data: SurahDetail
)

data class SurahDetail(
    val number: Int,
    val name: String,
    @SerializedName("englishName")
    val englishName: String,
    @SerializedName("englishNameTranslation")
    val englishNameTranslation: String,
    val revelationType: String,
    @SerializedName("numberOfAyahs")
    val numberOfAyahs: Int,
    val ayahs: List<Ayah>
)

data class Ayah(
    val number: Int,
    val text: String,
    @SerializedName("numberInSurah")
    val numberInSurah: Int,
    val juz: Int,
    val page: Int,
    val hizbQuarter: Int
)

// Edition (Translation/Reciter) Response
data class EditionListResponse(
    val code: Int,
    val status: String,
    val data: List<Edition>
)

data class Edition(
    val identifier: String,
    val language: String,
    val name: String,
    @SerializedName("englishName")
    val englishName: String,
    val format: String,
    val type: String
)

// Audio (Edition-based) Surah Response
data class AudioSurahResponse(
    val code: Int,
    val status: String,
    val data: AudioSurahDetail
)

data class AudioSurahDetail(
    val number: Int,
    val name: String,
    @SerializedName("englishName")
    val englishName: String,
    val revelationType: String,
    @SerializedName("numberOfAyahs")
    val numberOfAyahs: Int,
    val ayahs: List<AudioAyah>
)

data class AudioAyah(
    val number: Int,
    val audio: String,
    @SerializedName("audioSecondary")
    val audioSecondary: List<String>,
    val text: String,
    @SerializedName("numberInSurah")
    val numberInSurah: Int,
    val juz: Int,
    val page: Int
)