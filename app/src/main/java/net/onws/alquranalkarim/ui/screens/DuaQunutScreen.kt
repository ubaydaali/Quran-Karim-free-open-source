package net.onws.alquranalkarim.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.onws.alquranalkarim.ui.theme.QuranColors

data class DuaItem(
    val arabic: String,
    val meaning: String
)

private val qunutDuas = listOf(
    DuaItem("اللَّهُمَّ اهْدِنِي فِيمَنْ هَدَيْتَ", "اللهم اهدني ممن هديت"),
    DuaItem("وَعَافِنِي فِيمَنْ عَافَيْتَ", "وعافني ممن عافيت"),
    DuaItem("وَتَوَلَّنِي فِيمَنْ تَوَلَّيْتَ", "وتولني ممن توليت"),
    DuaItem("وَبَارِكْ لِي فِيمَا أَعْطَيْتَ", "بارك لي فيما أعطيت"),
    DuaItem("وَقِنِي شَرَّ مَا قَضَيْتَ", "وقني شر ما قضيت"),
    DuaItem("إِنَّكَ تَقْضِي وَلَا يُقْضَى عَلَيْكَ", "إنك تقضي ولا يقضى عليك"),
    DuaItem("إِنَّهُ لَا يَذِلُّ مَنْ وَالَيْتَ", "إنه لا يذل من واليت"),
    DuaItem("وَلَا يَعِزُّ مَنْ عَادَيْتَ", "ولا يعز من عاديت"),
    DuaItem("تَبَارَكْتَ رَبَّنَا وَتَعَالَيْتَ", "تباركت ربنا وتعاليت"),
    DuaItem("فَلَكَ الْحَمْدُ عَلَى مَا قَضَيْتَ", "فللك الحمد على ما قضيت"),
    DuaItem("وَأَسْتَغْفِرُكَ وَأَتُوبُ إِلَيْكَ", "وأستغفرك وأتوب إليك"),
    DuaItem("وَصَلَّى اللَّهُ عَلَى النَّبِيِّ مُحَمَّدٍ ﷺ", "وصلّى الله على النبي محمد ﷺ")
)

private val otherDuas = listOf(
    DuaItem("رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ", "دعاء خاتمة القرآن"),
    DuaItem("رَبَّنَا لَا تُؤَاخِذْنَا إِنْ نَسِينَا أَوْ أَخْطَأْنَا", "دعاء أهل الكهف"),
    DuaItem("رَبَّنَا لَا تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا", "دعاء عدم زيغ القلوب"),
    DuaItem("رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي", "دعاء موسى عليه السلام"),
    DuaItem("لَا إِلَٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ", "دعاء يونس عليه السلام"),
    DuaItem("حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ", "دعاء التوكل على الله"),
    DuaItem("رَبِّ زِدْنِي عِلْمًا", "دعاء طلب العلم"),
    DuaItem("رَبِّ هَبْ لِي مِنْ لَدُنْكَ ذُرِّيَّةً طَيِّبَةً", "دعاء زكريا عليه السلام")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaQunutScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("دعاء القنوت", "أدعية قرآنية")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الأدعية", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QuranColors.Background)
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val duas = if (selectedTab == 0) qunutDuas else otherDuas

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
                            if (selectedTab == 0) {
                                Text(
                                    text = "دعاء القنوت",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranColors.Primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "يُقرأ في صلاة الوتر بعد الركوع",
                                    fontSize = 14.sp,
                                    color = QuranColors.TextSecondary
                                )
                            } else {
                                Text(
                                    text = "أدعية من القرآن الكريم",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranColors.Primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "أدعية مأثورة ذكرها الله في كتابه",
                                    fontSize = 14.sp,
                                    color = QuranColors.TextSecondary
                                )
                            }
                        }
                    }
                }

                itemsIndexed(duas) { index, dua ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 12.sp,
                                color = QuranColors.TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dua.arabic,
                                fontSize = 20.sp,
                                lineHeight = 36.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = QuranColors.TextPrimary,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dua.meaning,
                                fontSize = 14.sp,
                                color = QuranColors.TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}