package io.github.utkan.ui.screen.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.squareup.picasso.Picasso
import io.github.utkan.R
import io.github.utkan.databinding.ActivityItemDetailBinding
import io.github.utkan.ui.MovieViewItem
import javax.inject.Inject

interface ItemDetailActivityView {
    fun view(): View
    fun setup()
    fun bind(savedInstanceState: Bundle?, movieViewItem: MovieViewItem)

    class Impl @Inject constructor(
        private val activity: FragmentActivity,
        private val viewBinding: ActivityItemDetailBinding,
        private val picasso: Picasso
    ) : ItemDetailActivityView {

        private lateinit var targetSmall: DetailTarget

        private val targetBig = object : DetailTarget(viewBinding.picture) {}

        override fun view(): View {
            return viewBinding.root
        }

        override fun setup() {
            (activity as AppCompatActivity).setSupportActionBar(activity.findViewById(R.id.detail_toolbar))

            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        override fun bind(savedInstanceState: Bundle?, movieViewItem: MovieViewItem) {
            if (savedInstanceState == null) {
                activity.supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, ItemDetailFragment.newInstance(movieViewItem))
                    .commit()
            }
            loadPicture(movieViewItem)
        }

        private fun loadPicture(movieViewItem: MovieViewItem) {
            targetSmall = object : DetailTarget(viewBinding.picture, buildUrl(activity, movieViewItem.backdropUrl)) {

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    super.onBitmapLoaded(bitmap, from)
                    imageView.post {
                        picasso
                            .load(url)
                            .into(targetBig)
                    }
                }
            }

            picasso
                .load(buildSmallUrl(activity, movieViewItem.backdropUrl))
                .into(targetSmall)
        }
    }

}
