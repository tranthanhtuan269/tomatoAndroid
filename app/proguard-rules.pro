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

-keep class com.meg7.widget.** { *; }

-keep class com.squareup.picasso.** { *; }
-dontwarn com.squareup.picasso.**

-keep class com.sun.mail.** { *; }
-dontwarn com.sun.mail.**

-keep class javax.activation.** { *; }
-dontwarn javax.activation.**

-keep class org.apache.harmony.** { *; }
-dontwarn org.apache.harmony.**

-keep class com.github.siyamed.shapeimageview.** { *; }
-dontwarn com.github.siyamed.shapeimageview.**

-keep class com.google.common.cache.** { *; }
-dontwarn com.google.common.cache.**

-keep class com.google.common.primitives.** { *; }
-dontwarn com.google.common.primitives.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class org.jsoup.** { *; }
-dontwarn org.jsoup.**

-dontwarn retrofit2.Platform$Java8

-dontwarn com.google.android.gms.internal.**
-dontwarn com.google.firebase.crash.**
-keep class com.firebase.** { *; }

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-ignorewarnings


# Needed for Parcelable/SafeParcelable Creators to not get stripped
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

##retrofit
-dontwarn javax.annotation.**

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions


#progard of googleadmod lib
# Keep SafeParcelable value, needed for reflection. This is required to support backwards
# compatibility of some classes.
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

# Keep the names of classes/members we need for client functionality.
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

# Needed when building against the Marshmallow SDK
-dontwarn org.apache.http.**

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}


###glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


#printmapping
-printmapping mapping.txt

#realtime firebase
# Add this global rule
-keepattributes Signature
##new by https://github.com/firebase/quickstart-android/blob/master/database/app/proguard-rules.pro
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.tomato.tuantt.tomatoapp.model.** { *; }

-keep class org.jsoup.** { *; }
-dontwarn org.jsoup.**


-dontwarn com.google.android.gms.internal.**
-dontwarn com.google.firebase.crash.**
-keep class com.firebase.** { *; }
-ignorewarnings

-keep class com.android.vending.billing.**

-keep class * implements java.io.Serializable { *; }

#### picasso
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

#### volley
# Prevent Proguard from inlining methods that are intentionally extracted to ensure locals have a
# constrained liveness scope by the GC. This is needed to avoid keeping previous request references
# alive for an indeterminate amount of time. See also https://github.com/google/volley/issues/114
-keepclassmembers,allowshrinking,allowobfuscation class com.android.volley.NetworkDispatcher {
    void processRequest();
}
-keepclassmembers,allowshrinking,allowobfuscation class com.android.volley.CacheDispatcher {
    void processRequest();
}

####facebook account kit
-keep class com.facebook.FacebookSdk {
   boolean isInitialized();
}
-keep class com.facebook.appevents.AppEventsLogger {
   com.facebook.appevents.AppEventsLogger newLogger(android.content.Context);
   void logSdkEvent(java.lang.String, java.lang.Double, android.os.Bundle);
}