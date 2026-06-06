# ── kotlinx.serialization ────────────────────────────────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep serializers for the @Serializable wire DTOs (contract package).
-keep,includedescriptorclasses class com.apialerts.client.contract.**$$serializer { *; }
-keepclassmembers class com.apialerts.client.contract.** {
    *** Companion;
}
-keepclasseswithmembers class com.apialerts.client.contract.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ── OkHttp (used by ktor-client-okhttp on Android/JVM) ───────────────────────
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ── Ktor ─────────────────────────────────────────────────────────────────────
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
