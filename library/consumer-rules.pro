# Keep all serializable classes
-keep class com.apialerts.contract.** { *; }

# Or more specifically for kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.apialerts.**$$serializer { *; }
-keepclassmembers class com.apialerts.** {
    *** Companion;
}
-keepclasseswithmembers class com.apialerts.** {
    kotlinx.serialization.KSerializer serializer(...);
}