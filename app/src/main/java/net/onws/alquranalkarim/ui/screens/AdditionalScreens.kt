package net.onws.alquranalkarim.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.onws.alquranalkarim.ui.theme.QuranColors

// ==================== Topic Index Screen ====================
data class QuranTopic(
    val name: String,
    val icon: String,
    val color: Color,
    val subtopics: List<TopicReference>
)

data class TopicReference(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val description: String
)

private val quranTopics = listOf(
    QuranTopic("العبادات", "🕌", QuranColors.Primary,
        listOf(
            TopicReference(2, "البقرة", 43, "أقيموا الصلاة"),
            TopicReference(2, "البقرة", 110, "أقيموا الصلاة والزكاة"),
            TopicReference(4, "النساء", 103, "الصلاة وسطات"),
            TopicReference(11, "هود", 114, "أقم الصلاة"),
            TopicReference(17, "الإسراء", 78, "الصلاة الوسطى"),
            TopicReference(29, "العنكبوت", 45, "إن الصلاة تنهى عن الفحشاء"),
            TopicReference(2, "البقرة", 183, "فرض الصيام"),
            TopicReference(2, "البقرة", 267, "الزكاة"),
            TopicReference(22, "الحج", 27, "الحج"),
            TopicReference(9, "التوبة", 36, "الحج أشهر معلومات")
        )
    ),
    QuranTopic("الأخلاق", "💎", QuranColors.GoldDark,
        listOf(
            TopicReference(17, "الإسراء", 23, "الإحسان للوالدين"),
            TopicReference(31, "لقمان", 13, "لا تشرك بالله"),
            TopicReference(49, "الحجرات", 11, "لا يسخر قوم"),
            TopicReference(49, "الحجرات", 12, "اجتناب الظن"),
            TopicReference(2, "البقرة", 263, "القول المعروف"),
            TopicReference(3, "آل عمران", 134, "الكاظمين الغيظ"),
            TopicReference(4, "النساء", 135, "العدل"),
            TopicReference(7, "الأعراف", 199, "العفو والأمر بالمعروف"),
            TopicReference(16, "النحل", 90, "العدل والإحسان"),
            TopicReference(23, "المؤمنون", 1, "المفلحون")
        )
    ),
    QuranTopic("القصص", "📖", QuranColors.MeccanText,
        listOf(
            TopicReference(2, "البقرة", 30, "قصة آدم عليه السلام"),
            TopicReference(2, "البقرة", 60, "قصة موسى عليه السلام"),
            TopicReference(5, "المائدة", 27, "قصة هابيل وقابيل"),
            TopicReference(12, "يوسف", 4, "قصة يوسف عليه السلام"),
            TopicReference(18, "الكهف", 9, "أصحاب الكهف"),
            TopicReference(18, "الكهف", 60, "قصة موسى والخضر"),
            TopicReference(18, "الكهف", 83, "ذو القرنين"),
            TopicReference(19, "مريم", 16, "قصة مريم عليه السلام"),
            TopicReference(20, "طه", 9, "قصة موسى"),
            TopicReference(21, "الأنبياء", 69, "قصة إبراهيم"),
            TopicReference(27, "النمل", 15, "قصة داوود وسليمان"),
            TopicReference(28, "القصص", 3, "قصة فرعون وموسى")
        )
    ),
    QuranTopic("الأحكام", "⚖️", QuranColors.MedinanText,
        listOf(
            TopicReference(2, "البقرة", 228, "أحكام الطلاق"),
            TopicReference(2, "البقرة", 282, "الديون والمعاملات"),
            TopicReference(4, "النساء", 3, "أحكام الزواج"),
            TopicReference(4, "النساء", 11, "المواريث"),
            TopicReference(4, "النساء", 24, "المحرمات"),
            TopicReference(4, "النساء", 58, "العدل في الحكم"),
            TopicReference(5, "المائدة", 1, "المحرمات من الطعام"),
            TopicReference(5, "المائدة", 3, "الخمر والميسر"),
            TopicReference(5, "المائدة", 38, "السرقة"),
            TopicReference(24, "النور", 2, "الزنا"),
            TopicReference(24, "النور", 31, "الحجاب")
        )
    ),
    QuranTopic("العقيدة", "🌟", QuranColors.GoldDark,
        listOf(
            TopicReference(2, "البقرة", 163, "توحيد الله"),
            TopicReference(2, "البقرة", 255, "آية الكرسي"),
            TopicReference(3, "آل عمران", 18, "شهادة أن لا إله إلا الله"),
            TopicReference(112, "الإخلاص", 1, "سورة الإخلاص"),
            TopicReference(2, "البقرة", 4, "الإيمان بالغيب"),
            TopicReference(2, "البقرة", 285, "الإيمان بالرسل"),
            TopicReference(4, "النساء", 136, "الإيمان بالكتب"),
            TopicReference(71, "نوح", 1, "قصة نوح"),
            TopicReference(23, "المؤمنون", 23, "قصة نوح"),
            TopicReference(36, "يس", 78, "البعث")
        )
    ),
    QuranTopic("الدعوة", "📢", QuranColors.Primary,
        listOf(
            TopicReference(3, "آل عمران", 104, "الدعوة إلى الخير"),
            TopicReference(3, "آل عمران", 110, "أمة وسط"),
            TopicReference(16, "النحل", 125, "ادع إلى سبيل ربك"),
            TopicReference(41, "فصلت", 33, "الدعوة بالحكمة"),
            TopicReference(103, "العصر", 1, "سورة العصر"),
            TopicReference(14, "إبراهيم", 24, "مثلاً كلمة طيبة"),
            TopicReference(2, "البقرة", 143, "شهادة على الناس"),
            TopicReference(22, "الحج", 78, "جاهدوا في الله")
        )
    ),
    QuranTopic("الصبر والابتلاء", "💪", QuranColors.MedinanText,
        listOf(
            TopicReference(2, "البقرة", 153, "استعينوا بالصبر"),
            TopicReference(2, "البقرة", 155, "الابتلاء"),
            TopicReference(3, "آل عمران", 186, "الابتلاء بالأموال"),
            TopicReference(12, "يوسف", 18, "صبر يوسف"),
            TopicReference(16, "النحل", 96, "أجر الصابرين"),
            TopicReference(38, "ص", 44, "صبر أيوب"),
            TopicReference(68, "القلم", 48, "صبر يونس"),
            TopicReference(90, "البلد", 4, "العسر واليسر"),
            TopicReference(94, "الشرح", 5, "مع العسر يسرا")
        )
    ),
    QuranTopic("العلم", "📚", QuranColors.MeccanText,
        listOf(
            TopicReference(96, "العلق", 1, "اقرأ"),
            TopicReference(2, "البقرة", 31, "علم آدم الأسماء"),
            TopicReference(20, "طه", 114, "وقل رب زدني علماً"),
            TopicReference(29, "العنكبوت", 49, "آيات الله"),
            TopicReference(35, "فاطر", 28, "خشيته العلماء"),
            TopicReference(39, "الزمر", 9, "هل يستوي العلماء"),
            TopicReference(58, "المجادلة", 11, "يرفع الله الذين آمنوا")
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicIndexScreen(
    onBackClick: () -> Unit,
    onNavigateToAyah: (Int, String, Int) -> Unit
) {
    var selectedTopic by remember { mutableStateOf<QuranTopic?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("فهرس الموضوعات", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("ابحث حسب الموضوع", fontSize = 14.sp, color = QuranColors.GoldLight)
                        }
                    }
                }
            }

            if (selectedTopic == null) {
                // Topics Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(quranTopics) { topic ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTopic = topic },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(topic.icon, fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    topic.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = topic.color,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${topic.subtopics.size} مرجع",
                                    fontSize = 12.sp,
                                    color = QuranColors.TextTertiary
                                )
                            }
                        }
                    }
                }
            } else {
                // Subtopics List
                val currentTopic = selectedTopic ?: return
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { selectedTopic = null }) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = QuranColors.Primary)
                        }
                        Text(
                            currentTopic.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.Primary
                        )
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentTopic.subtopics) { ref ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onNavigateToAyah(ref.surahNumber, ref.surahName, ref.ayahNumber)
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = QuranColors.PrimarySurface,
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                "${ref.surahNumber}:${ref.ayahNumber}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = QuranColors.Primary
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            ref.description,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = QuranColors.TextPrimary
                                        )
                                        Text(
                                            "سورة ${ref.surahName} - الآية ${ref.ayahNumber}",
                                            fontSize = 13.sp,
                                            color = QuranColors.TextTertiary
                                        )
                                    }
                                    Icon(
                                        Icons.Filled.ChevronLeft,
                                        contentDescription = null,
                                        tint = QuranColors.TextTertiary
                                    )
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

