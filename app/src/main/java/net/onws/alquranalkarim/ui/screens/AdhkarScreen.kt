package net.onws.alquranalkarim.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.*
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

data class Dhikr(
    val text: String,
    val repeat: Int,
    val source: String = ""
)

data class AdhkarCategory(
    val name: String,
    val icon: String,
    val adhkar: List<Dhikr>
)

fun getAllAdhkar(): List<AdhkarCategory> = listOf(
    AdhkarCategory(
        name = "أذكار الصباح",
        icon = "🌅",
        adhkar = listOf(
            Dhikr("أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", 1, "رواه أبو داود"),
            Dhikr("اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ وَإِلَيْكَ النُّشُورُ", 1, "رواه الترمذي"),
            Dhikr("اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَٰهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ", 1, "سيد الاستغفار - رواه البخاري"),
            Dhikr("بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ", 3, "رواه أبو داود والترمذي"),
            Dhikr("اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لاَ إِلَٰهَ إِلاَّ أَنْتَ", 3, "رواه أبو داود"),
            Dhikr("حَسْبِيَ اللَّهُ لاَ إِلَٰهَ إِلاَّ هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ", 7, "رواه أبو داود"),
            Dhikr("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ", 100, "من قالها مائة مرة حُطّت خطاياه ولو كانت مثل زبد البحر - رواه مسلم"),
            Dhikr("لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", 10, "كانت له عدل عشر رقاب - رواه مسلم"),
            Dhikr("أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", 100, "رواه البخاري ومسلم"),
            Dhikr("اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ", 1, "رواه أبو داود"),
            Dhikr("اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالآخِرَةِ", 1, "رواه ابن ماجه"),
            Dhikr("يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلاَ تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ", 1, "رواه الحاكم")
        )
    ),
    AdhkarCategory(
        name = "أذكار المساء",
        icon = "🌙",
        adhkar = listOf(
            Dhikr("أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", 1, "رواه أبو داود"),
            Dhikr("اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ وَإِلَيْكَ الْمَصِيرُ", 1, "رواه الترمذي"),
            Dhikr("أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ", 3, "رواه مسلم"),
            Dhikr("بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ", 3, "رواه أبو داود والترمذي"),
            Dhikr("اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لاَ إِلَٰهَ إِلاَّ أَنْتَ", 3, "رواه أبو داود"),
            Dhikr("حَسْبِيَ اللَّهُ لاَ إِلَٰهَ إِلاَّ هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ", 7, "رواه أبو داود"),
            Dhikr("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ", 100, "رواه مسلم"),
            Dhikr("لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", 10, "رواه مسلم"),
            Dhikr("أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", 100, "رواه البخاري ومسلم"),
            Dhikr("اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ", 1, "رواه أبو داود"),
            Dhikr("اللَّهُمَّ بِكَ أَمْسَيْنَا وَبِكَ أَصْبَحْنَا وَبِكَ نَحْيَا وَبِكَ نَمُوتُ وَإِلَيْكَ النُّشُورُ", 1, "رواه الترمذي"),
            Dhikr("أَمْسَيْنَا عَلَى فِطْرَةِ الإِسْلاَمِ، وَعَلَى كَلِمَةِ الإِخْلاَصِ، وَعَلَى دِينِ نَبِيِّنَا مُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ، وَعَلَى مِلَّةِ أَبِينَا إِبْرَاهِيمَ حَنِيفًا مُسْلِمًا وَمَا كَانَ مِنَ الْمُشْرِكِينَ", 1, "رواه أحمد")
        )
    ),
    AdhkarCategory(
        name = "أذكار بعد الصلاة",
        icon = "🕌",
        adhkar = listOf(
            Dhikr("أَسْتَغْفِرُ اللَّهَ (ثلاثاً) اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ", 1, "رواه مسلم"),
            Dhikr("لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، اللَّهُمَّ لاَ مَانِعَ لِمَا أَعْطَيْتَ، وَلاَ مُعْطِيَ لِمَا مَنَعْتَ، وَلاَ يَنْفَعُ ذَا الْجَدِّ مِنْكَ الْجَدُّ", 1, "متفق عليه"),
            Dhikr("سُبْحَانَ اللَّهِ (33 مرة) وَالْحَمْدُ لِلَّهِ (33 مرة) وَاللَّهُ أَكْبَرُ (33 مرة) لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", 1, "رواه مسلم"),
            Dhikr("اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ", 1, "رواه أبو داود والنسائي"),
            Dhikr("لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", 10, "كانت له عدل عشر رقاب - رواه مسلم"),
            Dhikr("سُبْحَانَ اللَّهِ", 33, "رواه مسلم"),
            Dhikr("الْحَمْدُ لِلَّهِ", 33, "رواه مسلم"),
            Dhikr("اللَّهُ أَكْبَرُ", 33, "رواه مسلم")
        )
    ),
    AdhkarCategory(
        name = "أذكار النوم",
        icon = "😴",
        adhkar = listOf(
            Dhikr("بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا", 1, "رواه البخاري"),
            Dhikr("اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ", 1, "رواه أبو داود والترمذي"),
            Dhikr("بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي وَبِكَ أَرْفَعُهُ، إِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ", 1, "متفق عليه"),
            Dhikr("اللَّهُمَّ بِاسْمِكَ أَحْيَا وَبِاسْمِكَ أَمُوتُ", 1, "رواه البخاري"),
            Dhikr("سُبْحَانَ اللَّهِ (33) وَالْحَمْدُ لِلَّهِ (33) وَاللَّهُ أَكْبَرُ (34)", 1, "رواه البخاري ومسلم"),
            Dhikr("اللَّهُمَّ خَلَقْتَ نَفْسِي وَأَنْتَ تَوَفَّاهَا، لَكَ مَمَاتُهَا وَمَحْيَاهَا، إِنْ أَحْيَيْتَهَا فَاحْفَظْهَا، وَإِنْ أَمَتَّهَا فَاغْفِرْ لَهَا، اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَافِيَةَ", 1, "رواه مسلم"),
            Dhikr("اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ", 3, "رواه أبو داود")
        )
    ),
    AdhkarCategory(
        name = "أذكار الاستيقاظ",
        icon = "⏰",
        adhkar = listOf(
            Dhikr("الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ", 1, "رواه البخاري"),
            Dhikr("لاَ إِلَٰهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلاَ إِلَٰهَ إِلاَّ اللَّهُ، وَاللَّهُ أَكْبَرُ، وَلاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ الْعَلِيِّ الْعَظِيمِ، رَبِّ اغْفِرْ لِي", 1, "رواه البخاري")
        )
    ),
    AdhkarCategory(
        name = "أذكار دخول المنزل",
        icon = "🏠",
        adhkar = listOf(
            Dhikr("بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى رَبِّنَا تَوَكَّلْنَا", 1, "رواه أبو داود"),
            Dhikr("اللَّهُمَّ إِنِّي أَسْأَلُكَ خَيْرَ الْمَوْلِجِ وَخَيْرَ الْمَخْرَجِ، بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى اللَّهِ رَبِّنَا تَوَكَّلْنَا", 1, "رواه أبو داود")
        )
    ),
    AdhkarCategory(
        name = "أذكار الخروج من المنزل",
        icon = "🚶",
        adhkar = listOf(
            Dhikr("بِسْمِ اللَّهِ، تَوَكَّلْتُ عَلَى اللَّهِ، وَلاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ", 1, "رواه أبو داود والترمذي"),
            Dhikr("اللَّهُمَّ إِنِّي أَعُوذُ بِكَ أَنْ أَضِلَّ أَوْ أُضَلَّ، أَوْ أَزِلَّ أَوْ أُزَلَّ، أَوْ أَظْلِمَ أَوْ أُظْلَمَ، أَوْ أَجْهَلَ أَوْ يُجْهَلَ عَلَيَّ", 1, "رواه الترمذي"),
            Dhikr("اللَّهُمَّ إِنِّي أَسْأَلُكَ خَيْرَ هَذَا الْيَوْمِ: فَتْحَهُ، وَنَصْرَهُ، وَنُورَهُ، وَبَرَكَتَهُ، وَهُدَاهُ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِيهِ وَشَرِّ مَا بَعْدَهُ", 1, "رواه أبو داود")
        )
    ),
    AdhkarCategory(
        name = "أذكار الطعام والشراب",
        icon = "🍽️",
        adhkar = listOf(
            Dhikr("بِسْمِ اللَّهِ", 1, "عند بداية الأكل - رواه أبو داود والترمذي"),
            Dhikr("اللَّهُمَّ بَارِكْ لَنَا فِيمَا رَزَقْتَنَا وَقِنَا عَذَابَ النَّارِ", 1, "عند الفراغ من الطعام - رواه مسلم"),
            Dhikr("الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَذَا وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلاَ قُوَّةٍ", 1, "رواه أبو داود والترمذي"),
            Dhikr("بِسْمِ اللَّهِ وَعَلَى بَرَكَةِ اللَّهِ", 1, "عند بداية الطعام")
        )
    ),
    AdhkarCategory(
        name = "أذكار السفر",
        icon = "✈️",
        adhkar = listOf(
            Dhikr("سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنقَلِبُونَ", 1, "رواه مسلم"),
            Dhikr("اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنقَلِبُونَ، اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى، وَمِنَ الْعَمَلِ مَا تَرْضَى، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا وَاطْوِ عَنَّا بُعْدَهُ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ، وَالْخَلِيفَةُ فِي الأَهْلِ", 1, "رواه مسلم"),
            Dhikr("أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ", 3, "عند النزول في مكان - رواه مسلم")
        )
    ),
    AdhkarCategory(
        name = "أذكار الهم والحزن",
        icon = "💙",
        adhkar = listOf(
            Dhikr("لاَ إِلَٰهَ إِلاَّ اللَّهُ الْعَظِيمُ الْحَلِيمُ، لاَ إِلَٰهَ إِلاَّ اللَّهُ رَبُّ الْعَرْشِ الْعَظِيمِ، لاَ إِلَٰهَ إِلاَّ اللَّهُ رَبُّ السَّمَوَاتِ وَرَبُّ الْأَرْضِ وَرَبُّ الْعَرْشِ الْكَرِيمِ", 1, "متفق عليه"),
            Dhikr("اللَّهُمَّ رَحْمَتَكَ أَرْجُو فَلاَ تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ، وَأَصْلِحْ لِي شَأْنِي كُلَّهُ، لاَ إِلَٰهَ إِلاَّ أَنْتَ", 1, "رواه أبو داود"),
            Dhikr("اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ", 1, "رواه أبو داود"),
            Dhikr("يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ", 1, "رواه الترمذي"),
            Dhikr("حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ", 7, "من قالها سبع مرات كفاه الله ما أهمه - راه أحمد وأبو داود"),
            Dhikr("اللَّهُمَّ إِنِّي عَبْدُكَ، ابْنُ عَبْدِكَ، ابْنُ أَمَتِكَ، نَاصِيَتِي بِيَدِكَ، مَاضٍ فِيَّ حُكْمُكَ، عَدْلٌ فِيَّ قَضَاؤُكَ، أَسْأَلُكَ بِكُلِّ اسْمٍ هُوَ لَكَ، سَمَّيْتَ بِهِ نَفْسَكَ، أَوْ عَلَّمْتَهُ أَحَدًا مِنْ خَلْقِكَ، أَوْ أَنْزَلْتَهُ فِي كِتَابِكَ، أَوِ اسْتَأْثَرْتَ بِهِ فِي عِلْمِ الْغَيْبِ عِنْدَكَ، أَنْ تَجْعَلَ الْقُرْآنَ رَبِيعَ قَلْبِي، وَنُورَ صَدْرِي، وَجَلاَءَ حُزْنِي، وَذَهَابَ هَمِّي", 1, "رواه أحمد")
        )
    ),
    AdhkarCategory(
        name = "أذكار متنوعة",
        icon = "📿",
        adhkar = listOf(
            Dhikr("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ", 100, "كلمتان حبيبتان إلى الرحمن، خفيفتان على اللسان، ثقيلتان في الميزان - رواه البخاري ومسلم"),
            Dhikr("لاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ", 100, "كنز من كنوز الجنة - رواه البخاري ومسلم"),
            Dhikr("اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ", 10, "من صلى عليّ حين يُصلي صلاة صلى الله عليه بها عشراً - رواه مسلم"),
            Dhikr("أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لاَ إِلَٰهَ إِلاَّ هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ", 3, "من قالها غُفرت ذنوبه وإن كان فارًّا من الزحف - رواه أبو داود والترمذي"),
            Dhikr("اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا، وَلاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ، وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ", 1, "متفق عليه"),
            Dhikr("رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ", 100, "كان النبي ﷺ يستغفر الله في اليوم أكثر من سبعين مرة - رواه البخاري"),
            Dhikr("يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ", 3, "رواه الترمذي"),
            Dhikr("اللَّهُمَّ مُصَرِّفَ الْقُلُوبِ صَرِّفْ قُلُوبَنَا عَلَى طَاعَتِكَ", 3, "رواه مسلم")
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdhkarScreen(
    onBackClick: () -> Unit
) {
    val categories = remember { getAllAdhkar() }
    var expandedCategory by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "أذكار المسلم",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "أذكار المسلم اليومية",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuranColors.GoldLight.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = QuranColors.Primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(QuranColors.Background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(categories) { index, category ->
                    val isExpanded = expandedCategory == index
                    AdhkarCategoryCard(
                        category = category,
                        isExpanded = isExpanded,
                        index = index,
                        onClick = {
                            expandedCategory = if (isExpanded) -1 else index
                        }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun AdhkarCategoryCard(
    category: AdhkarCategory,
    isExpanded: Boolean,
    index: Int,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400, delayMillis = index * 50)) +
                slideInHorizontally(
                    initialOffsetX = { 50 },
                    animationSpec = tween(400, delayMillis = index * 50)
                )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(300, easing = CubicBezierEasing(0.33f, 1f, 0.68f, 1f))
                ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isExpanded) 6.dp else 2.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isExpanded) QuranColors.Surface else QuranColors.Surface
            ),
            border = if (isExpanded) BorderStroke(1.5.dp, QuranColors.Gold.copy(alpha = 0.3f)) else null
        ) {
            Column {
                // Category header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onClick)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon with gradient background
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isExpanded) QuranColors.Primary else QuranColors.PrimarySurface,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = category.icon,
                                fontSize = 26.sp
                            )
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = QuranColors.TextPrimary
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = "${category.adhkar.size} أذكار",
                            fontSize = 13.sp,
                            color = QuranColors.TextTertiary
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isExpanded)
                            QuranColors.Primary.copy(alpha = 0.1f)
                        else QuranColors.SurfaceVariant
                    ) {
                        Icon(
                            if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).size(22.dp),
                            tint = if (isExpanded) QuranColors.Primary else QuranColors.TextTertiary
                        )
                    }
                }

                // Expanded adhkar list
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(tween(300)) + expandVertically(tween(400)),
                    exit = fadeOut(tween(200)) + shrinkVertically(tween(300))
                ) {
                    Column {
                        // Divider with gold accent
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(1.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            QuranColors.Gold.copy(alpha = 0.3f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        category.adhkar.forEachIndexed { dhikrIndex, dhikr ->
                            DhikrItem(dhikr = dhikr, index = dhikrIndex + 1)
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DhikrItem(dhikr: Dhikr, index: Int) {
    var count by remember { mutableIntStateOf(dhikr.repeat) }
    val isCompleted = count == 0
    val progress = 1f - (count.toFloat() / dhikr.repeat.toFloat())

    val bgColor by animateColorAsState(
        targetValue = if (isCompleted)
            QuranColors.Primary.copy(alpha = 0.06f)
        else QuranColors.Surface,
        tween(300), label = "bg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Dhikr text
            Text(
                text = dhikr.text,
                fontSize = 20.sp,
                lineHeight = 38.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                color = if (isCompleted)
                    QuranColors.TextSecondary.copy(alpha = 0.6f)
                else QuranColors.TextPrimary
            )

            Spacer(Modifier.height(10.dp))

            // Source
            if (dhikr.source.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = QuranColors.GoldSurface.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "📗 ${dhikr.source}",
                        fontSize = 12.sp,
                        color = QuranColors.GoldDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        textAlign = TextAlign.End
                    )
                }
                Spacer(Modifier.height(10.dp))
            }

            // Counter section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Count display
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCompleted)
                        QuranColors.Primary.copy(alpha = 0.12f)
                    else QuranColors.PrimarySurface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isCompleted) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = QuranColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "تم",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = QuranColors.Primary
                            )
                        } else if (dhikr.repeat > 1) {
                            Text(
                                text = "$count",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = QuranColors.Primary
                            )
                            Text(
                                text = " / ${dhikr.repeat}",
                                fontSize = 14.sp,
                                color = QuranColors.TextTertiary
                            )
                        } else {
                            Text(
                                text = "مرة واحدة",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = QuranColors.TextSecondary
                            )
                        }
                    }
                }

                // Tap to count button
                if (!isCompleted) {
                    Surface(
                        onClick = { if (count > 0) count-- },
                        shape = RoundedCornerShape(14.dp),
                        color = QuranColors.DhikrButton,
                        shadowElevation = if (!isCompleted) 2.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.TouchApp,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                            Text(
                                "تسبيح",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            // Progress bar
            if (dhikr.repeat > 1) {
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isCompleted) QuranColors.Primary else QuranColors.Gold,
                    trackColor = QuranColors.Primary.copy(alpha = 0.08f)
                )
            }
        }
    }
}