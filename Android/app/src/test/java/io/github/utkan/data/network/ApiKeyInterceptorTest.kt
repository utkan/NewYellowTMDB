package io.github.utkan.data.network

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Test

class ApiKeyInterceptorTest {

    @Test
    fun intercept() {
        // given
        val name = "api_key"
        val value = "46563f07c17888d841f3e8b6a1b5aa91"
        val chain: Interceptor.Chain = mock()
        val request: Request = mock()
        val url: HttpUrl = mock()
        val httpBuilder: HttpUrl.Builder = mock()
        whenever(url.newBuilder()).thenReturn(httpBuilder)
        whenever(httpBuilder.addQueryParameter(name, value)).thenReturn(httpBuilder)
        whenever(httpBuilder.build()).thenReturn(url)
        whenever(request.url).thenReturn(url)
        whenever(chain.request()).thenReturn(request)
        val requestBuilder: Request.Builder = mock()
        whenever(requestBuilder.url(url)).thenReturn(requestBuilder)
        whenever(requestBuilder.build()).thenReturn(request)
        whenever(request.newBuilder()).thenReturn(requestBuilder)
        whenever(chain.proceed(request)).thenReturn(mock())

        val apiKeyInterceptor = ApiKeyInterceptor(name, value)

        // when
        apiKeyInterceptor.intercept(chain)

        // then
        verify(httpBuilder).addQueryParameter(name, value)
    }
}
