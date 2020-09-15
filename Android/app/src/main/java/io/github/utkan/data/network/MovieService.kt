package io.github.utkan.data.network

import io.github.utkan.data.network.dto.ConfigurationDto
import io.github.utkan.data.network.dto.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("$VERSION/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int
    ): MovieListDto

    @GET("$VERSION/search/movie")
    suspend fun getMovieSearchResults(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieListDto


    @GET("$VERSION/configuration")
    suspend fun getConfiguration(): ConfigurationDto

    companion object {
        private const val VERSION = "3"
    }
}
