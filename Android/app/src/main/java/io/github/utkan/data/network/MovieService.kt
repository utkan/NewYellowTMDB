package io.github.utkan.data.network

import io.github.utkan.data.network.dto.ConfigurationDto
import io.github.utkan.data.network.dto.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int
    ): MovieListDto

    @GET("3/search/movie")
    suspend fun getMovieSearchResults(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieListDto


    @GET("3/configuration")
    suspend fun getConfiguration(): ConfigurationDto
}
