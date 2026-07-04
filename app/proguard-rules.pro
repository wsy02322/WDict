# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the Android SDK tools/proguard/proguard-android-optimize.txt file.

# Keep OkHttp and Gson classes for release builds
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class com.wsy.wdict.OpenRouterApiClient$* { *; }
