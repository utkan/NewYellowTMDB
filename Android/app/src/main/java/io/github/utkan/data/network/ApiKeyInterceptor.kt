package io.github.utkan.data.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor constructor(private val name: String, private val value: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter(name, value)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}
