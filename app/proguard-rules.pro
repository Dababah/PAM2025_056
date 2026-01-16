# 1. Mengabaikan anotasi ErrorProne dan JSR305
-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.annotation.**

# 2. Mengabaikan Google API Client (Error HTTP yang muncul tadi)
-dontwarn com.google.api.client.http.**
-dontwarn com.google.api.client.http.javanet.**

# 3. Mengabaikan Joda Time (Error Instant yang muncul tadi)
-dontwarn org.joda.time.**

# 4. Menjaga library Security agar tetap berfungsi
-keep class com.google.crypto.tink.** { *; }
-keep class androidx.security.crypto.** { *; }

# 5. Tambahan agar R8 tidak terlalu agresif pada library crypto
-dontwarn okio.**
-keepattributes Signature, *Annotation*, EnclosingMethod