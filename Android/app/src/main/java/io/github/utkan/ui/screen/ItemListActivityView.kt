package io.github.utkan.ui.screen

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.queryTextChangeEvents
import com.squareup.picasso.Picasso
import io.github.utkan.R
import io.github.utkan.databinding.ActivityItemListBinding
import io.github.utkan.ui.MovieViewItem
import io.github.utkan.ui.adapters.MovieAdapter
import io.github.utkan.ui.adapters.MovieLoadStateAdapter
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


interface ItemListActivityView {

    fun view(): View
    fun setup(
        openActivityDetail: (MovieViewItem) -> Unit,
        openFragmentsDetail: (MovieViewItem) -> Unit
    )

    fun onSearch(): Flowable<String>

    fun hideKeyBoard()

    fun onDestroy()

    suspend fun submitData(list: PagingData<MovieModel>)
    suspend fun submitSearchData(list: PagingData<MovieModel>)

    class Impl @Inject constructor(
        private val viewBinding: ActivityItemListBinding,
        private val picasso: Picasso,
        private val activity: FragmentActivity
    ) : ItemListActivityView {

        private val searchEvent = BehaviorSubject.createDefault("")

        override fun onSearch(): Flowable<String> {
            return searchEvent
                .toFlowable(BackpressureStrategy.LATEST)
                .debounce(300, TimeUnit.MILLISECONDS)
                .hide()
        }

        override fun view(): View {
            return viewBinding.root
        }

        private var twoPane: Boolean = false
        private lateinit var movieAdapter: MovieAdapter
        private lateinit var movieSearchAdapter: MovieAdapter
        private var searchDisposable = Disposables.disposed()

        override fun setup(
            openActivityDetail: (MovieViewItem) -> Unit,
            openFragmentsDetail: (MovieViewItem) -> Unit
        ) {
            (activity as? AppCompatActivity)?.setSupportActionBar(viewBinding.toolbar)
            viewBinding.toolbar.title = activity.title
            if (viewBinding.root.findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
                twoPane = true
            }
            val itemListInc = viewBinding.itemListInc
            setupRecyclerView(itemListInc.itemList, openActivityDetail, openFragmentsDetail)
            setupSearchRecyclerView(itemListInc.searchItemList, openActivityDetail, openFragmentsDetail)

            addLoadStateListener(movieAdapter, itemListInc.progressBar)
            addLoadStateListener(movieSearchAdapter, itemListInc.progressBarSearch)

            searchDisposable = viewBinding.searchView
                .queryTextChangeEvents()
                .subscribe({
                    val searchTerm = it.queryText.toString()
                    searchEvent.onNext(searchTerm)
                    val empty = searchTerm.isEmpty()
                    if (empty) {
                        viewBinding.searchView.post {
                            hideKeyBoard()
                        }
                    }
                    val searchItemList = viewBinding.itemListInc.searchItemList
                    val itemList = viewBinding.itemListInc.itemList
                    switchVisibility(empty, searchItemList, itemList)
                }, { Timber.e(it) })

        }

        override suspend fun submitData(list: PagingData<MovieModel>) {
            viewBinding.itemListInc.searchItemList.isVisible = false
            viewBinding.itemListInc.itemList.isVisible = true
            movieAdapter.submitData(list)
        }

        override suspend fun submitSearchData(list: PagingData<MovieModel>) {
            viewBinding.itemListInc.searchItemList.isVisible = true
            viewBinding.itemListInc.itemList.isVisible = false
            movieSearchAdapter.submitData(list)
        }

        override fun onDestroy() {
            searchDisposable.dispose()
        }

        override fun hideKeyBoard() {
            activity.runCatching {
                (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                    hideSoftInputFromWindow(viewBinding.searchView.windowToken, 0)
                }
            }.getOrElse { Timber.e(it) }
        }

        private fun switchVisibility(
            empty: Boolean,
            searchItemList: RecyclerView,
            itemList: RecyclerView
        ) {
            searchItemList.post { searchItemList.isVisible = empty.not() }
            itemList.post { itemList.isVisible = empty }
        }

        private fun addLoadStateListener(adapter: MovieAdapter, progressBar: ProgressBar) {
            adapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Toast.makeText(viewBinding.root.context, it.error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        private fun setupRecyclerView(
            adapter: MovieAdapter,
            recyclerView: RecyclerView,
        ) {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter { adapter.retry() }
            )
        }

        private fun setupRecyclerView(
            recyclerView: RecyclerView,
            openActivityDetail: (MovieViewItem) -> Unit,
            openFragmentsDetail: (MovieViewItem) -> Unit
        ) {
            movieAdapter = initMovieAdapter(openFragmentsDetail, openActivityDetail)

            setupRecyclerView(movieAdapter, recyclerView)
        }

        private fun setupSearchRecyclerView(
            recyclerView: RecyclerView,
            openActivityDetail: (MovieViewItem) -> Unit,
            openFragmentsDetail: (MovieViewItem) -> Unit
        ) {
            movieSearchAdapter = initMovieAdapter(openFragmentsDetail, openActivityDetail)

            setupRecyclerView(movieSearchAdapter, recyclerView)
        }

        private fun initMovieAdapter(
            openFragmentsDetail: (MovieViewItem) -> Unit,
            openActivityDetail: (MovieViewItem) -> Unit
        ): MovieAdapter {
            return MovieAdapter(picasso) { item ->
                if (twoPane) {
                    openFragmentsDetail(item)
                } else {
                    openActivityDetail(item)
                }
            }
        }
    }
}
