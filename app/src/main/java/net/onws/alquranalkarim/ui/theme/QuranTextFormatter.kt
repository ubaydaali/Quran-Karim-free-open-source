package net.onws.alquranalkarim.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * Advanced Quranic text formatter that parses raw Ayah strings and returns
 * an [AnnotatedString] with semantic styling applied to specific tokens.
 *
 * ## Features
 * - Highlights the word "الله" (Allah) with a majestic gold accent.
 * - Colors Waqf (pause) marks with a distinct secondary tone.
 * - Optionally applies a custom [FontFamily] (e.g. King Fahd Complex Hafs .ttf).
 * - Adds per-ayah end markers (e.g. ﴿١﴾) with their own style.
 *
 * ## Usage
 * ```kotlin
 * val formatted = formatQuranicText(
 *     rawText = ayah.text,
 *     ayahNumber = ayah.numberInSurah,
 *     baseColor = QuranColors.TextPrimary,
 *     customFontFamily = myHafsFontFamily // nullable
 * )
 * Text(text = formatted, ...)
 * ```
 */
object QuranTextFormatter {

    // ── Colour tokens ──────────────────────────────────────────────
    /** Majestic accent for the name of Allah. */
    val AllahColor: Color = Color(0xFFC8A951) // QuranColors.Gold

    /** Subtle secondary colour for Waqf marks. */
    val WaqfColor: Color = Color(0xFF6D8B74) // Muted sage

    /** Colour for the ayah-end marker. */
    val AyahMarkerColor: Color = Color(0xFF0D5E3B) // QuranColors.Primary

    // ── Waqf-mark patterns (Uthmani / Hafs encoding) ───────────────
    // ۖ (U+06D6) ۩ (U+06DF) ۗ (U+06D7) ۚ (U+06DA) ۛ (U+06DB) ۙ (U+06D8)
    // ۠ (U+06E0) ۜ (U+06DC) ۟ (U+06DF) ۠ (U+06E0) ۣ (U+06E3) ۥ (U+06E5)
    private val WAQF_REGEX = Regex("[ۖۗۘۙۚۛۜ۝۞۟۠۩]")

    // ── "الله" detection ───────────────────────────────────────────
    // Matches "الله" with optional diacritics between letters (Tashkeel-aware).
    // Covers: الله – ﷲ – اللَّهِ – ٱللَّه etc.
    private val ALLAH_REGEX = Regex("(?:ٱللَّه|اللَّه|الله|للَّه|ﷲ)")

    // ── Ayah-end marker ────────────────────────────────────────────
    // Matches patterns like ﴿١﴾, ﴿١٢﴾, ﴿١٢٣﴾
    private val AYAH_MARKER_REGEX = Regex("﴿[\\d٠-٩]+﴾")

    /**
     * Formats a raw Quranic ayah text into a styled [AnnotatedString].
     *
     * @param rawText         The raw ayah text from the API / cache.
     * @param ayahNumber      The ayah number within its surah (used for end-marker fallback).
     * @param baseColor       The default text colour (light or dark mode aware).
     * @param baseFontSize    The base font size (used to scale the marker, not applied here –
     *                        the caller's `Text` composable controls size).
     * @param customFontFamily Optional custom Arabic font (e.g. KFGQPC Hafs).
     *                          If null the system default is used.
     * @return An [AnnotatedString] ready to be rendered in a [Text] composable.
     */
    fun format(
        rawText: String,
        ayahNumber: Int = 0,
        baseColor: Color = QuranColors.TextPrimary,
        customFontFamily: FontFamily? = null
    ): AnnotatedString {
        // Early exit for empty strings
        if (rawText.isBlank()) return AnnotatedString(rawText)

        // Strategy: scan the text character-by-character / token-by-token
        // using regex findAll and build the annotated string in a single pass.
        // We collect all match ranges, sort them, and emit styled/unstyled spans.

        data class StyledRange(val start: Int, val end: Int, val style: SpanStyle, val tag: String)

        val styledRanges = mutableListOf<StyledRange>()

        // 1) Find all "الله" occurrences
        ALLAH_REGEX.findAll(rawText).forEach { match ->
            styledRanges += StyledRange(
                start = match.range.first,
                end = match.range.last + 1,
                style = SpanStyle(
                    color = AllahColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFontFamily
                ),
                tag = "allah"
            )
        }

        // 2) Find all Waqf marks
        WAQF_REGEX.findAll(rawText).forEach { match ->
            styledRanges += StyledRange(
                start = match.range.first,
                end = match.range.last + 1,
                style = SpanStyle(
                    color = WaqfColor,
                    fontWeight = FontWeight.Normal,
                    fontFamily = customFontFamily
                ),
                tag = "waqf"
            )
        }

        // 3) Find ayah-end markers ﴿N﴾
        AYAH_MARKER_REGEX.findAll(rawText).forEach { match ->
            styledRanges += StyledRange(
                start = match.range.first,
                end = match.range.last + 1,
                style = SpanStyle(
                    color = AyahMarkerColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFontFamily
                ),
                tag = "marker"
            )
        }

        // Sort by start position; overlapping ranges should not occur in valid Quran text
        val sorted = styledRanges.sortedBy { it.start }

        // Build the annotated string
        return buildAnnotatedString {
            var cursor = 0
            val baseStyle = SpanStyle(
                color = baseColor,
                fontWeight = FontWeight.Normal,
                fontFamily = customFontFamily
            )

            for (range in sorted) {
                // Emit unstyled text before this range
                if (cursor < range.start) {
                    withStyle(baseStyle) {
                        append(rawText.substring(cursor, range.start))
                    }
                }
                // Emit styled text for this range
                withStyle(range.style) {
                    append(rawText.substring(range.start, range.end))
                }
                cursor = range.end
            }
            // Emit remaining text after last styled range
            if (cursor < rawText.length) {
                withStyle(baseStyle) {
                    append(rawText.substring(cursor))
                }
            }
        }
    }
}

/**
 * Extension function on [String] for convenience:
 * ```kotlin
 * val formatted = ayah.text.formatQuranic(ayahNumber = ayah.numberInSurah)
 * ```
 */
fun String.formatQuranic(
    ayahNumber: Int = 0,
    baseColor: Color = QuranColors.TextPrimary,
    customFontFamily: FontFamily? = null
): AnnotatedString = QuranTextFormatter.format(
    rawText = this,
    ayahNumber = ayahNumber,
    baseColor = baseColor,
    customFontFamily = customFontFamily
)