// ==================== Page Navigator Screen ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageNavigatorScreen(
    onBackClick: () -> Unit,
    onNavigateToPage: (Int) -> Unit
) {
    var pageInput by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("الانتقال لصفحة", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("٦٠٤ صفحات المصحف الشريف", fontSize = 14.sp, color = QuranColors.GoldLight)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page input
                OutlinedTextField(
                    value = pageInput,
                    onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) pageInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("رقم الصفحة") },
                    placeholder = { Text("مثال: 1") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Go
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = QuranColors.Primary,
                        cursorColor = QuranColors.Primary
                    ),
                    supportingText = {
                        Text("من 1 إلى 604", fontSize = 12.sp, color = QuranColors.TextTertiary)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quick page buttons
                Text("انتقال سريع", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = QuranColors.TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))

                val quickPages = listOf(
                    "المصحف كله" to 1,
                    "البقرة" to 2,
                    "آل عمران" to 50,
                    "النساء" to 77,
                    "المائدة" to 106,
                    "الأنعام" to 128,
                    "الأعراف" to 151,
                    "التوبة" to 187,
                    "يوسف" to 226,
                    "الكهف" to 249,
                    "مريم" to 280,
                    "النور" to 322,
                    "يس" to 440,
                    "الرحمن" to 534,
                    "الملك" to 562,
                    "الإخلاص" to 602
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickPages) { (name, page) ->
                        Surface(
                            modifier = Modifier.clickable {
                                pageInput = page.toString()
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = QuranColors.Surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, QuranColors.Primary.copy(alpha = 0.2f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = QuranColors.TextPrimary,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "صف $page",
                                    fontSize = 12.sp,
                                    color = QuranColors.TextTertiary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val page = pageInput.toIntOrNull()
                        if (page != null && page in 1..604) {
                            onNavigateToPage(page)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuranColors.Primary),
                    enabled = pageInput.toIntOrNull()?.let { it in 1..604 } == true
                ) {
                    Icon(Icons.Filled.OpenInBrowser, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("انتقل للصفحة", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==================== Prophet Stories Screen ====================
data class ProphetStory(
    val name: String,
    val arabicName: String,
    val surahs: List<Pair<Int, String>>,
    val summary: String,
    val icon: String
)

private val prophetStories = listOf(
    ProphetStory("Adam", "آدم عليه السلام",
        listOf(2 to "البقرة:30", 5 to "المائدة:27", 7 to "الأعراف:19", 15 to "الحجر:26", 17 to "الإسراء:61", 20 to "طه:115", 38 to "ص:71"),
        "أول البشر وأبو البشرية، خلقه الله بيده ونفخ فيه من روحه وأسجد له الملائكة. علّمه الله الأسماء كلها.",
        "🌱"),
    ProphetStory("Nuh", "نوح عليه السلام",
        listOf(7 to "الأعراف:59", 10 to "يونس:71", 11 to "هود:25", 23 to "المؤمنون:23", 26 to "الشعراء:105", 71 to "نوح:1"),
        "أرسله الله إلى قومه فدعاهم ألف سنة إلا خمسين عاماً. بنى السفينة بأمر الله ونجّاه الله من الطوفان.",
        "🚢"),
    ProphetStory("Ibrahim", "إبراهيم عليه السلام",
        listOf(2 to "البقرة:124", 3 to "آل عمران:67", 4 to "النساء:125", 6 to "الأنعام:74", 14 to "إبراهيم:35", 19 to "مريم:41", 21 to "الأنبياء:51", 26 to "الشعراء:69", 29 to "العنكبوت:16", 37 to "الصافات:83", 43 to "الزخرف:26"),
        "خليل الرحمن، حطّم الأصنام ودعا قومه للتوحيد. امتحنه الله بأحب الناس إليه فصبر وأذعن.",
        "🔥"),
    ProphetStory("Ismail", "إسماعيل عليه السلام",
        listOf(2 to "البقرة:125", 6 to "الأنعام:86", 19 to "مريم:54", 37 to "الصافات:101", 38 to "ص:48"),
        "ابن إبراهيم الخليل، ذبحه أبوه في منامه ففداه الله بذبح عظيم. كان صبوراً موفقاً بالعهود.",
        "🐑"),
    ProphetStory("Ishaq", "إسحاق عليه السلام",
        listOf(2 to "البقرة:133", 6 to "الأنعام:84", 11 to "هود:71", 12 to "يوسف:6", 19 to "مريم:49", 29 to "العنكبوت:27", 37 to "الصافات:112", 38 to "ص:45"),
        "ابن إبراهيم ووالد يعقوب، بشره الله الملائكة بغلام عليم. من الأنبياء أولي العزم.",
        "⭐"),
    ProphetStory("Yaqub", "يعقوب عليه السلام",
        listOf(2 to "البقرة:132", 3 to "آل عمران:84", 6 to "الأنعام:84", 12 to "يوسف:4", 19 to "مريم:49", 29 to "العنكبوت:27"),
        "أبو الأسباط الاثني عشر، صبر على فراق يوسف سنين طويلة حتى اجتمع بهم. سُمي إسرائيل.",
        "👨‍👦"),
    ProphetStory("Yusuf", "يوسف عليه السلام",
        listOf(12 to "يوسف:4"),
        "ابن يعقوب، رُمي في الجب ثم ابتاعه عزيز مصر. فُتن بزليخا فعصمّه الله. أصبح عزيز مصر.",
        "👑"),
    ProphetStory("Musa", "موسى عليه السلام",
        listOf(2 to "البقرة:49", 5 to "المائدة:20", 7 to "الأعراف:103", 10 to "يونس:75", 18 to "الكهف:60", 20 to "طه:9", 26 to "الشعراء:10", 28 to "القصص:3", 40 to "غافر:23", 44 to "الدخان:17"),
        "كليم الله، أرسله الله إلى فرعون. ألقى عصاه فتحولت ثعباناً. فلق البحر وأنجاه الله من فرعون.",
        "🌊"),
    ProphetStory("Dawud", "داوود عليه السلام",
        listOf(2 to "البقرة:251", 4 to "النساء:163", 5 to "المائدة:78", 6 to "الأنعام:84", 17 to "الإسراء:55", 21 to "الأنبياء:78", 27 to "النمل:15", 34 to "سبأ:10", 38 to "ص:17"),
        "ملك ونبي، أنزل الله عليه الزبور. سخّر الله له الجبال والطير. كان تواباً.",
        "📖"),
    ProphetStory("Sulayman", "سليمان عليه السلام",
        listOf(2 to "البقرة:102", 4 to "النساء:163", 6 to "الأنعام:84", 21 to "الأنبياء:78", 27 to "النمل:15", 34 to "سبأ:12", 38 to "ص:30"),
        "ملك عظيم سخّر الله له الريح والجن والطير. فهم خطاب الهدهد والنمل. بنى الهيكل.",
        "🦅"),
    ProphetStory("Ayyub", "أيوب عليه السلام",
        listOf(4 to "النساء:163", 6 to "الأنعام:84", 21 to "الأنبياء:83", 38 to "ص:41"),
        "ابتلاه الله في جسده وأهله وماله فصبر صبراً جميلاً. شفاه الله وأعاد له أهله وأضعاف ما فقد.",
        "💪"),
    ProphetStory("Yunus", "يونس عليه السلام",
        listOf(4 to "النساء:163", 6 to "الأنعام:86", 10 to "يونس:98", 21 to "الأنبياء:87", 37 to "الصافات:139", 68 to "القلم:48"),
        "أرسله الله إلى أهل نينوى فكذّبوه. غادر فركب السفينة فالتقمه الحوت. تاب فأنبذه الله سليماً.",
        "🐋"),
    ProphetStory("Isa", "عيسى عليه السلام",
        listOf(2 to "البقرة:87", 3 to "آل عمران:45", 4 to "النساء:157", 5 to "المائدة:46", 19 to "مريم:16", 43 to "الزخرف:57", 61 to "الصف:6"),
        "كلمة الله وروح منه، ولدته مريم معصوماً. نفخ فيه فكان بإذن الله. أحياء الموتى بإذن الله.",
        "✨"),
    ProphetStory("Muhammad", "محمد ﷺ",
        listOf(33 to "الأحزاب:40", 47 to "محمد:2", 48 to "الفتح:29", 97 to "القدر:1", 96 to "العلق:1"),
        "خاتم الأنبياء والمرسلين، أُرسل رحمة للعالمين. أنزل عليه القرآن. أتمّ الله به الدين.",
        "🌙")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProphetStoriesScreen(
    onBackClick: () -> Unit,
    onNavigateToAyah: (Int, String, Int) -> Unit
) {
    var selectedStory by remember { mutableStateOf<ProphetStory?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (selectedStory != null) selectedStory = null else onBackClick()
                        }) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                if (selectedStory != null) selectedStory!!.arabicName else "قصص الأنبياء",
                                fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White
                            )
                            Text(
                                if (selectedStory != null) "المراجع في القرآن" else "الأنبياء المذكورون في القرآن",
                                fontSize = 14.sp, color = QuranColors.GoldLight
                            )
                        }
                    }
                }
            }

            if (selectedStory == null) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(prophetStories) { story ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStory = story },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = QuranColors.PrimarySurface,
                                    modifier = Modifier.size(52.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(story.icon, fontSize = 28.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        story.arabicName,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranColors.TextPrimary
                                    )
                                    Text(
                                        "${story.surahs.size} مرجع في القرآن",
                                        fontSize = 13.sp,
                                        color = QuranColors.TextTertiary
                                    )
                                }
                                Icon(
                                    Icons.Filled.ChevronLeft,
                                    contentDescription = null,
                                    tint = QuranColors.TextTertiary
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Summary card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.PrimarySurface)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    "نبذة عن النبي",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranColors.Primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    selectedStory!!.summary,
                                    fontSize = 15.sp,
                                    lineHeight = 28.sp,
                                    color = QuranColors.TextPrimary
                                )
                            }
                        }
                    }

                    // References header
                    item {
                        Text(
                            "المراجع في القرآن الكريم",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.Primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(selectedStory!!.surahs) { (surahNum, ref) ->
                        val parts = ref.split(":")
                        val surahName = parts[0]
                        val ayahNum = parts.getOrNull(1)?.toIntOrNull() ?: 1

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToAyah(surahNum, surahName, ayahNum) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = QuranColors.PrimarySurface,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            "$surahNum",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = QuranColors.Primary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "سورة $surahName",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = QuranColors.TextPrimary
                                    )
                                    Text(
                                        "الآية $ayahNum",
                                        fontSize = 13.sp,
                                        color = QuranColors.TextTertiary
                                    )
                                }
                                Icon(
                                    Icons.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = QuranColors.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

// ==================== Fiqh Screen ====================
data class FiqhTopic(
    val title: String,
    val icon: String,
    val sections: List<FiqhSection>
)

data class FiqhSection(
    val title: String,
    val content: String
)

private val fiqhTopics = listOf(
    FiqhTopic("الوضوء", "💧", listOf(
        FiqhSection("الفرائض", "غسل الوجه، غسل اليدين إلى المرفقين، مسح الرأس، غسل الرجلين إلى الكعبين. قال تعالى: {يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى الصَّلَاةِ فَاغْسِلُوا وُجُوهَكُمْ وَأَيْدِيَكُمْ إِلَى الْمَرَافِقِ وَامْسَحُوا بِرُءُوسِكُمْ وَأَرْجُلَكُمْ إِلَى الْكَعْبَيْنِ}"),
        FiqhSection("السنن", "التسمية، غسل الكفين ثلاثاً، المضمضة، الاستنشاق، تخليل اللحية، تخليل أصابع الرجلين، الترتيب، الموالاة، الدعاء بعده."),
        FiqhSection("النواقض", "الحدث من بول أو غائط، نوم مستغرق، أكل لحم الإبل، زوال العقل بجنون أو إغماء أو سكر، مس الفرج بلا حائل.")
    )),
    FiqhTopic("الصلاة", "🕌", listOf(
        FiqhSection("شروط الصلاة", "الإسلام، العقل، التمييز، رفع الحدث، سورة العورة، استقبال القبلة، دخول الوقت، النية."),
        FiqhSection("أركان الصلاة", "القيام مع القدرة، تكبيرة الإحرام، قراءة الفاتحة، الركوع، الاعتدال من الركوع، السجود، الاعتدال من السجود، التشهد الأخير، الجلوس للتشهد، الصلاة على النبي ﷺ، التسليم."),
        FiqhSection("واجبات الصلاة", "تكبيرة غير الإحرام، قول سمع الله لمن حمده، قول ربنا ولك الحمد، التسبيح في الركوع (سبحان ربي العظيم)، التسبيح في السجود (سبحان ربي الأعلى)، قول رب اغفر لي بين السجدتين، التشهد الأول، الجلوس للتشهد الأول.")
    )),
    FiqhTopic("الزكاة", "💰", listOf(
        FiqhSection("شروط وجوب الزكاة", "الإسلام، الحرية، ملك نصاب، تمام الحول، تمام الملك."),
        FiqhSection("نصاب الذهب والفضة", "الذهب: 85 جراماً (20 مثقال). الفضة: 595 جراماً (200 درهم). النسبة: 2.5% من المال."),
        FiqhSection("زكاة الأنعام", "الإبل والبقر والغنم حسب جداول محددة في الشرع. لا تجب في أقل من 5 من الإبل و30 من البقر و40 من الغنم.")
    )),
    FiqhTopic("الصيام", "🌙", listOf(
        FiqhSection("شروط وجوب الصيام", "الإسلام، البلوغ، العقل، القدرة، الإقامة (المسافر يرخص له)."),
        FiqhSection("أركان الصيام", "النية من الليل (فرض في صيام الفجر)، الإمساك عن المفطرات من طلوع الفجر إلى غروب الشمس."),
        FiqhSection("المفطرات", "الأكل والشرب عمداً، القيء عمداً، الجماع، إنزال المني بالاستمناء، الحجامة (على خلاف)، خروج دم الحيض والنفاس.")
    )),
    FiqhTopic("الحج", "🕋", listOf(
        FiqhSection("شروط وجوب الحج", "الإسلام، البلوغ، العقل، الحرية، الاستطاعة (مال وصحة وأمن الطريق)."),
        FiqhSection("أركان الحج", "الإحرام، الوقوف بعرفة، طواف الإفاضة، السعي بين الصفا والمروة."),
        FiqhSection("واجبات الحج", "الإحرام من الميقات، الوقوف بعرفة إلى غروب الشمس، المبيت بمزدلفة، المبيت بمنى ليالي أيام التشريق، رمي الجمرات، الحلق أو التقصير، طواف الوداع.")
    ))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiqhScreen(
    onBackClick: () -> Unit
) {
    var selectedTopic by remember { mutableStateOf<FiqhTopic?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (selectedTopic != null) selectedTopic = null else onBackClick()
                        }) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                if (selectedTopic != null) selectedTopic!!.title else "فقه العبادات",
                                fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White
                            )
                            Text(
                                if (selectedTopic != null) "ملخص فقهي مبسط" else "ملخصات فقهية مبسطة",
                                fontSize = 14.sp, color = QuranColors.GoldLight
                            )
                        }
                    }
                }
            }

            if (selectedTopic == null) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(fiqhTopics) { topic ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTopic = topic },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = QuranColors.PrimarySurface,
                                    modifier = Modifier.size(52.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(topic.icon, fontSize = 28.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        topic.title,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranColors.TextPrimary
                                    )
                                    Text(
                                        "${topic.sections.size} أقسام",
                                        fontSize = 13.sp,
                                        color = QuranColors.TextTertiary
                                    )
                                }
                                Icon(
                                    Icons.Filled.ChevronLeft,
                                    contentDescription = null,
                                    tint = QuranColors.TextTertiary
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(selectedTopic!!.sections) { section ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    section.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranColors.Primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    section.content,
                                    fontSize = 15.sp,
                                    lineHeight = 28.sp,
                                    color = QuranColors.TextPrimary
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

// ==================== Memorization Hub Screen ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemorizationHubScreen(
    onBackClick: () -> Unit,
    onPlanClick: () -> Unit,
    onTestClick: () -> Unit,
    onReviewClick: () -> Unit,
    onProgressClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("مركز الحفظ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("خطط واختبر وراجع", fontSize = 14.sp, color = QuranColors.GoldLight)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Plan
                MemorizationHubCard(
                    icon = Icons.Filled.DateRange,
                    title = "خطة الحفظ",
                    description = "ضع خطة حفظ يومية أو أسبوعية مع تنبيهات",
                    color = QuranColors.Primary,
                    onClick = onPlanClick
                )

                // Test
                MemorizationHubCard(
                    icon = Icons.Filled.Quiz,
                    title = "اختبار الحفظ",
                    description = "اختبر نفسك بإخفاء بعض الكلمات",
                    color = QuranColors.GoldDark,
                    onClick = onTestClick
                )

                // Review
                MemorizationHubCard(
                    icon = Icons.Filled.Replay,
                    title = "المراجعات المجدولة",
                    description = "نظام مراجعات متباعدة مثل Anki",
                    color = QuranColors.MedinanText,
                    onClick = onReviewClick
                )

                // Progress
                MemorizationHubCard(
                    icon = Icons.Filled.PieChart,
                    title = "متابعة التقدم",
                    description = "نسبة الإنجاز لكل سورة مع تقدم مرئي",
                    color = QuranColors.MeccanText,
                    onClick = onProgressClick
                )
            }
        }
    }
}

