# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ------------------------------------------
# TensorFlow Lite ProGuard rules
# ------------------------------------------

# Keep all TensorFlow Lite core classes
-keep class org.tensorflow.lite.** { *; }

# Keep TensorFlow Lite Support Library classes
-keep class org.tensorflow.lite.support.** { *; }

# Keep TensorFlow Lite GPU delegate classes
-keep class org.tensorflow.lite.gpu.** { *; }

# Optional: Preserve model metadata parsing (used by some TFLite utils)
-keep class org.tensorflow.lite.schema.** { *; }

# Optional: Keep annotations used by TensorFlow Lite
-keepattributes *Annotation*