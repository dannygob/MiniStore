package com.example.ministore.di

import androidx.work.ListenableWorker
import javax.inject.Inject
import javax.inject.Provider

class HiltWorkerFactory @Inject constructor(
    private val workerFactories: @JvmSuppressWildcards Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<dagger.assisted.AssistedFactory<out ListenableWorker>>>,
) : androidx.work.WorkerFactory() {

    override fun createWorker(
        appContext: android.content.Context,
        workerClassName: String,
        workerParameters: androidx.work.WorkerParameters,
    ): ListenableWorker? {
        val workerFactoryProvider = workerFactories[Class.forName(workerClassName)]
        val workerFactory = workerFactoryProvider?.get() as? SyncProductWorker.Factory
        return workerFactory?.create(appContext, workerParameters)
    }

    interface WorkerFactory<T : ListenableWorker> {
        fun create(
            appContext: android.content.Context,
            workerParameters: androidx.work.WorkerParameters,
        ): T
    }
}
