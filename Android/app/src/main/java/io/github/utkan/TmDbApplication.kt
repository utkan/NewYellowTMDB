package io.github.utkan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.utkan.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class TmDbApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    throw NotImplementedError()
                }
            })
        }
    }
}
