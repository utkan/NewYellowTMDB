package io.github.utkan.data.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiKeyInterceptor constructor(
    private val name: String,
    private val value: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = newBuilder(request, ::newUrl)
        return chain.proceed(newRequest)
    }

    private fun newBuilder(request: Request, urlBuilder: (Request) -> HttpUrl): Request {
        return request.newBuilder()
            .url(urlBuilder(request))
            .build()
    }

    private fun newUrl(request: Request): HttpUrl {
        return request.url.newBuilder()
            .addQueryParameter(name, value)
            .build()
    }
}
