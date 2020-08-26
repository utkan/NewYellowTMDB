package io.github.utkan.ui.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.utkan.R
import io.github.utkan.ui.MovieViewItem
import io.reactivex.disposables.Disposables
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ItemListActivity : AppCompatActivity() {

    private val viewModel: ItemListViewModel by viewModels()

    @Inject
    lateinit var itemListView: ItemListActivityView

    private var movieListJob: Job? = null
    private var searchJob: Job? = null

    private var searchDisposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(itemListView.view())

        itemListView.setup(
            openActivityDetail = { startActivity(ItemDetailActivity.intent(this, it)) },
            openFragmentsDetail = { openDetailFragment(it) }
        )

        movieListJob?.cancel()
        movieListJob = lifecycleScope.launch {
            viewModel.movies.collectLatest {
                itemListView.hideKeyBoard()
                itemListView.submitData(it)
            }
        }

        searchDisposable = itemListView
            .onSearch()
            .subscribe({ search(it) }, { Timber.e(it) })
    }

    override fun onDestroy() {
        itemListView.onDestroy()
        searchDisposable.dispose()
        super.onDestroy()
    }

    private fun search(searchTerm: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.onSearch(searchTerm).collectLatest {
                itemListView.submitSearchData(it)
            }
        }
    }

    private fun openDetailFragment(it: MovieViewItem) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.item_detail_container, ItemDetailFragment.newInstance(it))
            .commit()
    }
}
