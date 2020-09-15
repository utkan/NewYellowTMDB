package io.github.utkan.ui.screen.list

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
import io.github.utkan.ui.MovieViewItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SearchUseCase {

    fun onSearch(searchTerm: String, viewModelScope: CoroutineScope): Flow<PagingData<MovieModel>>

    class Impl @Inject constructor(
        private val pagingConfig: PagingConfig,
        @MovieSearchSource private val pagingSearchSource: PagingSource<Int, Movie>,
        @MovieItemMapper private val mapper: Mapper<Movie, MovieViewItem>,
    ) : SearchUseCase {

        private var currentQueryValue: String? = null

        private var currentSearchResult: Flow<PagingData<MovieModel>>? = null

        override fun onSearch(searchTerm: String, viewModelScope: CoroutineScope): Flow<PagingData<MovieModel>> {
            val lastResult = currentSearchResult
            if (searchTerm == currentQueryValue && lastResult != null) {
                return lastResult
            }
            currentQueryValue = searchTerm
            (pagingSearchSource as SearchSource).searchTerm = searchTerm

            if (currentQueryValue.isNullOrEmpty()) {
                currentSearchResult = flow { PagingData.empty<MovieModel>() }
                return currentSearchResult as Flow<PagingData<MovieModel>>
            }

            val newResult: Flow<PagingData<MovieModel>> = getMovieListSearchStream(viewModelScope)
                .map { pagingData -> pagingData.map { MovieModel.MovieItem(mapper.map(it)) } }

            currentSearchResult = newResult
            return newResult
        }

        private fun getMovieListSearchStream(viewModelScope: CoroutineScope): Flow<PagingData<Movie>> {
            return initPager { pagingSearchSource }
                .flow.cachedIn(viewModelScope)
        }

        private fun initPager(pagingSourceFactory: () -> PagingSource<Int, Movie>): Pager<Int, Movie> {
            return Pager(
                config = pagingConfig,
                pagingSourceFactory = pagingSourceFactory
            )
        }
    }
}
