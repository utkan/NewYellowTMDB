package io.github.utkan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TmDbApplication : Application() {

    @Inject
    lateinit var loggerConfigurator: LoggerConfigurator

    override fun onCreate() {
        super.onCreate()
        loggerConfigurator.configure()
    }
}
