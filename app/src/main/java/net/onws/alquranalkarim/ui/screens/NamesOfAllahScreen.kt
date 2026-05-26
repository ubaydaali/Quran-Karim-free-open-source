package net.onws.alquranalkarim.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.onws.alquranalkarim.ui.theme.QuranColors

data class NameOfAllah(
    val name: String,
    val meaning: String,
    val transliteration: String
)

val namesOfAllah = listOf(
    NameOfAllah("الرَّحْمَنُ", "الرحمة الواسعة", "Ar-Rahman"),
    NameOfAllah("الرَّحِيمُ", "الرحمة الخاصة بالمؤمنين", "Ar-Raheem"),
    NameOfAllah("الْمَلِكُ", "المالك المتصرف", "Al-Malik"),
    NameOfAllah("الْقُدُّوسُ", "المنزه عن كل نقص", "Al-Quddus"),
    NameOfAllah("السَّلَامُ", "السالم من العيوب", "As-Salam"),
    NameOfAllah("الْمُؤْمِنُ", "المصدق لرسله", "Al-Mu'min"),
    NameOfAllah("الْمُهَيْمِنُ", "الرقيب الحافظ", "Al-Muhaymin"),
    NameOfAllah("الْعَزِيزُ", "الغالب الذي لا يُغلب", "Al-Aziz"),
    NameOfAllah("الْجَبَّارُ", "الذي يجبر كسر عباده", "Al-Jabbar"),
    NameOfAllah("الْمُتَكَبِّرُ", "المتعالي عن صفات الخلق", "Al-Mutakabbir"),
    NameOfAllah("الْخَالِقُ", "الموجد للأشياء", "Al-Khaliq"),
    NameOfAllah("الْبَارِئُ", "المُبدع للخلق", "Al-Bari"),
    NameOfAllah("الْمُصَوِّرُ", "المعطي لكل مخلوق صورته", "Al-Musawwir"),
    NameOfAllah("الْغَفَّارُ", "كثير المغفرة", "Al-Ghaffar"),
    NameOfAllah("الْقَهَّارُ", "الغالب الذي قهر كل شيء", "Al-Qahhar"),
    NameOfAllah("الْوَهَّابُ", "الكثير العطاء", "Al-Wahhab"),
    NameOfAllah("الرَّزَّاقُ", "كثير الرزق", "Ar-Razzaq"),
    NameOfAllah("الْفَتَّاحُ", "الذي يفتح أبواب الخير", "Al-Fattah"),
    NameOfAllah("اَلْعَلِيمُ", "المحيط علمه بكل شيء", "Al-Aleem"),
    NameOfAllah("الْقَابِضُ", "الذي يقبض الأرواح", "Al-Qabid"),
    NameOfAllah("الْبَاسِطُ", "الذي يبسط الرزق", "Al-Basit"),
    NameOfAllah("الْخَافِضُ", "الذي يخفض الظالمين", "Al-Khafid"),
    NameOfAllah("الرَّافِعُ", "الذي يرفع المؤمنين", "Ar-Rafi"),
    NameOfAllah("الْمُعِزُّ", "الذي يُعطي العزة", "Al-Mu'izz"),
    NameOfAllah("المُذِلُّ", "الذي يُذل من يشاء", "Al-Muzill"),
    NameOfAllah("السَّمِيعُ", "الذي لا يخفى عليه مسموع", "As-Sami"),
    NameOfAllah("الْبَصِيرُ", "الذي يبصر كل شيء", "Al-Baseer"),
    NameOfAllah("الْحَكَمُ", "القاضي بين خلقه", "Al-Hakam"),
    NameOfAllah("الْعَدْلُ", "العادل في حكمه", "Al-Adl"),
    NameOfAllah("اللَّطِيفُ", "الرفيق بعباده", "Al-Lateef"),
    NameOfAllah("الْخَبِيرُ", "العالم ببواطن الأمور", "Al-Khabeer"),
    NameOfAllah("الْحَلِيمُ", "الذي لا يعجل بالعقوبة", "Al-Haleem"),
    NameOfAllah("الْعَظِيمُ", "الجامع لصفات العظمة", "Al-Azeem"),
    NameOfAllah("الْغَفُورُ", "الذي يستر الذنوب", "Al-Ghafoor"),
    NameOfAllah("الشَّكُورُ", "الذي يشكر القليل من العمل", "Ash-Shakoor"),
    NameOfAllah("الْعَلِيُّ", "المتعالي عن كل نقص", "Al-Aliyy"),
    NameOfAllah("الْكَبِيرُ", "العظيم المتعالي", "Al-Kabeer"),
    NameOfAllah("الْحَفِيظُ", "المحفوظ لخلقه", "Al-Hafeedh"),
    NameOfAllah("المُقيت", "الموصي لكل مخلوق", "Al-Muqeet"),
    NameOfAllah("الْحسِيبُ", "الكافي لعباده", "Al-Haseeb"),
    NameOfAllah("الْجَلِيلُ", "ذو الجلال والعظمة", "Al-Jaleel"),
    NameOfAllah("الْكَرِيمُ", "الجواد المعطي", "Al-Kareem"),
    NameOfAllah("الرَّقِيبُ", "الحافظ الذي لا يغيب عنه شيء", "Ar-Raqeeb"),
    NameOfAllah("الْمُجِيبُ", "الذي يجيب دعوة الداعي", "Al-Mujeeb"),
    NameOfAllah("الْوَاسِعُ", "الواسع رحمة وعلماً", "Al-Wasi"),
    NameOfAllah("الْحَكِيمُ", "ذو الحكمة البالغة", "Al-Hakeem"),
    NameOfAllah("الْوَدُودُ", "المحب لعباده الصالحين", "Al-Wadud"),
    NameOfAllah("الْمَجِيدُ", "الشريف في ذاته", "Al-Majeed"),
    NameOfAllah("الْبَاعِثُ", "الذي يبعث الخلق", "Al-Ba'ith"),
    NameOfAllah("الشَّهِيدُ", "الشاهد على خلقه", "Ash-Shaheed"),
    NameOfAllah("الْحَقُّ", "الموجود حقيقة", "Al-Haqq"),
    NameOfAllah("الْوَكِيلُ", "الكفيل بأرزاق العباد", "Al-Wakeel"),
    NameOfAllah("الْقَوِيُّ", "الشديد القوة", "Al-Qawiyy"),
    NameOfAllah("الْمَتِينُ", "الشديد الذي لا يُرام", "Al-Mateen"),
    NameOfAllah("الْوَلِيُّ", "الناصر لعباده", "Al-Waliyy"),
    NameOfAllah("الْحَمِيدُ", "المحمود في كل حال", "Al-Hameed"),
    NameOfAllah("الْمُحْصِي", "الذي أحصى كل شيء عدداً", "Al-Muhsi"),
    NameOfAllah("الْمُبْدِئُ", "المبدأ للخلق", "Al-Mubdi"),
    NameOfAllah("الْمُعِيدُ", "المعيد للخلق بعد فنائهم", "Al-Mu'eed"),
    NameOfAllah("الْمُحْيِي", "المعطي للحياة", "Al-Muhyi"),
    NameOfAllah("اَلْمُمِيتُ", "الذي يميت الخلق", "Al-Mumeet"),
    NameOfAllah("الْحَيُّ", "الحي الذي لا يموت", "Al-Hayy"),
    NameOfAllah("الْقَيُّومُ", "القائم بنفسه", "Al-Qayyum"),
    NameOfAllah("الْوَاجِدُ", "الذي لا يعوزه شيء", "Al-Wajid"),
    NameOfAllah("الْمَاجِدُ", "الشريف الماجد", "Al-Majid"),
    NameOfAllah("الْواحِدُ", "الذي لا شريك له", "Al-Wahid"),
    NameOfAllah("اَلاَحَدُ", "الفرد المتفرد", "Al-Ahad"),
    NameOfAllah("الصَّمَدُ", "الذي يُقصد في الحوائج", "As-Samad"),
    NameOfAllah("الْقَادِرُ", "الذي لا يعجزه شيء", "Al-Qadir"),
    NameOfAllah("الْمُقْتَدِرُ", "البالغ في القدرة", "Al-Muqtadir"),
    NameOfAllah("الْمُقَدِّمُ", "الذي يقدم من يشاء", "Al-Muqaddim"),
    NameOfAllah("الْمُؤَخِّرُ", "الذي يؤخر من يشاء", "Al-Mu'akhkhir"),
    NameOfAllah("الأوَّلُ", "الذي ليس قبله شيء", "Al-Awwal"),
    NameOfAllah("الآخِرُ", "الذي ليس بعده شيء", "Al-Akhir"),
    NameOfAllah("الظَّاهِرُ", "الذي ظهر فوق كل شيء", "Az-Zahir"),
    NameOfAllah("الْبَاطِنُ", "الذي لا يُرى", "Al-Batin"),
    NameOfAllah("الْوَالِي", "المتصرف في خلقه", "Al-Wali"),
    NameOfAllah("الْمُتَعَالِي", "المتعالي عن صفات الخلق", "Al-Muta'ali"),
    NameOfAllah("الْبَرُّ", "المنعم على عباده", "Al-Barr"),
    NameOfAllah("التَّوَّابُ", "الذي يقبل التوبة", "At-Tawwab"),
    NameOfAllah("الْمُنْتَقِمُ", "الذي ينتقم من الظالمين", "Al-Muntaqim"),
    NameOfAllah("العَفُوُّ", "الذي يعفو عن الذنوب", "Al-Afuww"),
    NameOfAllah("الرَّؤُوفُ", "الشديد الرحمة", "Ar-Ra'uf"),
    NameOfAllah("مَالِكُ الْمُلْكِ", "المالك المتصرف في ملكه", "Malik-ul-Mulk"),
    NameOfAllah("ذُوالْجَلاَلِ وَالإكْرَامِ", "ذو العظمة والكرامة", "Dhul-Jalali wal-Ikram"),
    NameOfAllah("الْمُقْسِطُ", "العادل في قسمه", "Al-Muqsit"),
    NameOfAllah("الْجَامِعُ", "الجامع للخلق", "Al-Jami"),
    NameOfAllah("الْغَنِيُّ", "الذي لا يحتاج لشيء", "Al-Ghaniyy"),
    NameOfAllah("الْمُغْنِي", "الذي يُغني عباده", "Al-Mughni"),
    NameOfAllah("اَلْمَانِعُ", "الذي يمنع عن عباده الشُر", "Al-Mani"),
    NameOfAllah("الضَّارُّ", "الذي يضر من يشاء", "Ad-Darr"),
    NameOfAllah("النَّافِعُ", "الذي ينفع من يشاء", "An-Nafi"),
    NameOfAllah("النُّورُ", "المنور للسماوات والأرض", "An-Nur"),
    NameOfAllah("الْهَادِي", "الذي يهدي عباده", "Al-Hadi"),
    NameOfAllah("الْبَدِيعُ", "المبدع في خلقه", "Al-Badee"),
    NameOfAllah("اَلْبَاقِي", "الباقي الذي لا يفنى", "Al-Baqi"),
    NameOfAllah("الْوَارِثُ", "الباقي بعد فناء خلقه", "Al-Warith"),
    NameOfAllah("الرَّشِيدُ", "المرشد لعباده", "Ar-Rasheed"),
    NameOfAllah("الصَّبُورُ", "الذي لا يعجل", "As-Saboor")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamesOfAllahScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("أسماء الله الحسنى", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = QuranColors.Primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(QuranColors.Background)
                .padding(paddingValues),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranColors.PrimarySurface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا",
                            fontSize = 18.sp,
                            lineHeight = 32.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            color = QuranColors.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "سورة الأعراف - 180",
                            fontSize = 13.sp,
                            color = QuranColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${namesOfAllah.size} اسماً",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.Primary
                        )
                    }
                }
            }

            // Names grid - 2 columns displayed as pairs
            items(namesOfAllah.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { name ->
                        NameCard(
                            name = name,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun NameCard(
    name: NameOfAllah,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Number circle
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(QuranColors.Primary, QuranColors.PrimaryDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${namesOfAllah.indexOf(name) + 1}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic name
            Text(
                text = name.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = QuranColors.Primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Transliteration
            Text(
                text = name.transliteration,
                fontSize = 11.sp,
                color = QuranColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Meaning
            Text(
                text = name.meaning,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                color = QuranColors.TextPrimary,
                maxLines = 2
            )
        }
    }
}