@Composable
private fun MemorizationHubCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = QuranColors.TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, fontSize = 13.sp, color = QuranColors.TextTertiary)
            }
            Icon(Icons.Filled.ChevronLeft, contentDescription = null, tint = QuranColors.TextTertiary)
        }
    }
}

// ==================== Memorization Plan Screen ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemorizationPlanScreen(
    onBackClick: () -> Unit,
    bookmarkManager: net.onws.alquranalkarim.data.local.BookmarkManager,
    onNavigateToAyah: (Int, String, Int) -> Unit
) {
    var plans by remember { mutableStateOf(bookmarkManager.getMemorizationPlans()) }
    var showNewPlanDialog by remember { mutableStateOf(false) }
    var selectedPlanName by remember { mutableStateOf("") }
    var selectedSurah by remember { mutableIntStateOf(1) }
    var dailyCount by remember { mutableIntStateOf(3) }

    if (showNewPlanDialog) {
        AlertDialog(
            onDismissRequest = { showNewPlanDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = QuranColors.Surface,
            title = {
                Text("خطة حفظ جديدة", fontWeight = FontWeight.Bold, color = QuranColors.Primary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = selectedPlanName,
                        onValueChange = { selectedPlanName = it },
                        label = { Text("اسم الخطة") },
                        placeholder = { Text("مثال: حفظ سورة البقرة") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dailyCount.toString(),
                        onValueChange = { dailyCount = it.toIntOrNull() ?: 3 },
                        label = { Text("عدد الآيات يومياً") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedPlanName.isNotBlank()) {
                            val plan = net.onws.alquranalkarim.data.local.MemorizationPlan(
                                name = selectedPlanName,
                                surahNumber = selectedSurah,
                                dailyAyahCount = dailyCount
                            )
                            bookmarkManager.saveMemorizationPlan(plan)
                            plans = bookmarkManager.getMemorizationPlans()
                            showNewPlanDialog = false
                            selectedPlanName = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuranColors.Primary)
                ) {
                    Text("إنشاء")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPlanDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("خطة الحفظ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("خطط حفظك اليومي والأنسبوعي", fontSize = 14.sp, color = QuranColors.GoldLight)
                        }
                    }
                }
            }

            if (plans.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = QuranColors.TextTertiary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "لا توجد خطط حفظ بعد",
                            fontSize = 18.sp,
                            color = QuranColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "أنشئ خطتك الأولى وابدأ رحلة الحفظ",
                            fontSize = 14.sp,
                            color = QuranColors.TextTertiary
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(plans) { plan ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = QuranColors.Primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        plan.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranColors.TextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "${plan.dailyAyahCount} آيات يومياً | من الآية ${plan.startAyah} إلى ${plan.endAyah}",
                                    fontSize = 13.sp,
                                    color = QuranColors.TextTertiary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // Progress
                                val progress = if (plan.endAyah > plan.startAyah)
                                    plan.completedAyahs.size.toFloat() / (plan.endAyah - plan.startAyah + 1)
                                else 0f
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = QuranColors.Gold,
                                    trackColor = QuranColors.PrimarySurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${plan.completedAyahs.size} آية محفوظة من ${plan.endAyah - plan.startAyah + 1}",
                                    fontSize = 12.sp,
                                    color = QuranColors.TextTertiary
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // FAB
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {
                FloatingActionButton(
                    onClick = { showNewPlanDialog = true },
                    modifier = Modifier.padding(16.dp),
                    containerColor = QuranColors.Primary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "خطة جديدة", tint = Color.White)
                }
            }
        }
    }
}

// ==================== Memorization Test Screen ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemorizationTestScreen(
    onBackClick: () -> Unit,
    surahNumber: Int = 1,
    ayahText: String = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
) {
    val words = remember(ayahText) { ayahText.split(" ") }
    var hiddenIndices by remember { mutableStateOf(setOf<Int>()) }
    var revealedWords by remember { mutableStateOf(setOf<Int>()) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    // Hide ~30% of words randomly
    LaunchedEffect(ayahText) {
        val count = maxOf(1, (words.size * 0.3).toInt())
        hiddenIndices = words.indices.shuffled().take(count).toSet()
        revealedWords = emptySet()
        score = 0
        showResult = false
    }

    fun checkAnswer(wordIndex: Int) {
        if (wordIndex in hiddenIndices && wordIndex !in revealedWords) {
            revealedWords = revealedWords + wordIndex
            score++
            if (revealedWords.size == hiddenIndices.size) {
                showResult = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("اختبار الحفظ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("اضغط على الكلمات المخفية للكشف", fontSize = 14.sp, color = QuranColors.GoldLight)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "الكلمات المخفية: ${hiddenIndices.size}",
                        fontSize = 14.sp,
                        color = QuranColors.TextSecondary
                    )
                    Text(
                        "تم كشف: ${revealedWords.size} / ${hiddenIndices.size}",
                        fontSize = 14.sp,
                        color = QuranColors.Primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { if (hiddenIndices.isNotEmpty()) revealedWords.size.toFloat() / hiddenIndices.size else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = QuranColors.Gold,
                    trackColor = QuranColors.PrimarySurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Ayah text with hidden words
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "اضغط على المساحات الفارغة للكشف عن الكلمات",
                            fontSize = 12.sp,
                            color = QuranColors.TextTertiary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        // Render words with wrapping using FlowRow-like approach
                        Column(modifier = Modifier.fillMaxWidth()) {
                            var currentRowWords = mutableListOf<@Composable () -> Unit>()
                            var currentRowWidth = 0f

                            // Simple column approach for Arabic text
                            androidx.compose.foundation.layout.FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                words.forEachIndexed { index, word ->
                                    if (index in hiddenIndices && index !in revealedWords) {
                                        // Hidden word - clickable
                                        Surface(
                                            modifier = Modifier.clickable { checkAnswer(index) },
                                            shape = RoundedCornerShape(8.dp),
                                            color = QuranColors.PrimarySurface,
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                QuranColors.Primary.copy(alpha = 0.3f)
                                            )
                                        ) {
                                            Text(
                                                "______",
                                                fontSize = 20.sp,
                                                color = QuranColors.Primary.copy(alpha = 0.5f),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    } else if (index in hiddenIndices && index in revealedWords) {
                                        // Revealed hidden word
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = QuranColors.Success.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                word,
                                                fontSize = 20.sp,
                                                color = QuranColors.Success,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                                            )
                                        }
                                    } else {
                                        // Normal word
                                        Text(
                                            word,
                                            fontSize = 20.sp,
                                            color = QuranColors.TextPrimary,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (showResult) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = QuranColors.Success.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🎉", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "أحسنت!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuranColors.Success
                            )
                            Text(
                                "لقد كشفت جميع الكلمات المخفية",
                                fontSize = 15.sp,
                                color = QuranColors.TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val count = maxOf(1, (words.size * 0.3).toInt())
                            hiddenIndices = words.indices.shuffled().take(count).toSet()
                            revealedWords = emptySet()
                            showResult = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuranColors.Primary)
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("إعادة الاختبار", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==================== Memorization Progress Screen ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemorizationProgressScreen(
    onBackClick: () -> Unit,
    bookmarkManager: net.onws.alquranalkarim.data.local.BookmarkManager
) {
    val plans = remember { bookmarkManager.getMemorizationPlans() }
    val stats = remember { bookmarkManager.getReadingStats() }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("متابعة الحفظ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("إحصائيات ونسب الإنجاز", fontSize = 14.sp, color = QuranColors.GoldLight)
                        }
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Overall stats
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "الإحصائيات العامة",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuranColors.Primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatColumn("الآيات المقروءة", "${stats.totalAyahsRead}")
                                StatColumn("الأيام المتتالية", "${stats.streak}")
                                StatColumn("أطول سلسلة", "${stats.longestStreak}")
                            }
                        }
                    }
                }

                // Per-plan progress
                if (plans.isNotEmpty()) {
                    item {
                        Text(
                            "خطط الحفظ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.Primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(plans) { plan ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = QuranColors.Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        plan.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranColors.TextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                val total = plan.endAyah - plan.startAyah + 1
                                val progress = if (total > 0) plan.completedAyahs.size.toFloat() / total else 0f
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(5.dp)),
                                    color = QuranColors.Gold,
                                    trackColor = QuranColors.PrimarySurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "${plan.completedAyahs.size} / $total آية",
                                        fontSize = 13.sp,
                                        color = QuranColors.TextTertiary
                                    )
                                    Text(
                                        "${(progress * 100).toInt()}%",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranColors.Primary
                                    )
                                }
                            }
                        }
                    }
                }

                // Daily goal
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "الهدف اليومي",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuranColors.Primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val dailyProgress = if (stats.dailyGoal > 0)
                                stats.dailyAyahsRead.toFloat() / stats.dailyGoal else 0f
                            LinearProgressIndicator(
                                progress = { minOf(dailyProgress, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                color = if (dailyProgress >= 1f) QuranColors.Success else QuranColors.Gold,
                                trackColor = QuranColors.PrimarySurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${stats.dailyAyahsRead} / ${stats.dailyGoal} آية اليوم",
                                fontSize = 14.sp,
                                color = QuranColors.TextSecondary
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = QuranColors.Primary)
        Text(label, fontSize = 12.sp, color = QuranColors.TextTertiary, textAlign = TextAlign.Center)
    }
}

// ==================== Hizb Screen ====================
data class HizbInfo(
    val hizbNumber: Int,
    val quarterNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val juzNumber: Int
)

private val hizbList = listOf(
    HizbInfo(1, 1, "الفاتحة", 1, 1),
    HizbInfo(1, 2, "البقرة", 26, 1),
    HizbInfo(1, 3, "البقرة", 44, 1),
    HizbInfo(1, 4, "البقرة", 60, 1),
    HizbInfo(2, 1, "البقرة", 75, 1),
    HizbInfo(2, 2, "البقرة", 92, 1),
    HizbInfo(2, 3, "البقرة", 106, 1),
    HizbInfo(2, 4, "البقرة", 124, 1),
    HizbInfo(3, 1, "البقرة", 142, 2),
    HizbInfo(3, 2, "البقرة", 158, 2),
    HizbInfo(3, 3, "البقرة", 177, 2),
    HizbInfo(3, 4, "البقرة", 189, 2),
    HizbInfo(4, 1, "البقرة", 203, 2),
    HizbInfo(4, 2, "البقرة", 219, 2),
    HizbInfo(4, 3, "البقرة", 233, 2),
    HizbInfo(4, 4, "البقرة", 243, 2),
    HizbInfo(5, 1, "البقرة", 253, 2),
    HizbInfo(5, 2, "البقرة", 263, 2),
    HizbInfo(5, 3, "البقرة", 272, 2),
    HizbInfo(5, 4, "البقرة", 283, 3),
    HizbInfo(6, 1, "آل عمران", 15, 3),
    HizbInfo(6, 2, "آل عمران", 33, 3),
    HizbInfo(6, 3, "آل عمران", 52, 3),
    HizbInfo(6, 4, "آل عمران", 75, 3),
    HizbInfo(7, 1, "آل عمران", 93, 3),
    HizbInfo(7, 2, "آل عمران", 113, 3),
    HizbInfo(7, 3, "آل عمران", 133, 3),
    HizbInfo(7, 4, "آل عمران", 153, 3),
    HizbInfo(8, 1, "آل عمران", 171, 4),
    HizbInfo(8, 2, "النساء", 1, 4),
    HizbInfo(8, 3, "النساء", 12, 4),
    HizbInfo(8, 4, "النساء", 24, 4),
    HizbInfo(9, 1, "النساء", 36, 4),
    HizbInfo(9, 2, "النساء", 47, 4),
    HizbInfo(9, 3, "النساء", 60, 4),
    HizbInfo(9, 4, "النساء", 74, 4),
    HizbInfo(10, 1, "النساء", 87, 5),
    HizbInfo(10, 2, "النساء", 100, 5),
    HizbInfo(10, 3, "النساء", 114, 5),
    HizbInfo(10, 4, "النساء", 135, 5),
    HizbInfo(11, 1, "النساء", 148, 5),
    HizbInfo(11, 2, "النساء", 163, 5),
    HizbInfo(11, 3, "المائدة", 1, 5),
    HizbInfo(11, 4, "المائدة", 12, 6),
    HizbInfo(12, 1, "المائدة", 23, 6),
    HizbInfo(12, 2, "المائدة", 32, 6),
    HizbInfo(12, 3, "المائدة", 43, 6),
    HizbInfo(12, 4, "المائدة", 55, 6),
    HizbInfo(13, 1, "المائدة", 67, 6),
    HizbInfo(13, 2, "المائدة", 82, 6),
    HizbInfo(13, 3, "المائدة", 97, 6),
    HizbInfo(13, 4, "المائدة", 109, 6),
    HizbInfo(14, 1, "الأنعام", 1, 7),
    HizbInfo(14, 2, "الأنعام", 20, 7),
    HizbInfo(14, 3, "الأنعام", 36, 7),
    HizbInfo(14, 4, "الأنعام", 53, 7),
    HizbInfo(15, 1, "الأنعام", 69, 7),
    HizbInfo(15, 2, "الأنعام", 83, 7),
    HizbInfo(15, 3, "الأنعام", 100, 7),
    HizbInfo(15, 4, "الأنعام", 114, 7),
    HizbInfo(16, 1, "الأنعام", 131, 8),
    HizbInfo(16, 2, "الأعراف", 1, 8),
    HizbInfo(16, 3, "الأعراف", 23, 8),
    HizbInfo(16, 4, "الأعراف", 41, 8),
    HizbInfo(17, 1, "الأعراف", 58, 8),
    HizbInfo(17, 2, "الأعراف", 74, 8),
    HizbInfo(17, 3, "الأعراف", 88, 8),
    HizbInfo(17, 4, "الأعراف", 104, 8),
    HizbInfo(18, 1, "الأعراف", 121, 9),
    HizbInfo(18, 2, "الأعراف", 141, 9),
    HizbInfo(18, 3, "الأعراف", 158, 9),
    HizbInfo(18, 4, "الأعراف", 174, 9),
    HizbInfo(19, 1, "الأعراف", 189, 9),
    HizbInfo(19, 2, "الأنفال", 1, 9),
    HizbInfo(19, 3, "الأنفال", 22, 10),
    HizbInfo(19, 4, "الأنفال", 41, 10),
    HizbInfo(20, 1, "الأنفال", 61, 10),
    HizbInfo(20, 2, "التوبة", 1, 10),
    HizbInfo(20, 3, "التوبة", 19, 10),
    HizbInfo(20, 4, "التوبة", 34, 10),
    HizbInfo(21, 1, "التوبة", 46, 11),
    HizbInfo(21, 2, "التوبة", 60, 11),
    HizbInfo(21, 3, "التوبة", 75, 11),
    HizbInfo(21, 4, "التوبة", 93, 11),
    HizbInfo(22, 1, "التوبة", 111, 11),
    HizbInfo(22, 2, "يونس", 1, 11),
    HizbInfo(22, 3, "يونس", 26, 11),
    HizbInfo(22, 4, "يونس", 53, 12),
    HizbInfo(23, 1, "يونس", 71, 12),
    HizbInfo(23, 2, "يونس", 90, 12),
    HizbInfo(23, 3, "هود", 1, 12),
    HizbInfo(23, 4, "هود", 24, 12),
    HizbInfo(24, 1, "هود", 41, 12),
    HizbInfo(24, 2, "هود", 61, 12),
    HizbInfo(24, 3, "هود", 84, 12),
    HizbInfo(24, 4, "هود", 108, 13),
    HizbInfo(25, 1, "يوسف", 1, 13),
    HizbInfo(25, 2, "يوسف", 21, 13),
    HizbInfo(25, 3, "يوسف", 42, 13),
    HizbInfo(25, 4, "يوسف", 61, 13),
    HizbInfo(26, 1, "يوسف", 80, 13),
    HizbInfo(26, 2, "يوسف", 101, 14),
    HizbInfo(26, 3, "الرعد", 1, 14),
    HizbInfo(26, 4, "الرعد", 19, 14),
    HizbInfo(27, 1, "إبراهيم", 1, 14),
    HizbInfo(27, 2, "الحجر", 1, 14),
    HizbInfo(27, 3, "الحجر", 30, 14),
    HizbInfo(27, 4, "النحل", 1, 14),
    HizbInfo(28, 1, "النحل", 30, 15),
    HizbInfo(28, 2, "النحل", 51, 15),
    HizbInfo(28, 3, "النحل", 75, 15),
    HizbInfo(28, 4, "النحل", 90, 15),
    HizbInfo(29, 1, "الإسراء", 1, 15),
    HizbInfo(29, 2, "الإسراء", 23, 15),
    HizbInfo(29, 3, "الإسراء", 44, 15),
    HizbInfo(29, 4, "الإسراء", 65, 15),
    HizbInfo(30, 1, "الكهف", 1, 16),
    HizbInfo(30, 2, "الكهف", 22, 16),
    HizbInfo(30, 3, "الكهف", 46, 16),
    HizbInfo(30, 4, "الكهف", 66, 16),
    HizbInfo(31, 1, "الكهف", 84, 16),
    HizbInfo(31, 2, "مريم", 1, 16),
    HizbInfo(31, 3, "مريم", 22, 16),
    HizbInfo(31, 4, "مريم", 45, 17),
    HizbInfo(32, 1, "مريم", 65, 17),
    HizbInfo(32, 2, "طه", 1, 17),
    HizbInfo(32, 3, "طه", 51, 17),
    HizbInfo(32, 4, "طه", 83, 17),
    HizbInfo(33, 1, "طه", 111, 17),
    HizbInfo(33, 2, "الأنبياء", 1, 17),
    HizbInfo(33, 3, "الأنبياء", 30, 17),
    HizbInfo(33, 4, "الأنبياء", 58, 18),
    HizbInfo(34, 1, "الأنبياء", 84, 18),
    HizbInfo(34, 2, "الحج", 1, 18),
    HizbInfo(34, 3, "الحج", 19, 18),
    HizbInfo(34, 4, "الحج", 38, 18),
    HizbInfo(35, 1, "المؤمنون", 1, 18),
    HizbInfo(35, 2, "المؤمنون", 36, 18),
    HizbInfo(35, 3, "النور", 1, 18),
    HizbInfo(35, 4, "النور", 21, 19),
    HizbInfo(36, 1, "النور", 35, 19),
    HizbInfo(36, 2, "النور", 53, 19),
    HizbInfo(36, 3, "الفرقان", 1, 19),
    HizbInfo(36, 4, "الفرقان", 21, 19),
    HizbInfo(37, 1, "الفرقان", 44, 19),
    HizbInfo(37, 2, "الشعراء", 1, 19),
    HizbInfo(37, 3, "الشعراء", 52, 20),
    HizbInfo(37, 4, "الشعراء", 111, 20),
    HizbInfo(38, 1, "النمل", 1, 20),
    HizbInfo(38, 2, "النمل", 27, 20),
    HizbInfo(38, 3, "النمل", 56, 20),
    HizbInfo(38, 4, "القصص", 1, 20),
    HizbInfo(39, 1, "القصص", 22, 20),
    HizbInfo(39, 2, "القصص", 44, 21),
    HizbInfo(39, 3, "القصص", 61, 21),
    HizbInfo(39, 4, "العنكبوت", 1, 21),
    HizbInfo(40, 1, "العنكبوت", 27, 21),
    HizbInfo(40, 2, "العنكبوت", 45, 21),
    HizbInfo(40, 3, "لقمان", 1, 21),
    HizbInfo(40, 4, "الأحزاب", 1, 21),
    HizbInfo(41, 1, "الأحزاب", 31, 22),
    HizbInfo(41, 2, "سبأ", 1, 22),
    HizbInfo(41, 3, "سبأ", 24, 22),
    HizbInfo(41, 4, "فاطر", 1, 22),
    HizbInfo(42, 1, "فاطر", 15, 22),
    HizbInfo(42, 2, "يس", 1, 22),
    HizbInfo(42, 3, "يس", 36, 22),
    HizbInfo(42, 4, "الصافات", 1, 23),
    HizbInfo(43, 1, "الصافات", 83, 23),
    HizbInfo(43, 2, "ص", 1, 23),
    HizbInfo(43, 3, "ص", 32, 23),
    HizbInfo(43, 4, "الزمر", 1, 23),
    HizbInfo(44, 1, "الزمر", 32, 24),
    HizbInfo(44, 2, "غافر", 1, 24),
    HizbInfo(44, 3, "غافر", 21, 24),
    HizbInfo(44, 4, "غافر", 41, 24),
    HizbInfo(45, 1, "فصلت", 1, 24),
    HizbInfo(45, 2, "فصلت", 25, 24),
    HizbInfo(45, 3, "الشورى", 1, 25),
    HizbInfo(45, 4, "الشورى", 23, 25),
    HizbInfo(46, 1, "الزخرف", 1, 25),
    HizbInfo(46, 2, "الزخرف", 24, 25),
    HizbInfo(46, 3, "الدخان", 1, 25),
    HizbInfo(46, 4, "الجاثية", 1, 25),
    HizbInfo(47, 1, "الأحقاف", 1, 26),
    HizbInfo(47, 2, "محمد", 1, 26),
    HizbInfo(47, 3, "الفتح", 1, 26),
    HizbInfo(47, 4, "الحجرات", 1, 26),
    HizbInfo(48, 1, "ق", 1, 26),
    HizbInfo(48, 2, "الذاريات", 1, 26),
    HizbInfo(48, 3, "الطور", 1, 27),
    HizbInfo(48, 4, "النجم", 1, 27),
    HizbInfo(49, 1, "القمر", 1, 27),
    HizbInfo(49, 2, "الرحمن", 1, 27),
    HizbInfo(49, 3, "الواقعة", 1, 27),
    HizbInfo(49, 4, "الحديد", 1, 27),
    HizbInfo(50, 1, "المجادلة", 1, 28),
    HizbInfo(50, 2, "الحشر", 1, 28),
    HizbInfo(50, 3, "الممتحنة", 1, 28),
    HizbInfo(50, 4, "الصف", 1, 28),
    HizbInfo(51, 1, "الجمعة", 1, 28),
    HizbInfo(51, 2, "المنافقون", 1, 28),
    HizbInfo(51, 3, "التغابن", 1, 28),
    HizbInfo(51, 4, "الطلاق", 1, 28),
    HizbInfo(52, 1, "التحريم", 1, 28),
    HizbInfo(52, 2, "الملك", 1, 29),
    HizbInfo(52, 3, "القلم", 1, 29),
    HizbInfo(52, 4, "الحاقة", 1, 29),
    HizbInfo(53, 1, "المعارج", 1, 29),
    HizbInfo(53, 2, "نوح", 1, 29),
    HizbInfo(53, 3, "الجن", 1, 29),
    HizbInfo(53, 4, "المزمل", 1, 29),
    HizbInfo(54, 1, "المدثر", 1, 29),
    HizbInfo(54, 2, "القيامة", 1, 29),
    HizbInfo(54, 3, "الإنسان", 1, 29),
    HizbInfo(54, 4, "المرسلات", 1, 29),
    HizbInfo(55, 1, "النبأ", 1, 30),
    HizbInfo(55, 2, "النازعات", 1, 30),
    HizbInfo(55, 3, "عبس", 1, 30),
    HizbInfo(55, 4, "التكوير", 1, 30),
    HizbInfo(56, 1, "الانفطار", 1, 30),
    HizbInfo(56, 2, "المطففين", 1, 30),
    HizbInfo(56, 3, "الانشقاق", 1, 30),
    HizbInfo(56, 4, "البروج", 1, 30),
    HizbInfo(57, 1, "الطارق", 1, 30),
    HizbInfo(57, 2, "الأعلى", 1, 30),
    HizbInfo(57, 3, "الغاشية", 1, 30),
    HizbInfo(57, 4, "الفجر", 1, 30),
    HizbInfo(58, 1, "البلد", 1, 30),
    HizbInfo(58, 2, "الشمس", 1, 30),
    HizbInfo(58, 3, "الليل", 1, 30),
    HizbInfo(58, 4, "الضحى", 1, 30),
    HizbInfo(59, 1, "الشرح", 1, 30),
    HizbInfo(59, 2, "التين", 1, 30),
    HizbInfo(59, 3, "العلق", 1, 30),
    HizbInfo(59, 4, "القدر", 1, 30),
    HizbInfo(60, 1, "البينة", 1, 30),
    HizbInfo(60, 2, "الزلزلة", 1, 30),
    HizbInfo(60, 3, "العاديات", 1, 30),
    HizbInfo(60, 4, "الإخلاص", 1, 30)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HizbScreen(
    onBackClick: () -> Unit,
    onNavigateToAyah: (Int, String, Int) -> Unit
) {
    var selectedJuz by remember { mutableIntStateOf(-1) }

    Box(modifier = Modifier.fillMaxSize().background(QuranColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(QuranColors.GradientStart, QuranColors.GradientEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowForward, "رجوع", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("الأحزاب والأجزاء", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("٦٠ حزباً في ٣٠ جزءاً", fontSize = 14.sp, color = QuranColors.GoldLight)
                        }
                    }
                }
            }

            // Juz filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    onClick = { selectedJuz = -1 },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedJuz == -1) QuranColors.Primary else QuranColors.Surface
                ) {
                    Text(
                        "الكل",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectedJuz == -1) Color.White else QuranColors.TextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
                // Show first few juz chips and a scrollable area
                val juzChips = (1..30).toList()
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(juzChips.size) { index ->
                        val juz = juzChips[index]
                        Surface(
                            onClick = { selectedJuz = if (selectedJuz == juz) -1 else juz },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedJuz == juz) QuranColors.Primary else QuranColors.Surface
                        ) {
                            Text(
                                "$juz",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (selectedJuz == juz) Color.White else QuranColors.TextSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            val filteredHizbs = remember(hizbList, selectedJuz) {
                if (selectedJuz == -1) hizbList else hizbList.filter { it.juzNumber == selectedJuz }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredHizbs) { hizb ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNavigateToAyah(1, hizb.surahName, hizb.ayahNumber)
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = QuranColors.PrimarySurface,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        "${hizb.hizbNumber}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranColors.Primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "حزب ${hizb.hizbNumber} - الربع ${hizb.quarterNumber}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = QuranColors.TextPrimary
                                )
                                Text(
                                    "سورة ${hizb.surahName} - الآية ${hizb.ayahNumber} | الجزء ${hizb.juzNumber}",
                                    fontSize = 12.sp,
                                    color = QuranColors.TextTertiary
                                )
                            }
                            Icon(
                                Icons.Filled.ChevronLeft,
                                contentDescription = null,
                                tint = QuranColors.TextTertiary
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}