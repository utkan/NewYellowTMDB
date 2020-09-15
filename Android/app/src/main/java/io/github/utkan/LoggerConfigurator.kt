package io.github.utkan

import timber.log.Timber
import javax.inject.Inject

interface LoggerConfigurator {

    fun configure()

    class Impl @Inject constructor() : LoggerConfigurator {

        override fun configure() {
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
}
