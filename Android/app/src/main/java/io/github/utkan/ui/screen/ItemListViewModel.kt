package io.github.utkan.ui.screen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.utkan.common.Mapper
import io.github.utkan.data.repository.model.Movie
import io.github.utkan.data.repository.paged.SearchSource
import io.github.utkan.di.MovieItemMapper
import io.github.utkan.di.MovieSearchSource
import io.github.utkan.di.MovieSource
import io.github.utkan.ui.MovieViewItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ItemListViewModel @ViewModelInject constructor(
    @MovieSource private val pagingSource: PagingSource<Int, Movie>,
    @MovieSearchSource private val pagingSearchSource: PagingSource<Int, Movie>,
    private val pagingConfig: PagingConfig,
    @MovieItemMapper private val mapper: Mapper<Movie, MovieViewItem>
) : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<MovieModel>>? = null

    val movies: Flow<PagingData<MovieModel>> = getMovieListStream()
        .map { pagingData -> pagingData.map { MovieModel.MovieItem(mapper.map(it)) } }

    fun onSearch(searchTerm: String): Flow<PagingData<MovieModel>> {
        val lastResult = currentSearchResult
        if (searchTerm == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = searchTerm

        if (currentQueryValue.isNullOrEmpty()) {
            return flow { PagingData.empty<MovieModel>() }
        }

        (pagingSearchSource as SearchSource).searchTerm = searchTerm

        val newResult: Flow<PagingData<MovieModel>> = getMovieListSearchStream()
            .map { pagingData -> pagingData.map { MovieModel.MovieItem(mapper.map(it)) } }

        currentSearchResult = newResult
        return newResult

    }

    private fun getMovieListSearchStream(): Flow<PagingData<Movie>> {
        return initPager { pagingSearchSource }
            .flow.cachedIn(viewModelScope)
    }

    private fun getMovieListStream(): Flow<PagingData<Movie>> {
        return initPager { pagingSource }
            .flow.cachedIn(viewModelScope)
    }

    private fun initPager(pagingSourceFactory: () -> PagingSource<Int, Movie>): Pager<Int, Movie> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory
        )
    }

}

sealed class MovieModel {
    data class MovieItem(val movie: MovieViewItem) : MovieModel()
}
