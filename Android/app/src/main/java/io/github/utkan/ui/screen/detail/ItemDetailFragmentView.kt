package io.github.utkan.ui.screen.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.github.utkan.R
import io.github.utkan.databinding.ItemDetailBinding
import io.github.utkan.ui.MovieViewItem
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

interface ItemDetailFragmentView {
    fun bind(view: View, item: MovieViewItem?)

    class Impl @Inject constructor(
        private val picasso: Picasso
    ) : ItemDetailFragmentView {

        private lateinit var targetSmall: DetailTarget

        private lateinit var targetBig: DetailTarget

        override fun bind(view: View, item: MovieViewItem?) {
            item?.let {
                ItemDetailBinding.bind(view).apply {
                    itemTitle.text = it.originalTitle
                    itemDetail.text = it.overview
                    itemRating.text = it.voteCount.toString()
                    loadPicture(picture, it)
                }
            }
        }

        private fun loadPicture(picture: ImageView?, movieViewItem: MovieViewItem) {
            picture?.run {
                val backdropUrl = movieViewItem.backdropUrl
                targetBig = object : DetailTarget(this) {}
                targetSmall = object : DetailTarget(this, buildUrl(context, backdropUrl)) {

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
                    .load(buildSmallUrl(context, backdropUrl))
                    .into(targetSmall)
            }
        }
    }

}

abstract class DetailTarget constructor(
    val imageView: ImageView,
    val url: String = ""
) : Target {

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        Timber.e(e)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        // no-op
    }
}

fun buildSmallUrl(context: Context, backdropUrl: String?) =
    String.format(backdropUrl ?: "", context.getString(R.string.backdrop_size_small))

fun buildUrl(context: Context, backdropUrl: String?) = String.format(backdropUrl ?: "", context.getString(R.string.backdrop_size))
