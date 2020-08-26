package io.github.utkan.di

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.utkan.BuildConfig
import io.github.utkan.common.Mapper
import io.github.utkan.data.network.ApiKeyInterceptor
import io.github.utkan.data.network.MovieRemoteSource
import io.github.utkan.data.network.MovieService
import io.github.utkan.data.network.dto.DatesDto
import io.github.utkan.data.network.dto.MovieDto
import io.github.utkan.data.network.dto.MovieListDto
import io.github.utkan.data.repository.Repository
import io.github.utkan.data.repository.mapper.DatesMapper
import io.github.utkan.data.repository.mapper.MovieListMapper
import io.github.utkan.data.repository.mapper.MovieMapper
import io.github.utkan.data.repository.model.Dates
import io.github.utkan.data.repository.model.Movie
import io.github.utkan.data.repository.model.MovieList
import io.github.utkan.data.repository.paged.MoviePagingSource
import io.github.utkan.data.repository.paged.MovieSearchPagingSource
import io.github.utkan.data.repository.paged.PagingSourceKeyProvider
import io.github.utkan.ui.MovieViewItem
import io.github.utkan.ui.screen.mapper.MovieViewItemMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier

@InstallIn(ApplicationComponent::class)
@Module
abstract class NetworkBindsModule {
    @MovieItemMapper
    @Binds
    abstract fun bindsMovieViewItemMapper(impl: MovieViewItemMapper): Mapper<Movie, MovieViewItem>

    @DatesDtoMapper
    @Binds
    abstract fun bindsDatesDtoMapper(impl: DatesMapper): Mapper<DatesDto, Dates>

    @MovieDtoMapper
    @Binds
    abstract fun bindsMovieDtoMapper(impl: MovieMapper): Mapper<MovieDto, Movie>

    @Binds
    abstract fun bindsPagingSourceKeyProvider(impl: PagingSourceKeyProvider.Impl): PagingSourceKeyProvider
}

@InstallIn(ApplicationComponent::class)
@Module
object NetworkProvidesModule {

    @Provides
    fun providesPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 15,
            enablePlaceholders = false
        )
    }

    @Provides
    fun providesMovieRemoteSource(
        api: MovieService
    ): MovieRemoteSource = MovieRemoteSource.Impl(api)

    @MovieSource
    @Provides
    fun providesMoviePagingSource(
        repository: Repository,
        keyProvider: PagingSourceKeyProvider
    ): PagingSource<Int, Movie> {
        return MoviePagingSource(
            repository = repository,
            keyProvider = keyProvider
        )
    }

    @MovieSearchSource
    @Provides
    fun providesSearchMoviePagingSource(
        repository: Repository,
        keyProvider: PagingSourceKeyProvider
    ): PagingSource<Int, Movie> {
        return MovieSearchPagingSource(
            repository = repository,
            keyProvider = keyProvider
        )
    }

    @MovieListDtoMapper
    @Provides
    fun providesMovieListDtoMapper(
        @MovieDtoMapper movieListMapper: Mapper<MovieDto, Movie>,
        @DatesDtoMapper datesMapper: Mapper<DatesDto, Dates>
    ): Mapper<MovieListDto, MovieList> {
        return MovieListMapper(
            movieListMapper = movieListMapper,
            datesMapper = datesMapper
        )
    }

    @Provides
    fun providesMovieService(
        retrofit: Retrofit
    ): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun okHTTPClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            NetworkProvidesModule.interceptors().forEach {
                addInterceptor(it)
            }
        }.build()
    }

    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl("https://api.themoviedb.org/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun loggingInterceptor(): Interceptor = HttpLoggingInterceptor().also {
        if (BuildConfig.DEBUG) {
            it.level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    private fun interceptors(): List<Interceptor> {
        return listOf(
            loggingInterceptor(),
            ApiKeyInterceptor("api_key", "46563f07c17888d841f3e8b6a1b5aa91")
        )
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatesDtoMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieDtoMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieListDtoMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieItemMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieSearchSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieSource

