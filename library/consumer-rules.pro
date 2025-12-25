# Keep all serializable classes
-keep class com.apialerts.client.contract.** { *; }

# Or more specifically for kotlinx.serialization
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