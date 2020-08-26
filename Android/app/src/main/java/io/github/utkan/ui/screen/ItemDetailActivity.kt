package io.github.utkan.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ImageView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.github.utkan.R
import io.github.utkan.ui.MovieViewItem
import javax.inject.Inject

//TODO: extract view
@AndroidEntryPoint
class ItemDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        // TODO: use pallet for bg color
        val picture = findViewById<ImageView>(R.id.picture)
        val movieViewItem = intent.getParcelableExtra(ItemDetailFragment.ARG_ITEM) as MovieViewItem
        val url = movieViewItem.backdropUrl

        picasso
            .load(url)
            .into(picture)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, ItemDetailFragment.newInstance(movieViewItem))
                .commit()
        }
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
