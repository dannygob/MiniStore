#############################################
# GENERAL REGLAS BÁSICAS PARA PROYECTOS ANDROID
#############################################

# Mantener clases y miembros usados por Android framework
-keepclassmembers class * {
    public <init>(android.content.Context);
}

# Mantener clases de anotaciones
-keepattributes *Annotation*

#############################################
# HILT / DAGGER
#############################################

# Conservar clases y anotaciones de Hilt y Dagger
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.** { *; }
-keep class com.google.dagger.** { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }

# Evitar errores con clases generadas por Hilt
-dontwarn dagger.hilt.internal.**
-dontwarn javax.inject.**

#############################################
# ROOM
#############################################

# Conservar entidades y DAOs
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

-dontwarn androidx.room.paging.**

#############################################
# RETROFIT / OKHTTP / GSON
#############################################

# Mantener interfaces de Retrofit
-keep interface retrofit2.** { *; }
-keep class retrofit2.** { *; }

# Gson: evitar eliminar campos serializados
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Si usas converters personalizados
-keep class com.example.ministore.network.** { *; }

#############################################
# FIREBASE
#############################################

# Firebase base
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Firestore
-keep class com.google.firestore.** { *; }
-dontwarn com.google.firestore.**

#############################################
# COIL (CARGA DE IMÁGENES)
#############################################

-keep class coil.** { *; }
-dontwarn coil.**

#############################################
# ZXING (Código de barras)
#############################################

-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

#############################################
# COMPOSE
#############################################

# Compose necesita conservar metadata de anotaciones
-keep class androidx.compose.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

#############################################
# OTRAS UTILIDADES Y BUENAS PRÁCTICAS
#############################################

# Conservar lambdas
-dontwarn kotlin.Metadata
-keep class kotlin.Metadata { *; }

# Evitar problemas con código generado
-keepnames class * {
    *;
}

# Evitar advertencias innecesarias
-dontwarn org.jetbrains.annotations.**
-dontwarn androidx.datastore.**
-dontwarn kotlinx.coroutines.**
