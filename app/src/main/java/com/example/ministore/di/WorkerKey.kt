package com.example.ministore.di

import dagger.MapKey
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out androidx.work.ListenableWorker>)
