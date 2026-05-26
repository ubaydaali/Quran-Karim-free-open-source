# ============================================================================
# ProGuard / R8 Rules — Al-Quran Al-Karim
# ============================================================================
# Purpose: Prevent R8 from obfuscating and stripping classes that are
#          resolved at runtime via reflection — specifically Gson-based
#          JSON deserialization and Retrofit interface proxies.
#
# Root cause addressed: Release builds crash because R8 renames data-class
# fields (e.g. "surahNumber" → "a"), causing Gson to silently return null
# values that propagate as NullPointerExceptions.
# ============================================================================

# ─────────────────────────────────────────────────────────────────────────────
# 1. Preserve source file & line numbers for crash reports (critical for prod)
# ─────────────────────────────────────────────────────────────────────────────
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ─────────────────────────────────────────────────────────────────────────────
# 2. PROJECT DATA LAYER — Full protection against obfuscation & stripping
# ─────────────────────────────────────────────────────────────────────────────

# 2a. API response models (net.onws.alquranalkarim.data.model.**)
#     These are deserialized from JSON by Gson via Retrofit.
#     Keep ALL class names, ALL fields, ALL methods, ALL constructors.
-keep class net.onws.alquranalkarim.data.model.** { *; }

# 2b. Local persistence models & manager (net.onws.alquranalkarim.data.local.**)
#     Serialized/deserialized via Gson ↔ SharedPreferences with TypeToken.
#     Keep ALL class names, ALL fields, ALL methods, ALL constructors.
-keep class net.onws.alquranalkarim.data.local.** { *; }

# 2c. Repository layer — keep public API surface
-keep class net.onws.alquranalkarim.data.repository.** { *; }

# 2d. Retrofit API service interface — required for dynamic proxy
-keep,allowobfuscation interface net.onws.alquranalkarim.data.api.QuranApiService
-keepclassmembers,allowshrinking,allowobfuscation interface net.onws.alquranalkarim.data.api.QuranApiService {
    <methods>;
}

# ─────────────────────────────────────────────────────────────────────────────
# 3. GSON — Comprehensive rules
# ─────────────────────────────────────────────────────────────────────────────

# 3a. Gson uses TypeToken at runtime to resolve generic types.
#     The actual type parameter is erased at compile time, so R8 must not
#     remove or obfuscate the signature metadata.
-keepattributes Signature

# 3b. Keep Gson's internal annotations
-keepattributes *Annotation*

# 3c. Keep TypeToken and its subclasses (used extensively in BookmarkManager)
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# 3d. Keep any class referenced as a TypeToken parameter
#     (e.g. TypeToken<List<SavedBookmark>>{})
-keep class com.google.gson.reflect.TypeToken {
    java.lang.reflect.Type type;
}

# 3e. Keep Gson annotations
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.Expose <fields>;
    @com.google.gson.annotations.Since <fields>;
    @com.google.gson.annotations.Until <fields>;
    @com.google.gson.annotations.JsonAdapter <fields>;
}

# 3f. Keep Gson default type adapters
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.Gson { *; }
-keep class com.google.gson.GsonBuilder { *; }
-keep class com.google.gson.TypeAdapterFactory { *; }
-keep class com.google.gson.TypeAdapter { *; }
-keep class com.google.gson.FieldNamingPolicy { *; }
-keep class com.google.gson.FieldNamingStrategy { *; }

# 3g. Prevent R8 from stripping enum fields used by Gson
-keepclassmembers enum * {
    @com.google.gson.annotations.SerializedName <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ─────────────────────────────────────────────────────────────────────────────
# 4. RETROFIT — Comprehensive rules
# ─────────────────────────────────────────────────────────────────────────────

# 4a. Retrofit uses reflection to inspect annotations on interface methods
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations

# 4b. Keep Retrofit service method parameters
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# 4c. Ignore annotation used for build tooling
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# 4d. Retrofit platform-specific warnings
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# 4e. Keep Retrofit Call and Response types
-keep class retrofit2.** { *; }
-keepclassmembers,allowshrinking,allowobfuscation class retrofit2.** {
    <methods>;
}

# ─────────────────────────────────────────────────────────────────────────────
# 5. OKHTTP — Rules
# ─────────────────────────────────────────────────────────────────────────────
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# ─────────────────────────────────────────────────────────────────────────────
# 6. KOTLIN — Rules
# ─────────────────────────────────────────────────────────────────────────────

# 6a. Keep Kotlin metadata (needed for reflection, data classes, sealed classes)
-keepattributes RuntimeVisibleAnnotations
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# 6b. Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# 6c. Keep Kotlin companion objects & default arguments
-keepclassmembers class ** {
    public static Companion INSTANCE;
}
-keepclassmembers class **$Companion {
    *;
}

# ─────────────────────────────────────────────────────────────────────────────
# 7. GENERAL REFLECTION SAFETY
# ─────────────────────────────────────────────────────────────────────────────

# Keep generic signatures for any class that might use them
-keepattributes Signature,InnerClasses,EnclosingMethod

# Keep the no-arg constructor for all data classes (Gson requirement)
-keepclassmembers class * {
    <init>();
}

# Don't warn about missing classes from optional dependencies
-dontwarn java.lang.invoke.**
-dontwarn sun.misc.Unsafe
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**