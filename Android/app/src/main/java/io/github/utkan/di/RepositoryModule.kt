package io.github.utkan.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.utkan.LoggerConfigurator
import io.github.utkan.common.Mapper
import io.github.utkan.data.network.MovieRemoteSource
import io.github.utkan.data.network.dto.MovieListDto
import io.github.utkan.data.repository.Repository
import io.github.utkan.data.repository.model.MovieList

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {
    @Provides
    fun providesRepository(
        movieRemoteSource: MovieRemoteSource,
        @MovieListDtoMapper movieListMapper: Mapper<MovieListDto, MovieList>
    ): Repository = Repository.Impl(
        movieRemoteSource = movieRemoteSource,
        movieListMapper = movieListMapper
    )
}


@InstallIn(ApplicationComponent::class)
@Module
abstract class ApplicationModule {
    @Binds
    abstract fun bindsRepository(impl: LoggerConfigurator.Impl): LoggerConfigurator
}

