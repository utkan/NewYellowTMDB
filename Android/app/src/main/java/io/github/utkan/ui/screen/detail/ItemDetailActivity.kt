package io.github.utkan.ui.screen.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.utkan.ui.MovieViewItem
import javax.inject.Inject

@AndroidEntryPoint
class ItemDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var itemDetailView: ItemDetailActivityView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(itemDetailView.view())
        itemDetailView.setup()

        val movieViewItem = intent.getParcelableExtra(ItemDetailFragment.ARG_ITEM) as MovieViewItem
        itemDetailView.bind(savedInstanceState, movieViewItem)

        // TODO: use pallet for bg color
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else              -> {
                super.onOptionsItemSelected(item)
            }
        }

    companion object {
        fun intent(context: Context, item: MovieViewItem): Intent {
            return Intent(context, ItemDetailActivity::class.java).putExtra(ItemDetailFragment.ARG_ITEM, item)
        }
    }
}
