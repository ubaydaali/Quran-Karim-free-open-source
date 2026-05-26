package net.onws.alquranalkarim.data.model

import com.google.gson.annotations.SerializedName

// Tafsir API Response
data class TafsirResponse(
    val code: Int,
    val status: String,
    val data: TafsirData
)

data class TafsirData(
    val number: Int,
    val name: String,
    @SerializedName("englishName")
    val englishName: String,
    @SerializedName("englishNameTranslation")
    val englishNameTranslation: String,
    val revelationType: String,
    @SerializedName("numberOfAyahs")
    val numberOfAyahs: Int,
    val ayahs: List<TafsirAyah>
)

data class TafsirAyah(
    val number: Int,
    val text: String,
    @SerializedName("numberInSurah")
    val numberInSurah: Int,
    val juz: Int,
    val page: Int,
    val hizbQuarter: Int
)