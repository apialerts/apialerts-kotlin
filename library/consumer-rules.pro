# ── kotlinx.serialization ────────────────────────────────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.apialerts.client.**$$serializer { *; }
-keepclassmembers class com.apialerts.client.** {
    *** Companion;
}
-keepclasseswithmembers class com.apialerts.client.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep all public API classes and contract models
-keep class com.apialerts.client.** { *; }

# ── OkHttp (used by ktor-client-okhttp on Android/JVM) ───────────────────────
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ── Ktor ─────────────────────────────────────────────────────────────────────
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
