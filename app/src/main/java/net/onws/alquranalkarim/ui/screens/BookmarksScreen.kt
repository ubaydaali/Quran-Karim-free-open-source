package net.onws.alquranalkarim.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.onws.alquranalkarim.data.local.BookmarkManager
import net.onws.alquranalkarim.data.local.SavedBookmark
import net.onws.alquranalkarim.ui.theme.QuranColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    bookmarkManager: BookmarkManager,
    onBackClick: () -> Unit,
    onBookmarkClick: (Int, String, Int) -> Unit
) {
    var bookmarks by remember { mutableStateOf(bookmarkManager.getBookmarks()) }
    var showDeleteDialog by remember { mutableStateOf<SavedBookmark?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "المحفوظات",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = QuranColors.Primary,
                    titleContentColor = QuranColors.TextOnPrimary,
                    navigationIconContentColor = QuranColors.TextOnPrimary
                )
            )
        },
        containerColor = QuranColors.Background
    ) { paddingValues ->
        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = QuranColors.TextTertiary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "لا توجد محفوظات",
                        fontSize = 18.sp,
                        color = QuranColors.TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "اضغط على أيقونة الحفظ بجانب أي آية لحفظها",
                        fontSize = 14.sp,
                        color = QuranColors.TextTertiary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        "${bookmarks.size} محفوظة",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuranColors.TextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(bookmarks) { bookmark ->
                    BookmarkItem(
                        bookmark = bookmark,
                        onClick = {
                            onBookmarkClick(bookmark.surahNumber, bookmark.surahName, bookmark.ayahNumber)
                        },
                        onDelete = {
                            showDeleteDialog = bookmark
                        }
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteDialog?.let { bookmark ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("حذف المحفوظة", fontWeight = FontWeight.Bold) },
            text = { Text("هل تريد حذف هذه المحفوظة؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookmarkManager.removeBookmark(bookmark.surahNumber, bookmark.ayahNumber)
                        bookmarks = bookmarkManager.getBookmarks()
                        showDeleteDialog = null
                    }
                ) {
                    Text("حذف", color = QuranColors.Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("إلغاء")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = QuranColors.Surface
        )
    }
}

@Composable
private fun BookmarkItem(
    bookmark: SavedBookmark,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = QuranColors.Surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Surah/Ayah number badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = QuranColors.PrimarySurface,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${bookmark.surahNumber}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranColors.Primary
                        )
                        Text(
                            "آية ${bookmark.ayahNumber}",
                            fontSize = 9.sp,
                            color = QuranColors.PrimaryLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    bookmark.surahName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = QuranColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (bookmark.ayahText.isNotBlank()) {
                    Text(
                        bookmark.ayahText,
                        fontSize = 13.sp,
                        color = QuranColors.TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (bookmark.note.isNotBlank()) {
                    Text(
                        "📝 ${bookmark.note}",
                        fontSize = 12.sp,
                        color = QuranColors.GoldDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = QuranColors.Error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}